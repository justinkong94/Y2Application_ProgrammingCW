import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;


public class RequestHandler implements IRequestHandler {
	
	//RequestHandler constructor
	public RequestHandler(){
		
	}
	
	//process the request from the client
	public byte[] processRequest(byte[] request){
		
		//call interpretRequest method
		String interpret = interpretRequest(request);
		byte[] outputByte = null;
		
		try{
			//convert String to byte[]
			outputByte = interpret.getBytes("US-ASCII");
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		
		return outputByte;
	}
	
	public String interpretRequest(byte[] request){
		String returnString = null;
		
		//list of regular expression 
		String patternGet = "^GET ((http://(.+))|(/)) ((HTTP/1.0)|(HTTP/0.9))?(\r\n\r\n)";
		String patternPost = "^POST ((http://(.+))|(/)) ((HTTP/1.0)|(HTTP/0.9))(\r\n\r\n)";
		String patternHead = "^HEAD ((http://(.+))|(/)) ((HTTP/1.0)|(HTTP/0.9))(\r\n\r\n)";
		
		//convert request from byte to string
		String input = new String(request); 
		
		//create the regular expressions
		Pattern getRequest = Pattern.compile(patternGet); 
		Pattern postRequest = Pattern.compile(patternPost);
		Pattern headRequest = Pattern.compile(patternHead);
		
		//compare the regular expressions with the request input
		if(getRequest.matcher(input).matches()){ 
			returnString = doGet(input);
		}
		else if(postRequest.matcher(input).matches()){ 
			returnString = doPost(input);
		}
		else if(headRequest.matcher(input).matches()){ 
			returnString = doHead(input);
		}
		else{
			returnString = doBadRequest(input);
		}
		
		return returnString;
	}
	
	//return get not implemented
	public String doGet(String data){
		return "HTTP/1.0 501 GET Not Implemented\r\n\r\n";
	}
	
	//return head not implemented
	public String doHead(String data){
		return "HTTP/1.0 501 HEAD Not Implemented\r\n\r\n";
	}
	
	//return post not implemented
	public String doPost(String data){
		return "HTTP/1.0 501 POST Not Implemented\r\n\r\n";
	}
	
	//return bad request
	public String doBadRequest(String data){
		return "HTTP/1.0 400 Bad Request\r\n\r\n";
	}

}
