package guizilla;



import javax.swing.*;

public class TEXT {
	protected Token txt = null;
	
	public TEXT (Token text){
		this.txt = text;
	}
    
	public void render(JPanel panel){
		JLabel text = new JLabel(this.toimplement());
		panel.add(text);
	}
	
    public String toimplement(){
    	String s = txt.toString();
    	return s;
    }
}
