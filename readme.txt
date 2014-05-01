README

Sparkzilla's main method is found within the class "Browser". Upon opening the program, the main method makes a new browser and a bufferedreader
to read lines of user input, which are then fed to the browser's "run" method. "Run" takes a BufferedReader, from the main method, that reads lines of user input.
If a given input line is not an integer from 1 to (the number of activeelements this page has + 3), prints a line asking for one; otherwise, performs the command
corresponding to the number.
	 * (1) Back pops the history and sets it equal to the current page;
	 * (2) New URL reads a line of user input and calls "getpage" on it;
	 * (3) Quits the while loop in which this whole method is contained, thereby quitting the application
	 * (4 +) Calls activate() on the appropriately numbered activeelement of the page
"Getpage", in turn, takes three strings: "before", "url", and "after". "before" is either "GET" or "POST". If url is not a valid URL--that is, it does not begin
with http:// and some host, or just / and a path--then an error message is printed. Otherwise, the hostname and the path (if there is one) are extracted from the URL, and an appropriate
request is thereby constructed (incorporating the sent, via the socket, to the server. Then the current page is pushed to "history", a stack of th most recently viewed html pages; the
server's response is read, parsed (it prints an error message if this is impossible), and made the current page; and the current page is printed. "after" is empty,
unless this is a form being posted, in which case it contains the relevant form info. To print the current page, a method "printpage" is called: it prints the browser's
current page, followed by a line of instructions telling the user what commands he can make and what numbers they correspond to.

To parse a page, the method SPARK1Parser (in its own file) is called. SPARK1Parser takes a bufferedreader--in this case, of an inputstreamreader of the browser's socket's
getinputstream--and makes a new HTMLTokenizer out of it. Now, as each line is tokenized, the starter parse() method begins the advance through the tokenized code. If it encounters
an OPENHTML token, then it returns an HTMLPAGE that calls parseHTMLSTUFF; otherwise, a ParseException is thrown indicating that, bitch, this ain't no HTML page. Likewise,
PARSEHTMLSTUFF() looks for either of two appropriate tokens, CLOSEBODY or OPENPARAGRAPH, and returns a new HTMLSTUFF with the appropriate constructors (which take as inputs similar
parsing methods). The process continues, in accordance with our LL1 BNF grammar (in its own file), until ultimately, parseHTMLSTUFF is called and encounters an EOF token, in which
case an HTMLSTUFF is returned that takes no more parsed code in its constructor, and no more objects are created; the parse tree is complete.


We've tested this page on many webpages and it works! So NO, there aren't any bugs as far as we can tell.

List of people with whom we collaborated:
import java.util.*;
private ArrayList<String> people = new ArrayList<String>();
people.add("Karishma Bhatia");
people.add("Phil Trammell");

How we tested:

We tested on a bunch of HTML pages and recorded our interactions with those pages. These can be found on Karishma's phone, or in one of our test documents.
1) we first re-tested our parser to see if it would render our test HTML files appropriately. 
2) we tested our server on ice-weasel and on our browser. We have images of our interactions with those. 
	1. our servertest1 image shows us opening our crush-o-meter. 
	2. we write in a name (karishma bhatia)
	3. When we hit submit, we are taken to the next page of the crushometer. This is shown in ServerTest2 image
	4. We enter a name (Karishma) and hit the submit button again. 
	5. We are taken to the final page - I am 75% compatable with myself. ServerTest3
	6. But wait, we didn't submit the crush's name right. So we hit "Submit" again to go back and submit properly again
	7. We are taken to the form to submit the crush's name again(kdawg). ServerTest4 image
	8. And we find that Karishma is not as compatible with Kdawg. ServerTest5 image 

