package guizilla;

import javax.swing.JPanel;

public class PARASTUFF {

	private PARASTUFF more = null;
	private TEXT txt = null;
	private LINK link = null;
	
	public PARASTUFF(){
	}
	
	public PARASTUFF(TEXT txt, PARASTUFF more){
		this.txt = txt;
		this.more = more;
	}

	public PARASTUFF(LINK link, PARASTUFF more){
		this.link = link;
		this.more = more;
	}
	
	public void render(JPanel panel){
		if(more != null){
			if (txt != null){
    		txt.render(panel);
    		more.render(panel);
    	}
    	else {
    		link.render(panel);
    		more.render(panel);
    	}
		}
	}
    
    
    public String toimplement(){
    	String s = "";
    	if (more == null){
    		s = "";
    	}
    	else if (txt != null){
    		s += txt.toimplement() + more.toimplement();
    	}
    	else {
    		s += link.toimplement() + more.toimplement();
    	}
    	return s;
    }


}
