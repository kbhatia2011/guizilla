package guizilla;

import java.util.HashMap;

import javax.swing.JPanel;

public class HTMLPAGE {
	protected Token openhtml = null;
	protected Token openbody = null;
	private HTMLSTUFF contents = null;
	public HashMap<Integer,FORM> forms = new HashMap<Integer, FORM>();
	
	public HashMap<Integer,ActiveElements> activeelements = new HashMap<Integer, ActiveElements>();

    public HTMLPAGE(HTMLSTUFF contents){
    	this.contents = contents;
    }
    
    //renders the HTMLPage
    public void render(JPanel panel){
		contents.render(panel);
	}
    
    //HTMLPage tostring method
    public String toimplement(){
    	return contents.toimplement();
    }
	
	
	
	

}
