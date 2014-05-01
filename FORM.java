package guizilla;

import java.util.HashMap;

import javax.swing.JPanel;

public class FORM {

	public FORMSTUFF form = null;
	public HashMap<Integer,FORMSTUFF> submit = new HashMap<Integer, FORMSTUFF>();
	public Integer number = null;
	
	
	
	public FORM(FORMSTUFF form){
		this.form = form;
	 }
    
	//render method for form - renders formstuff. 
	public void render(JPanel panel){
		form.render(panel);
	}
	
    public String toimplement(){
    	return "\n" + form.toimplement();
    }
}
