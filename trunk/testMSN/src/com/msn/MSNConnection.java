package com.msn;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

import org.omg.IOP.Encoding;

import com.parse.ParseURL;
import com.parse.parseXML;

public class MSNConnection {
	
	// These are the numbers where the addresses wil appear in the arraylist
	// it is not the niced solution, but it works a lot faster
	// you can use some kind of IDictionary object to put the value behind a key name
	//
	// PASPORTURLS
	public int DARREALM = 0;
	public int DALOGIN = 1;
	public int DAREG = 2;
	public int PROPERTIES = 3;
	public int GENERALDIR = 4;
	public int HELP = 5;
	public int CONFIGVERSION = 6;
	
	public ArrayList PassportUrls;
	
	/// <summary>
	/// Every message from a client has a unique transactionID
	/// With every message this number will be increased
	/// </summary>
	private long            _transactionID = 0;

	private Socket             _socket;
	private InputStream        _stream;
	private BufferedReader     _reader;
	private PrintWriter        _writer;
	private DataOutputStream   _doc;

	public String _UserName;
	public String _ScreenName;
	
//	protected boolean DataAvailable()
//	{
//		return _stream.DataAvailable;
//	}
//
//	public bool Connected
//	{
//		get { return _socket != null; }
//	}
	
	protected void ConnectSocket(String host,int port){
		System.out.println("Connecting to the host :       " + host);
		
		_transactionID = 0;
		try {
			_socket = new Socket(host,port);

			_stream = _socket.getInputStream();
			
			_reader = new BufferedReader(new InputStreamReader(_stream));
			
			_writer = new PrintWriter(_socket.getOutputStream());
			_doc = new DataOutputStream(_socket.getOutputStream());
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/// <summary>
	/// Write a message to the current socket, this functions raises the transactionID
	/// </summary>
	/// <param name="line">The Message</param>
	/// <param name="writeNewLine">Determine if you write a line, or only a message (with or withhout ending character</param>
	private void WriteLine(String line,boolean writeNewLine) throws IOException{
		System.out.println("Writing                :       " + line);
		
		if(writeNewLine){
			_writer.println(line);
			_writer.flush();
//			_doc.writeUTF(line);
			//_doc.close();
		}else{
			_writer.print(line);
			_writer.flush();
			_transactionID ++;
		}
	}
	
	/// <summary>
	/// Write a command to the server
	/// </summary>
	/// <param name="command">The first command</param>
	/// <param name="parameters">The parameters that have to be send to the server</param>
	/// <param name="bSendId">Sometimes you don't have to send an transactionID</param>
	protected void WriteCommand(String command,String parameters,boolean bSendId) throws IOException {
		String line;

		if(bSendId){

			line = command +  parameters;

		}else{
			line = String.format("{0},{1}", command,parameters);
		}
		WriteLine(line, true);
	}
	
	/// <summary>
	/// This function read the information from the streamreader
	/// and if it read something it returns a new ServerCommand object
	/// </summary>
	/// <returns>if there is something return new ServerCommand object</returns>
	protected ServerCommand readCommand(){
		try{
			
			String line = "";
			line = _reader.readLine();
//			
			
			System.out.println("Response received!             the length:" + line.length());
			System.out.println("Reading from server    :       " + line);
			if(line == null){
				System.out.println("nothing readed form server!");
				return new ServerCommand();
			}else{
				return new ServerCommand(line);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	protected String readCommand2(){
		try{
			
			String line = "";
			line = _reader.readLine();
//			
			
			System.out.println("Response received!             the length:" + line.length());
			System.out.println("Reading from server    :       " + line);
			if(line == null){
				System.out.println("nothing readed form server!");
				return "";
			}else{
				return line;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	/// <summary>
	/// This function closes the connection, if its open
	/// </summary>
	public void Dispose()
	{
		if( _socket != null )
		{
			try {
				_reader.close();
				_writer.close();
				_stream.close();
				_socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			_socket = null;
		}
	}
	
	public int connect(String userName,String password) {
		String host = "messenger.hotmail.com";
		int port = 1863;
		String challengeString;
		ServerCommand serCom = null;
		
		try{
			while(true){
				ConnectSocket(host, port);
				
				WriteCommand("VER 1 ", "MSNP21 CVR0", true);
				
				serCom = readCommand();
				
				if(!serCom.CommandName().equals("VER")){
		
					return 1;
				}
				
				WriteCommand("CVR 2 ", "0x0804 winnt 6.1.0 i386 MSNMSGR 15.4.3502.0922 MSNMSGR " + userName + " 0", true);

				//WriteCommand("USR 3 ", "SSO I " + userName, true);
				
				serCom = readCommand();

				if(!serCom.CommandName().equals("CVR")){
					return 1;
				}
				

				WriteCommand("USR 3 ",
						"SSO I "
								+ userName, true);
				
				serCom = readCommand();
				
				if(serCom.CommandName().equals("USR")){
					challengeString = serCom.Param(3);
					break;
				}else if(serCom.CommandName().equals("GCF")){
					try {
						URL url = new URL("https://login.live.com/RST.srf");
						
						HttpsURLConnection huc = (HttpsURLConnection) url.openConnection();
						huc.setDoInput(true);
						huc.setDoOutput(true);
						byte[] request = ParseURL.getXML("taoliang1985@126.com","taolovesun");
						//huc.setRequestMethod("post");
						OutputStream oStream = huc.getOutputStream();
						oStream.write(request);
						oStream.flush();
						
						int responseCode = huc.getResponseCode();
						String responseMessageString = huc.getResponseMessage();
						
						BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
						
						String sCurrentLine;  
				        String sTotalString;  
				        sCurrentLine = "";  
				        sTotalString = "";  
				        InputStream l_urlStream;  

//				        while ((sCurrentLine = br.readLine()) != null) {  
//				            sTotalString += sCurrentLine + "\r\n";  
//				  
//				        }  
//				       // System.out.println(sTotalString);  
				        
				        challengeString = parseXML.parsexml(huc.getInputStream());
						
				        break;
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				if(!serCom.CommandName().equals("XFR")){
					return 1;
				}else{
					System.out.println("Redirected to another Server!");
				}
				
				String[] arIP = serCom.Param(2).split(":");
				host = arIP[0];
				port = Integer.parseInt(arIP[1]);
				
				Dispose();
			}
			
			//String clientticketString = GetClientTicket(password,userName ,challengeString);
			
			
			if(challengeString == "401"){
				return 401;
			}else if(challengeString == "0"){
				return 1;
			}else{
				
//				WriteCommand("USR 3 ",
//						"SSO I "
//								+ userName, true);

				serCom = readCommand();
				
				WriteCommand("USR 4 ", "SSO S "+challengeString, true);
				serCom = readCommand();
				
				if(!serCom.CommandName().equals("USR") && !serCom.Param(1).equals("OK")){
					return 1;
				}
				
				_UserName = serCom.Param(2);
				_ScreenName = serCom.Param(3);
				
				WriteCommand("CHG", "NLN", true);
				
				return 0;
			}
		}catch(Exception e){
			return 1;
		}
		
	}
	
	/// <summary>
	/// This function asks a valid login adres, to connect to
	/// </summary>
	/// <returns>true if succeed</returns>
	public boolean GetLoginServerAddres() throws IOException
	{		
		// Make a request to the server, this adresses are being used in the MSN messenger
		URL url = new URL("https://nexus.passport.com/rdr/pprdr.asp");
		HttpURLConnection huc = (HttpURLConnection) url.openConnection();
		//huc.setRequestMethod("get");
		//huc.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
		
		System.out.println("httpUrlConnection Started!");
		//HttpWebRequest ServerRequest = (HttpWebRequest)WebRequest.Create("https://nexus.passport.com/rdr/pprdr.asp");
		// Get the result
		//HttpWebResponse ServerResponse = (HttpWebResponse)ServerRequest.GetResponse();
		if (huc.getResponseCode() == HttpURLConnection.HTTP_OK)
		{
			String retrieveddata = huc.getHeaderField(0).toString();
			PassportUrls = new ArrayList();
			// Pick up the header en split
			String[] result = huc.getHeaderField("PassportURLs").toString().split(",");

			for(int i = 0 ;i<result.length;i++){
				String s = result[i];
				PassportUrls.add(s.substring(s.indexOf('=') + 1));
			}
			huc.disconnect();
			return true;
		}
		else
		{
			huc.disconnect();
			return false;
		}
	}
	
	/// <summary>
	/// This function connects to a login server to request a valid ticket,
	/// that will be used to login on the MSN servers
	/// </summary>
	/// <param name="Password">The password of the user, this is just plain text. The connection is HTTPS
	/// <param name="Username">The complete username</param>
	/// <param name="ChallengeString">A challenge string that you have got, wile connecting to a msn server
	/// <returns>a valid ticket, that you send back to the server to get connected</returns>
	public String GetClientTicket(String Password, String Username, String ChallengeString) throws IOException
	{

		// First get a valid login adres for the initial server
		if (GetLoginServerAddres())
		{
			// On the position of DALOGIN is a valid URL, for login
			String uri = "https://" + PassportUrls.get(DALOGIN);
			
			URL url = new URL(uri);
			HttpURLConnection ServerRequest;
//			 ServerResponse;
			try
			{
				while( true )
				{
			
					System.out.println("Connecting to          :        " + uri);
					// Make a new request
					ServerRequest = (HttpURLConnection) url.openConnection();
					ServerRequest.setInstanceFollowRedirects(false);
					
					//ServerRequest.KeepAlive = false;
					//ServerRequest.ProtocolVersion = new Version(1,0);
					// Send the authentication header
					ServerRequest.addRequestProperty("Authorization", "Passport1.4 OrgVerb=GET,OrgURL=http%3A%2F%2Fmessenger%2Emsn%2Ecom,sign-in=" + Username.replace("@", "%40") + ",pwd=" + Password + "," + ChallengeString + "\n");
					// Pick up the result
//					ServerResponse = (HttpWebResponse)ServerRequest.GetResponse();

					// If the statuscode is OK, then there is a valid return
					if (ServerRequest.getResponseCode() == HttpURLConnection.HTTP_OK)
					{
						// Pick up the information of the authentications
						String AuthenticationInfo = ServerRequest.getHeaderField("Authentication-Info");
						// Get the startposition of the ticket id (note it is between two quotes)
						int startposition = AuthenticationInfo.indexOf('\'');
						// Get the endposition 
						int endposition = AuthenticationInfo.lastIndexOf('\'');
						// Substract the startposition of the endposition
						endposition = endposition - startposition ;

						// Close connection
						//ServerResponse.Close();

						// Generate a new substring and return it
						return AuthenticationInfo.substring(startposition + 1, endposition -1 );
				
					}
						// If the statuscode is 302, then there is a redirect, read the new adres en connect again
					else
					{
						uri = ServerRequest.getHeaderField("Location");
					}
				}
			}
			catch (Exception e)
			{
				// If the statuscode is 401, then an exeption occurs
				// Think that your password + username combination is not correct
				// return number so that calling functions knows what to do
//				if (e.getMessage() == WebExceptionStatus.ProtocolError)
//				{
//					return "401";
//				}
//				else
//				{
//					return "0";
//				}
				e.printStackTrace();
			}
		}
		return "0";
	}
}
