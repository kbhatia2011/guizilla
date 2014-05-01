package guizilla;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;

public class Server {
	private static final int PORT = 1820; //?
	private static ServerSocket serversocket;
	
	public Server() throws IOException {
		Server.serversocket = new ServerSocket(PORT);
	}
	
	//pagestack...? I don't remember
	
	public void run() throws IOException {
		while (true) {
			Socket socket = serversocket.accept();
			String statusline = "";
			String restofheader = "\r\nServer: Sparkserver/1.0\r\nConnection: close\r\nContent-Type: text/html\r\n\r\n";
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String[] firstline = bufferedreader.readLine().split(" ");
			String pagename = "";
			try {
				pagename = firstline[1];
			}
			catch (ArrayIndexOutOfBoundsException e) {
				statusline = "HTTP/1.0 500 Internal Server Error";
			}
			String formid;
			if (firstline[0] == "POST") {
				formid = pagename.split(":")[1].split("/")[0];
			}
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
			}
			catch (ArrayIndexOutOfBoundsException g) {
				statusline = "HTTP/1.0 500 Internal Server Error";
				pagename = "errorpage";
			}
			bufferedreader.readLine();
			String[] formdata = bufferedreader.readLine().split("&");
			
			//String formdatum = URLDecoder.decode(bufferedreader.readLine(), "UTF-8");   ???
			
			BufferedReader secondreader = new BufferedReader(new FileReader(pagename + ".html"));
			
			BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			bufferedwriter.write(statusline + restofheader);
			String holder = "";
			while (holder != "</html>") {
				bufferedwriter.write(holder);
				holder = secondreader.readLine();
			}
			bufferedwriter.flush();
			socket.shutdownOutput();
		}
	}
	
	public static void main (String[] args){
		
	}
}
