import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class RequestHandler implements IRequestHandler {
	
	private String rootURL = "";
	
	//list of regular expressions
	private final String PATTERN_GET = "^GET ((http://(.+))|(/)(.)*) ((HTTP/1.0)|(HTTP/0.9))?(\r\n\r\n)?";
	private final String PATTERN_HEAD = "^HEAD ((http://(.+))|(/)(.)*) ((HTTP/1.0)|(HTTP/0.9))(\r\n\r\n)?";
	private final String PATTERN_POST = "^POST ((http://(.+))|(/)(.)*) ((HTTP/1.0)|(HTTP/0.9))(\r\n\r\n)?";
	private final String PATTERN_URI = "((http://)(.+)|(/)(.)*) ";//requires blank space at the end
	
	//RequestHandler constructor
	public RequestHandler(){
		
	}
	
	public void setRootURL(String rootURL){
		this.rootURL = rootURL;
	}
	
	//process the request from the client
	public byte[] processRequest(byte[] request){
		
		//convert byte[] to String
		String requestString = new String(request);
		//call interpretRequest method
		String interpret = interpretRequest(requestString);
		byte[] outputByte = null;
		
		try{
			//convert String to byte[]
			outputByte = interpret.getBytes("US-ASCII");
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		
		return outputByte;
	}
	
	public String interpretRequest(String request){
		String postBodyData = "";
		String returnString = null;
		String finalUriString = "";
		String ifModifiedDate = "";
		String oneLine = "";
		String[] lines = null;
		
        lines = countNewLines(request);
		finalUriString = setURL(lines);
		
		//create the regular expressions
		Pattern getRequest = Pattern.compile(PATTERN_GET); 
		Pattern postRequest = Pattern.compile(PATTERN_POST);
		Pattern headRequest = Pattern.compile(PATTERN_HEAD);
		
		//compare the regular expressions with the request input and check for get conditional
		if(request.contains("If-Modified-Since:")){
			ifModifiedDate = request.substring(request.indexOf("If-Modified-Since:") + 21,request.indexOf("GMT") + 3);
			
			try{
				Date date = new SimpleDateFormat("E',' dd MMM yyyy hh:mm:ss zzz").parse(ifModifiedDate);
				returnString = doConditionalGet(finalUriString,date);
				
			} catch(ParseException e){
				returnString = "HTTP/1.0 500 Internal Error \r\n\r\n";
			}
		}
		else if(getRequest.matcher(lines[0]).matches()){ 
			returnString = doGet(finalUriString);
		}
		else if(postRequest.matcher(lines[0]).matches()){ 
			
			if(!request.contains("(body)")){
				doBadRequest(finalUriString);
			} else{
			
				for(int i=0; i<lines.length;i++){
					oneLine = lines[i];
					if(oneLine.contains("(body)")){
						postBodyData = setBodyString(lines,i+1);
					}
				}
				
				returnString = doPost(finalUriString,postBodyData);
			}
		}
		else if(headRequest.matcher(lines[0]).matches()){ 
			returnString = doHead(finalUriString);
		}
		else{
			returnString = doBadRequest(finalUriString);
		}
		
		return returnString;
	}
	
	//return get value
	private String doGet(String uriLink){
		
		FileServer fileSvr = new FileServer();
		byte[] byteResponse = null;
		String stringResponse = "";
		
		try{
			byteResponse = fileSvr.httpGet(uriLink);
			stringResponse = new String(byteResponse);
		} catch(HTTPFileNotFoundException e){
			stringResponse = e.getMessage();
		} catch(HTTPRuntimeException e){
			stringResponse = e.getMessage();
		} catch(HTTPPermissionDeniedException e){
			stringResponse = e.getMessage();
		}

		return stringResponse;
	}
	
	//return conditional get value
	private String doConditionalGet(String uriLink,Date date){
		
		FileServer fileSvr = new FileServer();
		byte[] byteResponse = null;
		String stringResponse = "";
		
		try{
			byteResponse = fileSvr.httpGETconditional(uriLink,date);
			stringResponse = new String(byteResponse);
		} catch(HTTPFileNotFoundException e){
			stringResponse = e.getMessage();
		} catch(HTTPRuntimeException e){
			stringResponse = e.getMessage();
		} catch(HTTPPermissionDeniedException e){
			stringResponse = e.getMessage();
		}

		return stringResponse;
	}
	
	//return head value
	private String doHead(String uriLink){
		
		FileServer fileSvr = new FileServer();
		byte[] byteResponse = null;
		String stringResponse = "";
		
		try{
			byteResponse = fileSvr.httpHEAD(uriLink);
			stringResponse = new String(byteResponse);
		} catch(HTTPFileNotFoundException e){
			stringResponse = e.getMessage();
		} catch(HTTPRuntimeException e){
			stringResponse = e.getMessage();
		} catch(HTTPPermissionDeniedException e){
			stringResponse = e.getMessage();
		}

		return stringResponse;
	}
	
	//return post value
	private String doPost(String uriLink, String postBody){
		
		FileServer fileSvr = new FileServer();
		byte[] byteResponse = null;
		String stringResponse = "";
		
		try{
			byteResponse = fileSvr.httpPOST(uriLink,postBody.getBytes());
			stringResponse = new String(byteResponse);
		} catch(HTTPFileNotFoundException e){
			stringResponse = e.getMessage();
		} catch(HTTPRuntimeException e){
			stringResponse = e.getMessage();
		} catch(HTTPPermissionDeniedException e){
			stringResponse = e.getMessage();
		}
		
		return stringResponse;
	}
	
	//return bad request
	private String doBadRequest(String data){
		return "HTTP/1.0 400 Bad Request\r\n\r\n";
	}
	
	//concatenate the Strings in the string array into one string
	private String setBodyString(String[] lines, int number){
		
		String body = "";
		
		for(int i=number;i<lines.length;i++){
			body += lines[i] + "\n";
		}
		
		return body;
	}
	
	//count number of new line characters in string
	private String[] countNewLines(String request){
		
		int numberOfNewLineChar = 0;
		String[] stringArray;
		
		Pattern newLinePattern = Pattern.compile("\n");
	    Matcher newLineMatch = newLinePattern.matcher(request);
	        
	    while (newLineMatch.find()) {
	    	numberOfNewLineChar++;
	    }
			
	    if(numberOfNewLineChar > 2){ 
	    	stringArray = request.split("\n");
	    } else{
	    	stringArray = new String[1];
	    	stringArray[0] = request;
	    }
	    
	    return stringArray;
	}
	
	//set the final url
	private String setURL(String[] lines){
		
		String fileUriString = "";
		String uriString = "";
		
		Pattern uriPattern = Pattern.compile(PATTERN_URI);
		Matcher uriMatch = uriPattern.matcher(lines[0]);
		
		while(uriMatch.find()) {
			fileUriString += uriMatch.group();
		}
		
		//get server fixed uri
		uriString = rootURL;
		
		if(fileUriString.length() > 1){
			//remove first backslash from file uri read from the client
			fileUriString = fileUriString.substring(1);	
			//concatenate the file uri from the client with the server to form complete uri
			uriString += fileUriString;
		}
		
		//remove all whitespace characters in file URI
		uriString = uriString.replaceAll("\\s+$", "");
		
		return uriString;
	}
}
