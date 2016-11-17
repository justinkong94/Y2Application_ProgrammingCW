import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class FileTransactionRegister implements ILogger {

	private String registerType;
	private String registerLocation;
	private String fileURI;
	private Boolean isArgumentsCorrect = false;
	
	public FileTransactionRegister(){
		
	}
	
	public void setArguments(String info){
		
		String[] stringList = info.split(" ");
		this.registerType = stringList[0];
		this.registerLocation = stringList[1];
	}
	
	public void checkArguments(){
		
		File file = new File(registerLocation);
		
		if(file.exists()){

			DateFormat dateFormat = new SimpleDateFormat("E',' dd MMM yyyy hh:mm:ss:SSS zzz");
			Date currentDate = new Date();
			
			switch(registerType){
			case "-r":
				
				try{
					FileWriter fileWriter = new FileWriter(file,true);
					PrintWriter pw = new PrintWriter(fileWriter);
					pw.println(dateFormat.format(currentDate) + ", $, starting registering");
					pw.close();
					
					isArgumentsCorrect = true;
				} catch(IOException e){
					
				}
				
				break;
			case "-R":
				
				try{
					FileWriter fileWriter = new FileWriter(file,false);
					PrintWriter pw = new PrintWriter(fileWriter);
					pw.println(dateFormat.format(currentDate) + ", $, starting registering");
					pw.close();
					
					isArgumentsCorrect = true;
				} catch(IOException e){
					
				}
				
				break;
			default:
				System.out.println("Command not recognised.");
				System.exit(-1);
				break;
			}
			
		} else{
			System.out.println("Register file not found.");
		}
	}
	
	public void writeRequestDetails(String fullRequest){
		
		String patternURI = "((/)(.)*) ";
		String[] requestLines;
		
		DateFormat dateFormat = new SimpleDateFormat("E',' dd MMM yyyy hh:mm:ss:SSS zzz");
		Date requestDate = new Date();
		
		if(isArgumentsCorrect){
			
			try{
				FileWriter fileWriter = new FileWriter(registerLocation,true);
				PrintWriter pw = new PrintWriter(fileWriter);
				pw.print(dateFormat.format(requestDate) + ", >, ");
				
				requestLines = fullRequest.split("\n");
				
				Pattern uriPattern = Pattern.compile(patternURI);
				Matcher uriMatch = uriPattern.matcher(requestLines[0]);
				
				while(uriMatch.find()) {
					fileURI = uriMatch.group();
				}
				
				fileURI = fileURI.substring(1);
				
				pw.println(fileURI + ", " + requestLines[0]);
				pw.close();
				
			} catch(IOException e){
				
			}
		}
	}
	
	public void writeResponseDetails(String fullResponse){
		
		String[] responseLines;
		
		DateFormat dateFormat = new SimpleDateFormat("E',' dd MMM yyyy hh:mm:ss:SSS zzz");
		Date responseDate = new Date();
		
		if(isArgumentsCorrect){
			
			try{
				FileWriter fileWriter = new FileWriter(registerLocation,true);
				PrintWriter pw = new PrintWriter(fileWriter);
				pw.print(dateFormat.format(responseDate) + ", <, " + fileURI + ", ");
				
				responseLines = fullResponse.split("\n");
				
				pw.println(responseLines[0]);
				pw.close();
				
			}catch(IOException e){
				
			}
		}
	}	
}
