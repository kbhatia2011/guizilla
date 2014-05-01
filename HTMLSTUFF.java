package guizilla;

import javax.swing.JPanel;


public class HTMLSTUFF {


	private FORM form = null;
	private PARASTUFF para = null;
	private HTMLSTUFF more = null;

	
	public HTMLSTUFF(){

	}

    public HTMLSTUFF(FORM form, HTMLSTUFF more){
    	this.form = form;
    	this.more = more;
    }
    
    public HTMLSTUFF(PARASTUFF para, HTMLSTUFF more){
    	this.para = para;
    	this.more = more;
    }
    
    //our render method - only renders if more is not equal to null). 
    public void render(JPanel panel){
    	if (more != null){
    		if (para == null){
    		form.render(panel); 
    		more.render(panel);
    	}
    	else{
    		para.render(panel);
    		more.render(panel);
    	}}
	}
    
    //our to-string method for HTMLStuff 
    public String toimplement(){
    	String s = "";
    	if (more == null){
    		s = "\n";
    	}
    	else if (para == null){
    		s += form.toimplement() + "\n" + more.toimplement();
    	}
    	else{
    		s += para.toimplement() + "\n" + more.toimplement();
    	}
    	return s;
    }

}
