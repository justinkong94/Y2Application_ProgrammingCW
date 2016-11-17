import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class G52APRServer {

	public static void main(String[] args) {
		
		//get port number
		int portNumber = Integer.parseInt(args[0]);
		Socket clientSocket = null;
		//a ServerSocket object is created
		ServerSocket serverSocket = null;
		
		try{
			//set ServerSocket object to listen to a specific port 
			serverSocket = new ServerSocket(portNumber);
			
			while(true){
				//waits until a client starts up and requests a connection on the host and port of this server
				clientSocket = serverSocket.accept();
				ThreadHandler threadHandler = new ThreadHandler(clientSocket);
				Thread trd = new Thread(threadHandler);
				trd.start();
			}
			
		} catch(IOException e){
			e.printStackTrace();
		} 
		finally{
			try{
			    serverSocket.close();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
