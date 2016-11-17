import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class G52APRServer {

	public static void main(String[] args) {
		
		String fileTRInfo = "";
		FileTransactionRegister fileTR = new FileTransactionRegister();
		
		//get port number
		int portNumber = Integer.parseInt(args[0]);
		String rootURL = args[1];
		
		if(args.length > 3){
			fileTRInfo = args[2] + " " + args[3];
			fileTR.setArguments(fileTRInfo);
			fileTR.checkArguments();
		}
		
		Socket clientSocket = null;
		//a ServerSocket object is created
		ServerSocket serverSocket = null;
		
		try{
			//set ServerSocket object to listen to a specific port 
			serverSocket = new ServerSocket(portNumber);
			
			while(true){
				//waits until a client starts up and requests a connection on the host and port of this server
				clientSocket = serverSocket.accept();
				ThreadHandler threadHandler = new ThreadHandler(clientSocket,rootURL,fileTR);
				Thread trd = new Thread(threadHandler);
				trd.start();
			}
			
		} catch(IOException e){
			e.printStackTrace();
		} finally{
			try{
			    serverSocket.close();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
