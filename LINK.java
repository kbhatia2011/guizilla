package guizilla;



import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;


import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

public class LINK implements ActiveElements {
	
	protected Token openlink = null;
	private Token txt = null;
	protected Token closelink = null;
	public Integer number = null;
	public Browser b = null;
	
	
	public LINK(Integer number, Token openlink, Token txt, Token closelink){
	  this.openlink = openlink;
	  this.txt = txt;
	  this.closelink = closelink;
	  this.number = number;
	}
	
	//Here we create a Hyperlink Class. These hyperlinks are for links and function like buttons
	@SuppressWarnings("serial")
	public class Hyperlink extends JButton {  
		  
		private Color focusColor = Color.blue;  
		
		//constructor
		public Hyperlink(String text) {  
		super(text);  
		setBorderPainted(false);  
		setContentAreaFilled(false);  
		setFocusPainted(false);  
		setOpaque(false);  
		addMouseListener(new RolloverListener());
		addActionListener(new LinkActionListener());
		}  
		
		//tiny methods allowing us to set and get focuscolors
		public void setFocusColor(Color focusColor) {  
		this.focusColor = focusColor;  
		}  
		  
		public Color getFocusColor() {  
		return focusColor;  
		}  
		
		//here we create a RolloverListener class which highlights the 
		//button as the mouse rolls over it. 
		private final class RolloverListener extends MouseInputAdapter {  
		   
		private Color origColor;  
		  
		public void mouseEntered(MouseEvent e) {  
		origColor = getForeground();  
		setForeground(focusColor);  
		}  
		   
		public void mouseExited(MouseEvent e) {  
		if (getForeground() == focusColor) {  
		setForeground(origColor);  
		}  
		}  
		}
		
		//creates action listener for links. Allows user to go to the given page in the link.
		private final class LinkActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e){
				activate(b);
			}
		}}
	

	//render method - creates a new hyperlink and adds it to the panel. 
	public void render(JPanel panel){
		Hyperlink link = new Hyperlink(txt.toString());
		panel.add(link);
	}
	
	public String toimplement(){
		String s = "";
		s += txt.toString();
		return s;
	}

	@Override
	public boolean save() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void activate(Browser browse) {
		System.out.print(openlink.getAttr());
		browse.getpage("GET ", openlink.getAttr(), "\r\n");
	}

	@Override
	public Integer getnumber() {
		// TODO Auto-generated method stub
		return this.number;
	}

}

