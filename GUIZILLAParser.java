package guizilla;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;


public class GUIZILLAParser {
	/* In this class each parse(thing) method handles parsing all productions for non-terminal thing.
	 * 
	 * Use:
	 * Instance with a tokenizer
	 * then call the toimplement() or any other necessary methods method, returns the parse tree for valid input, otherwise
	 * throws ParseException or LexicalException.
	 * 
	 */

		
		protected HTMLTokenizer t = null;
		private Integer counter = 3;
		private ArrayList<FORMSTUFF> tempinputs = new ArrayList<FORMSTUFF>();
		private Integer forms = 0;
		private ArrayList<FORM> form = new ArrayList<FORM>();
		private Token tempstorage = null;
		private ArrayList<FORMSTUFF> tempsubmit = new ArrayList<FORMSTUFF>();
		public Browser browser = null;
		private ArrayList<ActiveElements> l = new ArrayList<ActiveElements>();
		private HashMap<Integer,ActiveElements> activecollection = new HashMap<Integer, ActiveElements>();
		
		/*
		 * Constructs a Parser with a given reader, and makes a new HTMLTokenizer "t" of that reader
		 */

		public GUIZILLAParser(Reader r, Browser b){
			t = new HTMLTokenizer(r);
			this.browser = b;
		}
		
		/* parse(): tries advancing "t" and eating its next token. If it is OPENHTML, as it should be, eats the next token as well,
		 * an OPENBODY, and returns a new HTMLPAGE whose input is parseHTMLSTUFF() (that is, on what is left of the untokenized code).
		 * It also goes through the page's activeElements and places them all in a hashmap for this page, from the integer by which
		 * the element is called to the element itself.
		 */
		public HTMLPAGE parse() throws ParseException {
			t.advance();
			if(eatToken(HTMLConstants.OPENHTML)){
			eatToken(HTMLConstants.OPENBODY);
			final HTMLPAGE toreturn = new HTMLPAGE(parseHTMLSTUFF());
			for(ActiveElements x : l){
				activecollection.put(x.getnumber()+1, x);
			}
			for(FORM m : form){
				toreturn.forms.put(m.number, m);
			}
			toreturn.activeelements = this.activecollection;
			return toreturn;
			}
			else throw new ParseException("HTMLPAGE: Bitch this ain't no HTML page");
		}

		/* parseHTMLSTUFF(): tries eating the next token. If it is CLOSEBODY, eats the following CLOSEHTML and EOF todkens as well
		 * and returns a new HTMLSTUFF without any more code to parse, thus finishing the parse tree. If it is an OPENPARAGRAPH, 
		 * returns a new HTMLSTUFF with 2 constructor arguments, PARASTUFF() and PARSE
		 * It also goes through the page's activeElements and places them all in a hashmap for this page, from the integer by which
		 * the element is called to the element itself.
		 */
		public HTMLSTUFF parseHTMLSTUFF() throws ParseException {
			if(eatToken(HTMLConstants.CLOSEBODY)){
				System.out.println(t.current().toString());
				eatToken(HTMLConstants.CLOSEHTML);
				eatToken(HTMLConstants.EOF);
				return new HTMLSTUFF();
			}
			else if(eatToken(HTMLConstants.OPENPARAGRAPH)){
				return new HTMLSTUFF(parsePARASTUFF(), parseHTMLSTUFF());
			}
			else return new HTMLSTUFF(parseFORM(), parseHTMLSTUFF());
			}
		
		/* parseFORM(): tries eating the next token. If it is OPENFORM, stores the current token as "curr" and creates a new form
		 * with the appropriate number and submit value. Otherwise, throws ParseException.
		 */
		public FORM parseFORM() throws ParseException{
			Token curr = t.current();
			eatToken(HTMLConstants.OPENFORM);
			this.tempstorage = curr;
			forms = forms + 1;
			final FORM toreturn = new FORM(parseFORMSTUFF());
			for(FORMSTUFF x: tempsubmit){
				for(FORMSTUFF m : tempinputs){
					x.inputs.put(m.getnumber(), m);
				}
				toreturn.submit.put(x.number, x);
			}
			tempinputs.clear();
			tempsubmit.clear();
			toreturn.number = forms;
			form.add(toreturn);
			return toreturn;
		}
		
		/* parseFORMSTUFF(): tries eating the next token. If it is OPENPARAGRAPH, stores the current token as "curr" and creates a
		 * new formstuff with the parastuff and formstuff constructors. If it is INPUT, creates a new "input" formstuff with a number
		 * to identify it (generated from a counter), the title of the form-part (from curr.getAttr()), and the parsing of what remains,
		 * which should be a formstuff. If it is SUBMIT, creates a new "submit" formstuff with a number to identify it, the title of
		 * the form-part (from curr), and the parsing of what remains, which should be a FORMSTUFF. If it is CLOSEFORM, the form is
		 * over and and an argument-less FORMSTUFF constructor is called that moves on to parsing the rest of the code.
		 */
		public FORMSTUFF parseFORMSTUFF() throws ParseException{
			Token curr = t.current();
			if(eatToken(HTMLConstants.OPENPARAGRAPH))
			{
				return new FORMSTUFF(parsePARASTUFF(), parseFORMSTUFF());
			}
			else if (eatToken(HTMLConstants.INPUT)){
				counter= counter + 1;
				final FORMSTUFF input = new FORMSTUFF(counter, curr.getAttr(), parseFORMSTUFF());
				input.b = browser;
				l.add(input);
				tempinputs.add(input);
				input.form = forms;
				return input;
			}
			else if (eatToken(HTMLConstants.SUBMIT)){
				counter = counter + 1;
			    FORMSTUFF submit = new FORMSTUFF(counter, curr, parseFORMSTUFF());
			    submit.b = browser;
			    submit.form = forms;
				submit.submitlink = this.tempstorage;
				l.add(submit);
				tempsubmit.add(submit);
				return submit;
			}
			else if (eatToken(HTMLConstants.CLOSEFORM)){
				return new FORMSTUFF();
			}
			else throw new ParseException("FORMSTUFF: didn't get what expected. Get your act together - yeesh!");
		}
		
		/*
		 *  that moves on to parsing the rest of the code (and returns it). If it is TEXT, returns a new PARASTUFF with text followed by calling
		 *  parsePARASTUFF() on what follows. Otherwise, since we must have encountered a link, returns a new PARASTUFF with
		 *  parseLINK() on what follows, followed by parsePARASTUFF() on what follows that.
		 */
		public PARASTUFF parsePARASTUFF() throws ParseException{
			
			Token curr = t.current();
			if(eatToken(HTMLConstants.CLOSEPARAGRAPH)){
				return new PARASTUFF();
			}
			
			else if(eatToken(HTMLConstants.TEXT)){
				final TEXT x = new TEXT(curr);
				return new PARASTUFF(x, parsePARASTUFF());
			}
			else {
				return new PARASTUFF(parseLINK(), parsePARASTUFF());
			}
		}
		
		/* parseLINK(): tries eating the next token. If it is an OPENLINK, makes a new LINK from the activeelements counter,
		 * the OPENLINK token, the following text (what the user sees as being the link's name), and the following CLOSELINK token.
		 * Otherwise, throws ParseException.
		 */
		public LINK parseLINK() throws ParseException{
			Token curr = t.current();
			if(eatToken(HTMLConstants.OPENLINK)){
				Token text = t.current();
				eatToken(HTMLConstants.TEXT);
				Token closelink = t.current();
				eatToken(HTMLConstants.CLOSELINK);
				counter = counter + 1;
				final LINK toputout = new LINK(counter, curr, text, closelink);
				toputout.b = browser;
				l.add(toputout);
				return toputout;
			}
			
			else throw new ParseException("LINK: this ain't no link woman.");
		}
		
		/* parseTEXT(): tries eating the next token. If it is text, returns a new TEXT with that text.
		 * Otherwise, throws ParseException.
		 */
		public TEXT parseTEXT() throws ParseException{
			Token curr = t.current();
			if(eatToken(HTMLConstants.TEXT)){
				return new TEXT(curr);
			}
			else 
				throw new ParseException("TEXT: WTF this isn't text bitch.");
		}
		
		/* eatToken: takes an integer representing the kind of "token" expected of the tokenizer's current. If they are indeed the
		 * same, advances the tokenizer and returns true; otherwise, returns false.
		 */
		protected boolean eatToken(int kind) throws ParseException {
			if (t.current().getKind() != kind || t.current() == null)
				return false;
			else {
				t.advance();
				return true;
			}
		}
		
		/* expectToken: takes an integer representing the kind of "token" expected of the tokenizer's current. If they are indeed the
		 * same, returns true; otherwise, throws a ParseException telling the user what kind was expected and what kind was found.
		 */
		protected boolean expectToken(int kind) throws ParseException {
			if (!eatToken(kind))
				throw new ParseException("Expected to find " + kind
						+ ", but instead found " + t.current().getKind() + ".");
			return true;
		}
}
