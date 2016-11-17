import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FileServer implements IServe {
	
	private final String FORBIDDEN_403 = "HTTP/1.0 403 Forbidden\r\n\r\n";
	private final String OK_200 = "HTTP/1.0 200 OK ";
	
	//perform get request
	public byte[] httpGet(String requestURI) throws HTTPFileNotFoundException, HTTPRuntimeException,
	HTTPPermissionDeniedException{
		
		File file = new File(requestURI);
		byte[] returnByte = null;
		String returnString = "";
		int character = 0;
		
		//if file exists do this
		if (file.exists() && !file.isDirectory()) {
			
			try{
				//set status code to 200 OK and return it with the body of file
				returnString = OK_200;
				returnString += setFileExistHeaders(file);
				InputStreamReader isReader = new InputStreamReader(new FileInputStream(file));
				
				while((character = isReader.read()) != -1){
					returnString += (char)character;
				}
				
				isReader.close();
				
			} catch(IOException e){
				throw new HTTPPermissionDeniedException(FORBIDDEN_403);
			}
			
		} else {//file does not exist
			returnString = setNoFileHeaders();
			throw new HTTPFileNotFoundException(returnString);
		}

		returnByte = returnString.getBytes();
		
		return returnByte;
	}
	
	//perform conditional get request
	public byte[] httpGETconditional(String requestURI, Date ifModifiedSince) throws HTTPFileNotFoundException,
	HTTPRuntimeException, HTTPPermissionDeniedException{
		
		byte[] returnByte = null;
		String returnString = "";
		
		try{
			File file = new File(requestURI);
			
			//if file exists do this
			if (file.exists() && !file.isDirectory()) {
				returnString = compareDates(file,ifModifiedSince,requestURI);
			} else {//file does not exist, do this
				returnString = setNoFileHeaders();
				throw new HTTPFileNotFoundException(returnString);
			}
			
		} catch (ParseException e){
			throw new HTTPPermissionDeniedException(FORBIDDEN_403);
		}
		
		returnByte = returnString.getBytes();
		
		return returnByte;
	}
	
	//perfomm head request
	public byte[] httpHEAD(String requestURI) throws HTTPFileNotFoundException, HTTPRuntimeException,
	HTTPPermissionDeniedException{
		
		byte[] returnByte = null;
		String returnString = "";
		File file = new File(requestURI);
		
		//if file exists do this
		if (file.exists() && !file.isDirectory()) {
			
			try{
				returnString = OK_200;
				returnString += setFileExistHeaders(file);
			} catch(IOException e){
				throw new HTTPPermissionDeniedException(FORBIDDEN_403);
			}
		
		} else{//file does not exist, do this
			returnString = setNoFileHeaders();
			throw new HTTPFileNotFoundException(returnString);
		}
		
		returnByte = returnString.getBytes();
		
		return returnByte;
	}
	
	//perform post request
	public byte[] httpPOST(String requestURI, byte[] postData) throws HTTPFileNotFoundException, HTTPRuntimeException,
	HTTPPermissionDeniedException{
		
		byte[] returnByte = null;
		String returnString = "";
		File file = new File(requestURI);
		
		//if file exists do this
		if (file.exists() && !file.isDirectory()) {
			returnByte = httpGet(requestURI);
		} else{//if file does not exist do this
			try{
				PrintWriter writer = new PrintWriter(requestURI, "UTF-8");
				writer.println(new String(postData));
				writer.close();
				
				returnString = "HTTP/1.0 201 Created ";
				returnString += setFileExistHeaders(file);
				
			} catch(Exception e){
				throw new HTTPPermissionDeniedException(FORBIDDEN_403);
			}
			
			returnByte = returnString.getBytes();
		}
		
		return returnByte;
	}
	
	//set the response headers if the file exists
	private String setFileExistHeaders(File file) throws IOException{
		
		String finalOutput = "";
		String currentDate = "";
		String modifiedDate = "";
		DateFormat dateFormat = new SimpleDateFormat("E',' dd MMM yyyy hh:mm:ss zzz");
		Date date = new Date();
		
		modifiedDate = "\nLast-Modified: " + dateFormat.format(file.lastModified());
		currentDate += "\nDate: " + dateFormat.format(date);
		finalOutput += "\nContent-Length: " + file.length();
		finalOutput += "\nContent-Type: " + Files.probeContentType(file.toPath());  
		
		finalOutput += currentDate + modifiedDate + "\r\n\r\n";
		
		return finalOutput;
	}
	
	//set the response headers if the file does not exist
	private String setNoFileHeaders(){
		
		String finalOutput = "";
		DateFormat dateFormat = new SimpleDateFormat("E',' dd MMM yyyy hh:mm:ss zzz");
		Date date = new Date();
		
		finalOutput = "HTTP/1.0 404 Not Found ";
		finalOutput += "Date: " + dateFormat.format(date) + "\r\n\r\n";
		
		return finalOutput;
	}
	
	//compare the dates for the conditional get method
	private String compareDates(File file,Date ifModified,String requestURI)throws HTTPFileNotFoundException,
	HTTPRuntimeException, HTTPPermissionDeniedException,ParseException{
		
		String outputString = "";
		DateFormat dateFormat = new SimpleDateFormat("E',' dd MMM yyyy hh:mm:ss zzz");
		String dateString = dateFormat.format(file.lastModified());
		Date date = dateFormat.parse(dateString);
		
		//if the dates are similar do this
		if(ifModified.compareTo(date) == 0){
			
			try{	
				outputString = "HTTP/1.0 304 Not Modified ";
				outputString += setFileExistHeaders(file);
			} catch(IOException e){
				throw new HTTPPermissionDeniedException(FORBIDDEN_403);
			}

		}else{//if the dates are not similar do this
			outputString = new String (httpGet(requestURI));
		}
		
		return outputString;
	}
}
