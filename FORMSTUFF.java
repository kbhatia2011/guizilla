package guizilla;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.swing.*;

public class FORMSTUFF implements ActiveElements {
	
	protected Token inputsubmit = null;
	protected Token endform = null;
	private PARASTUFF para = null;
	private FORMSTUFF more = null;
	public Integer number =  null;
	public Token submitlink = null;
	public String title = null;
	public JTextField ans = null;
	public String answer = null;
	public HashMap<Integer,FORMSTUFF> inputs = new HashMap<Integer, FORMSTUFF>();
	public Integer form = null;
	public Browser b = null;
	
	public FORMSTUFF(Integer number, String token,  FORMSTUFF more){
		this.more = more;
		this.number = number;
		this.title = token;
	}
	
	public FORMSTUFF (PARASTUFF para, FORMSTUFF more){
		this.para = para;
		this.more = more;
	}

	public FORMSTUFF(Integer number, Token inputsubmit, FORMSTUFF more){
		this.inputsubmit = inputsubmit;
		this.more = more;
		this.number = number;
	}
	
    
    public FORMSTUFF(){
    }
    //creates an action listener for the submit button
    //Uses activate to go to the links
    class SubmitActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			activate(b);
		}
	}
    //creates an action listener for the inputs and assigns the text entered in the textfield to answer
    class InputActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			answer = ans.getText();
		}}
    //the render method for formstuff:
    public void render(JPanel panel){
    	if (more != null){

    	if (para != null){
    		para.render(panel);
    		more.render(panel);
    	}
    	else if (inputsubmit != null){
    		JButton sub = new JButton("Submit");
    		sub.addActionListener(new SubmitActionListener());
    		panel.add(sub);
    		more.render(panel);
    		
    	}
    	else{
    		JTextField txt = new JTextField(1);;
    		if (answer != null){
    		txt.setText(answer);}
    		txt.setPreferredSize(new Dimension(300, 30));
    		this.ans = txt;
    		panel.add(txt);
    		more.render(panel);
    	}}
	}
    
    public String toimplement(){
    	String s = "";
    	if (more == null){
    		s = "\n";
    	}
    	else if (para != null){
    		s += para.toimplement() + more.toimplement();
    	}
    	else if (inputsubmit != null){
    		s += "[Submit][" + number.toString() + "]" + "\n"+more.toimplement();
    	}
    	else{
    		if(answer == null){
    		s += "_____________ [" + number.toString() + "]" + "\n"+ more.toimplement();
    	}
    		else{
    			s += " " + answer.toString() + " ["+number.toString() + "]" + "\n" + more.toimplement();
    		}
    	}
    	return s;
    }

    //builds a string to send to the server (the results of all the forms)
    private String buildstring() throws UnsupportedEncodingException {
    	String a = "";
        for (Integer key : inputs.keySet()) {
            FORMSTUFF value = inputs.get(key);
            System.out.println(value.title);
            String e = "";
            String answ = value.ans.getText();
            System.out.println("got here 2");
            if(answ == null){
            	e = "";
            }
            else{
            	value.answer = answ;
            	e = value.answer;
            }
            a += value.title + "=" + URLEncoder.encode(e, "UTF-8") + "&";
        }
        a = a.substring(0, Math.max(0, a.length()-1));
        return a;
    }
    
	@Override
//activate method activates the submit button. It builds the string with all the answers, and then uses "getpage" to get a page. 
	public void activate(Browser browse){
		try {
		if(inputsubmit != null){
			String a = buildstring();
            String str = "Content-Type: application/x-222-form-urlencoded\r\nContent-Length: "+ a.length() + "\r\n\r\n" + a + "\r\n";
            browse.getpage("POST ", submitlink.getAttr(), str);
		}}
		catch(IOException e){// TODO Auto-generated catch block
			e.printStackTrace();}
	}

	@Override
	public boolean save() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Integer getnumber() {
		// TODO Auto-generated method stub
		return this.number;
	}


}
