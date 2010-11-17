import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			URL url = new URL("https://login.live.com/RST.srf");
			
			HttpsURLConnection huc = (HttpsURLConnection) url.openConnection();
			huc.setDoInput(true);
			huc.setDoOutput(true);
			byte[] request = getXML("taoliang1985@126.com","taolovesun");
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

	        while ((sCurrentLine = br.readLine()) != null) {  
	            sTotalString += sCurrentLine + "\r\n";  
	  
	        }  
	        System.out.println(sTotalString);  
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static byte[] getXML(String userString,String pasString){
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>+" +
				"<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsse=\"http://schemas.xmlsoap.org/ws/2003/06/secext\" xmlns:saml=\"urn:oasis:names:tc:SAML:1.0:assertion\" xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2002/12/policy\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/03/addressing\" xmlns:wssc=\"http://schemas.xmlsoap.org/ws/2004/04/sc\" xmlns:wst=\"http://schemas.xmlsoap.org/ws/2004/04/trust\">" +
				"<Header>" +
				"<ps:AuthInfo xmlns:ps=\"http://schemas.microsoft.com/Passport/SoapServices/PPCRL\" Id=\"PPAuthInfo\">" +
				"<ps:HostingApp>{7108E71A-9926-4FCB-BCC9-9A9D3F32E423}</ps:HostingApp>" +
				"<ps:BinaryVersion>4</ps:BinaryVersion>" +
				"<ps:UIVersion>1</ps:UIVersion>" +
				"<ps:Cookies />" +
				"<ps:RequestParams>AQAAAAIAAABsYwQAAAAyMDUy</ps:RequestParams>" +
				"</ps:AuthInfo>" +
				"<wsse:Security>" +
				"<wsse:UsernameToken Id=\"user\">" +
				"<wsse:Username>" + userString + "</wsse:Username>" +
				"<wsse:Password>" + pasString + "</wsse:Password>" +
				"</wsse:UsernameToken>" +
				"</wsse:Security>" +
				"</Header>" +
				"<Body>" +
				"<ps:RequestMultipleSecurityTokens xmlns:ps=\"http://schemas.microsoft.com/Passport/SoapServices/PPCRL\" Id=\"RSTS\">" +
				"<wst:RequestSecurityToken Id=\"RST0\">" +
				"<wst:RequestType>http://schemas.xmlsoap.org/ws/2004/04/security/trust/Issue</wst:RequestType>" +
				"<wsp:AppliesTo>" +
				"<wsa:EndpointReference>" +
				"<wsa:Address>http://Passport.NET/tb</wsa:Address>" +
				"</wsa:EndpointReference>" +
				"</wsp:AppliesTo>" +
				"</wst:RequestSecurityToken>" +
				"<wst:RequestSecurityToken Id=\"RST1\">" +
				"<wst:RequestType>http://schemas.xmlsoap.org/ws/2004/04/security/trust/Issue</wst:RequestType>" +
				"<wsp:AppliesTo>" +
				"<wsa:EndpointReference>" +
				"<wsa:Address>messengerclear.live.com</wsa:Address>" +
				"</wsa:EndpointReference>" +
				"</wsp:AppliesTo>" +
				"<wsse:PolicyReference URI=\"MBI_KEY_OLD\" />" +
				"</wst:RequestSecurityToken>" +
				"<wst:RequestSecurityToken Id=\"RST2\">" +
				"<wst:RequestType>http://schemas.xmlsoap.org/ws/2004/04/security/trust/Issue</wst:RequestType>" +
				"<wsp:AppliesTo>" +
				"<wsa:EndpointReference>" +
				"<wsa:Address>messenger.msn.com</wsa:Address>" +
				"</wsa:EndpointReference>" +
				"</wsp:AppliesTo>" +
				"<wsse:PolicyReference URI=\"?id=507\" />" +
				"</wst:RequestSecurityToken>" +
				"<wst:RequestSecurityToken Id=\"RST3\">" +
				"<wst:RequestType>http://schemas.xmlsoap.org/ws/2004/04/security/trust/Issue</wst:RequestType>" +
				"<wsp:AppliesTo>" +
				"<wsa:EndpointReference>" +
				"<wsa:Address>contacts.msn.com</wsa:Address>" +
				"</wsa:EndpointReference>" +
				"</wsp:AppliesTo>" +
				"<wsse:PolicyReference URI=\"MBI\" />" +
				"</wst:RequestSecurityToken>" +
				"<wst:RequestSecurityToken Id=\"RST4\">" +
				"<wst:RequestType>http://schemas.xmlsoap.org/ws/2004/04/security/trust/Issue</wst:RequestType>" +
				"<wsp:AppliesTo>" +
				"<wsa:EndpointReference>" +
				"<wsa:Address>messengersecure.live.com</wsa:Address>" +
				"</wsa:EndpointReference>" +
				"</wsp:AppliesTo>" +
				"<wsse:PolicyReference URI=\"MBI_SSL\" />" +
				"</wst:RequestSecurityToken>" +
				"<wst:RequestSecurityToken Id=\"RST5\">" +
				"<wst:RequestType>http://schemas.xmlsoap.org/ws/2004/04/security/trust/Issue</wst:RequestType>" +
				"<wsp:AppliesTo>" +
				"<wsa:EndpointReference>" +
				"<wsa:Address>spaces.live.com</wsa:Address>" +
				"</wsa:EndpointReference>" +
				"</wsp:AppliesTo>" +
				"<wsse:PolicyReference URI=\"MBI\" />" +
				"</wst:RequestSecurityToken>" +
				"<wst:RequestSecurityToken Id=\"RST6\">" +
				"<wst:RequestType>http://schemas.xmlsoap.org/ws/2004/04/security/trust/Issue</wst:RequestType>" +
				"<wsp:AppliesTo>" +
				"<wsa:EndpointReference>" +
				"<wsa:Address>storage.msn.com</wsa:Address>" +
				"</wsa:EndpointReference>" +
				"</wsp:AppliesTo>" +
				"<wsse:PolicyReference URI=\"MBI\" />" +
				"</wst:RequestSecurityToken>" +
				"</ps:RequestMultipleSecurityTokens>" +
				"</Body>" +
				"</Envelope>";
		
		return xmlString.getBytes();
	}

}
