import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class ThreadHandler implements Runnable{

	private Socket client;
	
	public ThreadHandler(Socket client){
		this.client = client;
	}
	
	public void run(){
	    String line;
	    String output;
	    byte[] response = null;
	    BufferedReader input = null;
	    PrintWriter out = null;
	    
	    try{
	    	//Gets the socket's input stream and opens a reader on it
	    	input = new BufferedReader(new InputStreamReader(client.getInputStream()));
	    	//Gets the socket's output stream and opens a writer on it
	    	out = new PrintWriter(client.getOutputStream(), true);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }

	    try{
	    	//reads the request from the socket's input stream
	        line = input.readLine();
	        //create new request handler object
	        RequestHandler requestHandler = new RequestHandler();
	        //call process request method from the request handler and process request
	        response = requestHandler.processRequest(convertString(line));
	        //convert response from byte[] to String
	        output = new String(response);
	        //print response
	        out.println(output);
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
}
