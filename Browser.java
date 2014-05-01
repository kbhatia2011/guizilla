package guizilla;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Stack;
import java.io.FileReader;
import javax.swing.*;

/* Browser: the overarching object that reads user input and, on its account, gets pages from and posts 
 * information to servers, receives server responses, commands that they be parsed, and prints them.
 */
public class Browser {
	Browser b = this;
	private static Integer PORT = 8080;
	public static Socket socket;
	public HTMLPAGE currpage = null;
	private Stack<String> hostholder = new Stack<String>();
	
	/* convert: takes a BufferedReader, which should be reading html, and returns an HTMLPAGE from it
	 * (or null, if the reader was not reading parsable HTML).
	 */
	public HTMLPAGE convert(BufferedReader r) {
		try {
			GUIZILLAParser x = new GUIZILLAParser(r, b);
			return x.parse();
		} catch (ParseException e) {
			return new HTMLPAGE(new HTMLSTUFF());
		}
	}
	
	//Here we initialize all the elements of our JFrame
	JFrame frame;
	FlowLayout mgr;
	JButton back;
	JTextField url;
	JPanel topbar;
	JScrollPane content;
	
	//This method allows us to go back .
	public void goback() {
			currpage = history.pop();
			hostholder.pop();
			back.setEnabled(false);
			render(currpage);
	}
	
	//we create an actionlistener for the back button that implements "goback()"
	class BackActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			goback();
		}
	}
	
	//a method that allows us to "go to a url"
	public void gotoURL(String url) {
		getpage("GET ", url, "\r\n");
	}
	
	//action listener for our URL panel
	class URLActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e){

			gotoURL(url.getText());
		}
	}
	
	//our "render" method. This method renders any page given an "HTMLPAGE" object	
	public void render(HTMLPAGE curr) {
		//we enable the back button only if history isn't empty. 
		if (!history.isEmpty()){
			back.setEnabled(true);
		}
		if (content != null){
		frame.remove(content);}
		JPanel contents = new JPanel();
		contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
		curr.render(contents);
		content = new JScrollPane(contents);
		frame.add(content);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);	
	}
	
	/* Browser: an initializer for the class. It tries to make a home page by setting "tempreader"--the
	 * Bufferedreader that convert--equal to a BufferedReader of a FileReader of a certain file in the
	 * same directory, which contains the HTML for a home page. If no such file exists, an error is printed.
	 */
	public Browser() {
		try {
			BufferedReader tempreader = new BufferedReader(new FileReader("src/guizilla/homepage.html")); 
			currpage = convert(tempreader);
			frame = new JFrame("Guizilla");
			frame.setPreferredSize(new Dimension(750,750));
			mgr = new FlowLayout();
			back = new JButton("Back");
			back.addActionListener(new BackActionListener());
			back.setEnabled(false);
			url = new JTextField();
			url.setPreferredSize(new Dimension(500, 20));
			url.setText("Enter URL Here...");
			url.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e){
					url.setText("");
				}
			});
			url.addActionListener(new URLActionListener());
			mgr.addLayoutComponent("Back", back);
			mgr.addLayoutComponent("Open", url);
			topbar = new JPanel(mgr);
			topbar.add(back);
			topbar.add(url);
			frame.add(topbar, BorderLayout.NORTH);
			this.render(currpage);
		}
		catch (FileNotFoundException e) {
			System.out.println("Error: homepage doesn't exist.");
		}
	}
	
	void assigncurrpage(){
		BufferedReader x;
		try {
			x = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			currpage = convert(x);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//history: a stack of HTMLPAGES with the last visited page on top, the one before it underneath, and so on
	public Stack<HTMLPAGE> history = new Stack<HTMLPAGE>();
	
	//towrite: the string that all server requests have in common
	public String towrite = " HTTP/1.0\r\nConnection: close\r\nUser-Agent: Guizilla/1.0\r\n";
	
	
	/* portintosocket: takes a string, and makes it the browser's socket's host (changing the server to
	 * which requests are sent), leaving its port unchanged
	 */
	public void portintosocket(String x) {
		try {socket = new Socket(x, PORT.intValue());
		} catch (IOException e) {
			System.out.println("Socket not found.");
		}
	}
	

	
	/* getpage: takes three strings: "before", "url", and "after".
	 * "before" is either "GET" or "POST".
	 * If url is not a valid URL--that is, it does not begin with http:// and some host, or just / and a path--then an error message is printed.
	 * Otherwise, the hostname and the path (if there is one) are extracted from the URL, and an appropriate request is thereby
	 * constructed (incorporating the sent, via the
	 *  socket, to the server. Then the current page is pushed to "history"; the
	 * server's response is read, parsed (it prints an error message if this is impossible), and made the current page; and the current page is printed.
	 * "after" is empty, unless this is a form being posted, in which case it contains the relevant form info.
	 */
	public void getpage(String before, String url, String after) {
		try {
			history.push(currpage);
			String towriteplus = "";
			if (!((url.length() > 7) && (url.substring(0,7).equals("http://")))) {
	    		System.out.print("stopped here");
	    		if(hostholder.peek() != null){
				socket = new Socket(hostholder.peek(), PORT);
				towriteplus = before + url + towrite + after;
				hostholder.push(hostholder.peek());}
			}
			else {
				String[] spliturl = url.split("/");
				if (spliturl.length < 2) System.out.println("Error: URLs in Sparkzilla must either begin with 'http://' and be followed by a hostname, a slash, and then (optionally) a path, or consist only of a subpage on the current host"); 
				else {
					String hostname = spliturl[2];
					String path = "/";
					for (int i=3; i < spliturl.length; i++) {
						path += spliturl[i];
					}
					socket = new Socket(hostname, PORT.intValue());;
					hostholder.push(hostname);
					towriteplus = before + path + towrite + after;
				}
			}
			BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			bufferedwriter.write(towriteplus);
			bufferedwriter.flush();
			socket.shutdownOutput();
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//the bufferedreader reads 5 times to get past the server response headers
			bufferedreader.readLine();
			bufferedreader.readLine();
			bufferedreader.readLine();
			bufferedreader.readLine();
			bufferedreader.readLine();
			GUIZILLAParser newparser = new GUIZILLAParser(bufferedreader, b);
			currpage = newparser.parse();
			render(currpage);
		} catch (IOException e) {
			System.out.println(e);
		} catch (TokenMgrError e) {
			System.out.println("Token Manager Error.");
		} catch (ParseException e) {
			BufferedReader tempreader;
			try {
				//if the server gives an unreadable page or a bad response, we display an error page. 
				tempreader = new BufferedReader(new FileReader("src/guizilla/BadServerResponse.html"));
				currpage = convert(tempreader);
				render(currpage);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		}
	}
	
	// main: makes a new browser, thing, and a new reader to read lines of user input and calls thing.run on the reader;
	public static void main (String[] args) throws ParseException {
		    //create an instance of this class
		  new Browser();
		  }

}
