package guizilla;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class Crush {
	private static final int PORT = 8080;
	private static ServerSocket serversocket;
	private static Socket socket;
	int counter;
	int counter2;
	Hashtable<Integer, Crush_O_Meter> crushhash;
	Hashtable<Integer, ArrayList<String>> titles;
	
	public Crush() throws IOException {
		crushhash = new Hashtable<Integer, Crush_O_Meter>();
		counter = 1;
	}
	
	public abstract class Page implements Cloneable {
		public Object clone() throws CloneNotSupportedException {
			return super.clone();
		}
		public abstract String defaultHandler(Hashtable<String,String> inputs, String id);
	}
	
	public class Search extends Page {	
		public String search() {
			return "<html><body>"+
				"<p>Enter a query:</p>"+
				"<form method=\"post\" action=\""+"/searchresults\">"+
			    "<input type=\"text\" name=\"query\" />"+
				"<input type=\"submit\" value=\"submit\" /></form></body><html>";
		}
				
		
		@Override
		public String defaultHandler(Hashtable<String, String> inputs, String id) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	public class Crush_O_Meter extends Page {
		private String firstname;
		private String lastname;
		public Crush_O_Meter() {
			firstname = null;
			lastname = null;
		}
		
		public Crush_O_Meter clone() throws CloneNotSupportedException {
			Crush_O_Meter com2 = (Crush_O_Meter) super.clone();
			return com2;
		}
		
		public String yourname(String uid) {
			return "<html><body><p>Wanna know how compatible you are with your crush?</p>"+
		    "<form method=\"post\" action=\""+"/" + "id:"+uid+"/crushname\">"+
			"<p>Enter your first name: </p>"+
		    "<input type=\"text\" name=\"firstname\" />"+
			"<p>Enter your last name: </p>"+
		    "<input type=\"text\" name=\"lastname\" />"+
			"<input type=\"submit\" value=\"submit\" />" +
			"<p>Have you submitted the form yet? You really should!</p>"+
			"<input type=\"submit\" value=\"submit\" />" +
		    "</form></body></html>";
		} 
		
		public String crushname(Hashtable<String, String> inputs, String uid) {
				firstname = inputs.get("firstname");
				lastname = inputs.get("lastname");
				
				return "<html><body>"+
				"<form method=\"post\" action=\""+"/"+ "id:" +uid+"/displayResult\">"+
				"<p>Enter your crush's name: </p>"+
				"<input type=\"text\" name=\"crushname\" />"+
				"<input type=\"submit\" value=\"submit\" />"+
				"</form></body></html>";
		}
		
		public String displayResult(Hashtable<String,String> inputs, String uid) {
			String crushname = inputs.get("crushname");
			Random rand = new Random();
			int x = rand.nextInt(100);
			String statement = "";
			if(x<50){
				statement = "Your future looks pretty frickin bleak. I would drop that mother-fudger like a hot potato if I were you.";
			}
			else {
				statement = "What are you doing staring at this screen? Go make babies with " + crushname +"!";
			}
			return "<html><body>"+
			"<p>You wrote your crush's name as \"" + crushname + "\". This is very important--if his or her name is spelled incorrectly, please reenter it." + "</p>"+
			"<form method=\"post\" action=\"/"+ "id:"+ uid +"/crushname\">"+
			"<input type=\"submit\" value=\"submit\" /></form>"+
			"<p>"+ firstname + " " + lastname + ", you and " + crushname + " are " + (new Integer(x)).toString() + "% Compatable! </p>"+
			"<p>" + statement +"</p>" +
			"</body></html>";
		}

		@Override
		public String defaultHandler(Hashtable<String, String> inputs, String id) {
			return null;
		}

	}
	
	public void run() throws IOException, CloneNotSupportedException {
		while (true) {
		System.out.println("Webserver starting up on port 8080");
	    System.out.println("(press ctrl-c to exit)");
	    serversocket = new ServerSocket(PORT);
			Socket socket = serversocket.accept();
			String statusline = "";
			String restofheader = "\r\nServer: Sparkserver/1.0\r\nConnection: close\r\nContent-Type: text/html\r\n";
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String[] firstline = bufferedreader.readLine().split(" ");
			String pagename = "";
			BufferedReader secondreader = null;
			String returned = null;
			try {
				pagename = firstline[1];
			}
			catch (ArrayIndexOutOfBoundsException e) {
				statusline = "HTTP/1.0 500 Internal Server Error";
				pagename = "errorpage";
			}
			if (firstline[0].equals("POST")) {
				if (!pagename.contains("/")) {
					pagename = "errorpage";
					statusline = "HTTP/1.0 400 Bad Request";
				}
				else if (pagename.equals("/searchresults")) {
					Socket socket2 = new Socket("eckert", 8081);
					BufferedWriter searchwriter = new BufferedWriter(new OutputStreamWriter(socket2.getOutputStream()));
					while (!bufferedreader.readLine().contains("Content-Type:")); //Maybe this has one too many readLines?
					String contentlength = bufferedreader.readLine();
					if (contentlength.split(" ")[0].equals("Content-Length:")) {
						try {
							contentlength = contentlength.split(" ")[1];
						}
						catch (ArrayIndexOutOfBoundsException f) {
							statusline = "HTTP/1.0 500 Internal Server Error";
							pagename = "errorpage";
						}
					}
					bufferedreader.readLine();
					String ttosearch = bufferedreader.readLine();
					String tosearch = ttosearch.substring(0, Math.min(Integer.parseInt(contentlength), ttosearch.length())).split("=")[1];
					searchwriter.write("REQUEST\t<"+tosearch+">\n");
					searchwriter.flush();
					BufferedReader searchreader = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
					//System.out.println(searchreader.readLine());
					//String[] sresults = searchreader.readLine().split(" ");
					returned = "<html><body><p>Search results:</p>";
					returned += "<p>"+searchreader.readLine()+"</p>";
					ArrayList<String> titlelist = new ArrayList<String>();
				/*	for (int i=0; i < sresults.length-1; i++) {
						String blarg = sresults[i].split(" ")[1];
						returned += blarg + "\n";
						titlelist.add(blarg);
					} */
					//titles.put(counter2, titlelist);
					counter2++;
					returned += "<form method=\"post\" action=\"searchcall\">"
							  + "<p>Enter a page's name: </p>"
							  + "<input type=\"text\" name=\"pagename\" />"
							  + "<input type=\"submit\" value=\"submit\" />"
							  + "</body></html>";
					socket2.close();
				}
				else {
					String[] broken = pagename.split("/");
					System.out.println(broken[2]);
					if (broken.length != 3) {
						pagename = "errorpage";
						statusline = "HTTP/1.0 400 Bad Request";
					}
					else if (broken[2] == "searchcall") {
						Socket socket2 = new Socket("eckert", 8082);  //Figure out all this socket stuff better
						BufferedWriter swriter = new BufferedWriter(new OutputStreamWriter(socket2.getOutputStream()));
						BufferedReader sreader = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
						String[] formbloop = pagename.split(":")[1].split("/"); //Put this in another try/catch block...
						String formid = formbloop[0];
						while (!bufferedreader.readLine().contains("Content-Type:")); //Maybe this has one too many readLines?
						String contentlength = bufferedreader.readLine();
						try {
							if (contentlength.split(" ")[0] == "Content-Length:") {
								try {
									contentlength = contentlength.split(" ")[1];
								}
								catch (ArrayIndexOutOfBoundsException f) {
									statusline = "HTTP/1.0 500 Internal Server Error";
									pagename = "errorpage";
								}
							}
						} catch (ArrayIndexOutOfBoundsException g) {
							statusline = "HTTP/1.0 500 Internal Server Error";
							pagename = "errorpage";
						}
						bufferedreader.readLine();
						String ttoget = bufferedreader.readLine();
						String toget = ttoget.substring(0, Math.min(Integer.parseInt(contentlength), ttoget.length())).split("=")[1];
						ArrayList<String> thislist = titles.get(formid);
						if (thislist.contains(toget)) {
							swriter.write(toget);
							String a = sreader.readLine();
							while (a != null) {
								returned += a;
								a = sreader.readLine();
							}
						}
						else {
							pagename = "errorpage"; //Maybe make this a special "You didn't enter a valid page name--Go Back!" error page
							statusline = "HTTP/1.0 400 Bad Request";
						}
					}
					else if ((broken[2].equals("crushname")) || (broken[2].equals("displayResult"))) {
						String[] formbloop = pagename.split(":")[1].split("/"); //Put this in another try/catch block...
						String formid = formbloop[0];
						String formcall = formbloop[1];
						System.out.println(formcall);
						while (!bufferedreader.readLine().contains("Content-Type:"));
						String contentlength = bufferedreader.readLine();
						if (contentlength.split(" ")[0].equals("Content-Length:")) {
							try {
								contentlength = contentlength.split(" ")[1];
							}
							catch (ArrayIndexOutOfBoundsException f) {
								statusline = "HTTP/1.0 500 Internal Server Error";
								pagename = "errorpage";
							}
						}
						Crush_O_Meter thiscrush = crushhash.get(Integer.parseInt(formid));
						bufferedreader.readLine();
						String fformdata = bufferedreader.readLine();
						Hashtable<String, String> formtable = new Hashtable<String, String>();
						if (fformdata.contains("=")) {
							String[] formdata = fformdata.substring(0, Math.min(Integer.parseInt(contentlength), fformdata.length())).split("&");
							for (int i = 0; i < formdata.length; i++) {
								String key = formdata[i].split("=")[0];
								String value = URLDecoder.decode(formdata[i].split("=")[1], "UTF-8");
								formtable.put(key, value);
							}
						}
						else if ((formcall.equals("crushname")) && (!(thiscrush.firstname == null))) {
							//System.out.println("oops");
							thiscrush = thiscrush.clone();
							formtable.put("firstname", thiscrush.firstname);
							formtable.put("lastname", thiscrush.lastname);
							Integer formidinteger = new Integer(counter);
							formid = formidinteger.toString();
							crushhash.put(formidinteger, thiscrush);
							counter++;
							returned = thiscrush.crushname(formtable, formid);
						}

						if (formcall.equals("crushname")) {
							returned = thiscrush.crushname(formtable, formid);
						}
						if (formcall.equals("displayResult")) {
							returned = thiscrush.displayResult(formtable, formid);
						}

						}
					}
				}
				else if (pagename.equals("/yourname")) {
					Integer id = new Integer(counter);
					counter++;
					Crush_O_Meter a = new Crush_O_Meter();
					crushhash.put(id, a);
					returned = a.yourname(id.toString());
				}
				else if (pagename.equals("/search")) {
					Search a = new Search();
					returned = a.search();
				}
				else try {
					secondreader = new BufferedReader(new FileReader("src/guizilla/" + pagename + ".html"));
				} catch (FileNotFoundException e) {
					pagename = "errorpage";
					statusline = "HTTP/1.0 404 Not Found";
					secondreader = new BufferedReader(new FileReader("src/guizilla/" + pagename + ".html"));
				}
			statusline = "HTTP/1.0 200 OK";
			BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			String holder = "";
			//bufferedwriter.close();
			if (secondreader != null) {
				while (holder != "</html>") {
					bufferedwriter.write(statusline + restofheader+holder);
					holder = secondreader.readLine();
					bufferedwriter.flush();
					bufferedwriter.close();
				}
			}
			else {
				//BufferedWriter bufferedwriter2 = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				bufferedwriter.write(statusline + restofheader +"\n" + returned);
				System.out.println(returned);
				bufferedwriter.flush();
				bufferedwriter.close();
			}
			serversocket.close();
		}
	}
	
	public static void main (String[] args) throws IOException, CloneNotSupportedException{
		Crush thingy = new Crush();
		thingy.run();
	}
}
