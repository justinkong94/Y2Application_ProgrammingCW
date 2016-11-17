import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThreadHandler implements Runnable{

	private Socket client;
	private String rootURL;
	private FileTransactionRegister fileTR;
	
	public ThreadHandler(Socket client,String rootURL,FileTransactionRegister fileTR){
		this.client = client;
		this.rootURL = rootURL;
		this.fileTR = fileTR;
	}
	
	public void run(){
		
		String fullString = "";
	    String line = "";
	    String output = "";
	    byte[] response = null;
	    BufferedReader input = null;
	    PrintWriter out = null;
	    
	    try{
	    	
	    	//Gets the socket's input stream and opens a reader on it
	    	input = new BufferedReader(new InputStreamReader(client.getInputStream())); 
	    	//Gets the socket's output stream and opens a writer on it
	    	out = new PrintWriter(client.getOutputStream(), true);
	    	
	    	//read all headers
	    	while(!(line = input.readLine()).isEmpty()){
	    		fullString += line + "\n";
	    	}
	    	
	    	fileTR.writeRequestDetails(fullString);
	    	
	    	//read body if it exists and content-length is specified in the headers
	    	if(fullString.contains("Content-Length:")){
	    		fullString = getPostBody(fullString,input);
	    	}	    	
	    	
	        //create new request handler object
	        RequestHandler requestHandler = new RequestHandler();
	        requestHandler.setRootURL(rootURL);
	        //call process request method from the request handler and process request
	        response = requestHandler.processRequest(convertString(fullString));
	        //convert response from byte[] to String
	        output = new String(response);
	        
	        fileTR.writeResponseDetails(output);
	        
	        //print response
	        out.print(output); 
	        out.close();
	        client.close();
	        
	    }catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	//convert String to byte[]
	public byte[] convertString(String input){
		
		byte[] output = null;
		
		 try{
				output = input.getBytes("US-ASCII");
			} catch(UnsupportedEncodingException e){
				e.printStackTrace();
			}
		 
		 return output;
	}
	
	//get the body of the request for post
	private String getPostBody(String fullString,BufferedReader input){
		
		String finalString = "";
		String line ="";
    		
    	Pattern contentPattern = Pattern.compile("Content-Length: (\\d)*");
		Matcher contentMatch = contentPattern.matcher(fullString);
			
		while(contentMatch.find()) {
			line = contentMatch.group();
		}
			
		line = line.replaceAll("\\D+","");
		finalString = fullString + "(body)\n";
			
		try{
			for(int i = 0; i < Integer.parseInt(line); i++){
				finalString += (char)input.read();
			}
		} catch(IOException e){
			finalString = "IOException Error";
			e.printStackTrace();
		}
    	
		return finalString;
	}
}
