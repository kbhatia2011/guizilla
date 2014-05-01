package guizilla;

import java.util.Hashtable;
import java.util.Random;

public class Crush_O_Meter {
	private String yourname_;
	public Crush_O_Meter(){
		yourname_ = "";
	}
	
	public String yourname(Hashtable<String, String> t, String id){
		return "<htaml><body><p>Wanna know how compatible you are with your crush?</p>"+
	    "<form method =\"post\" action=\"/"+id+"/secondNumber\">"+
		"<p>Enter your first name followed by your last name: </p>"+
	    "<input type=\"text\" name=\"yourname\" />" +
		"<input type=\"submit\" />" +
	    "</form></body></html>";

	} 
	
	public String crushname(Hashtable<String, String> t, String id){
			yourname_ = t.get("yourname");
			return "<html><body>"+
			"<form method=\"post\" action=\"/"+id+"/displayResult\">"+
			"<p>Enter your crush's first name followed by his/her last name: </p>"+
			"<input type=\"text\" name=\"crushname\" />"+
			"<input type=\"submit\" />"+
			"</form></body></html>";
	}
	
	public String displayResult(Hashtable<String,String> t, String id) {
		String crushname = t.get("crushname");
		Random rand = new Random();
		int x = rand.nextInt(100);
		String statement = "";
		if(x<50){
			statement = "Your future looks pretty frickin bleak. I would drop that mother-fudger like a hot potato if I were you. And yes, this webiste is totally reliable and you should make all your love decisions based on what is written here.";
		}
		else {
			statement = "What are you doing staring at this screen? Go make babies with " + crushname;
		}
		return "<html><body>"+
		"<p>"+ yourname_ + " and " + crushname + " are " + (new Integer(x)).toString() + "% Compatable! </p>"+
		"<p>" + statement +"</p>" +
		"</body></html>";
	}

}

