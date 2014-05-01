package guizilla;

import java.io.*;

public class TestforParser1 {
	
	static Browser b = null;

	static HTMLPAGE convert(Reader r) throws ParseException, spark1.ParseException{
		GUIZILLAParser x = new GUIZILLAParser(r, b);
		return x.parse();
		}
	
//StringReader("<html><body><p>This is a stupid page for testing.<a href=\"/Test/page2\">This</a> is a link.</p><p>Here is a form:</p><form method=\"post\" action=\"/id:6adf96775832d5/formsubmit\"><p>Favorite food:</p><input type=\"text\" name=\"food\" /><p>Favorite ice cream flavor:</p><input type=\"text\" name=\"icecream\" /><p>Favorite drink:</p><input type=\"text\" name=\"drink\" /><input type=\"submit\" value=\"submit\" /></form></body></html>")
public static void main(String[] args) throws spark1.ParseException, FileNotFoundException{
	
	//testing simple HTMLPage
		System.out.println("Testing on simple HTMLPage");
		BufferedReader test1 = new BufferedReader(new FileReader("src/guizilla/homepage.html"));
		Browser test1b = new Browser();
		try {
			test1b.render(convert(test1));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
}
