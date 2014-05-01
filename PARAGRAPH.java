package guizilla;



import javax.swing.JPanel;

public class PARAGRAPH {
	
	protected Token openpara = null;
	private PARASTUFF para = null;
	
//constructor
	public PARAGRAPH (Token openpara, PARASTUFF para){
		this.openpara = openpara;
		this.para = para;
	}
	
//renders paragraph
	public void render(JPanel panel){
		para.render(panel);
	}
	
//tostring method
	public String toimplement(){
		return para.toimplement();
	}

}
