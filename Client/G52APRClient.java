import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


/* You will need to use this class to build your G52APRClient.
 * It needs to implement the interface IG52APRClient and so it
 * will need to have the methods for httpGet, httpHead and httpPost
 */     

public class G52APRClient implements IG52APRClient{
	
	// Constructor to make G52APRClient object.
	public G52APRClient() {
		
	}
		
	public static void main(String[] args){
		
		//url for client to connect to
		String url = "http://localhost:4444";
		//create new client object
		G52APRClient client = new G52APRClient();
		
		//create test array list for post method
		ArrayList<NameValuePair> postArray = client.createArrayList();
		
		//sending various method requests
		System.out.println(client.httpGet(url));
		System.out.println(client.httpHead(url));
		System.out.println(client.httpPost(url,"hello"));
		System.out.println(client.httpPost(url,postArray));
	}
	
	//get method request
	public String httpGet(String url){
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet getRequest = new HttpGet(url);
		
		//set protocol version to HTTP 1.0
		getRequest.setProtocolVersion(HttpVersion.HTTP_1_0);
		
		//return response String
		String returnOutput = null;
		
		try{
			//send request to server and get response
			CloseableHttpResponse response = httpclient.execute(getRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			
			//check if response is working, else return status line
			if(statusCode != HttpStatus.SC_OK){
				return response.getStatusLine().toString();
			}
			
			//get headers of the response
			returnOutput = getHeaders(response);
			
			HttpEntity entity = response.getEntity();
			returnOutput += EntityUtils.toString(entity);
			EntityUtils.consume(entity);
			
		} catch(IOException e){
			e.printStackTrace();
		} finally {
			try{
				httpclient.close();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
		
		return returnOutput;
	}
	
	//head method request
	public String httpHead(String url){
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpHead getRequest = new HttpHead(url);
		
		//set protocol version to HTTP 1.0
		getRequest.setProtocolVersion(HttpVersion.HTTP_1_0);
		
		//return response String
		String returnOutput = null;
		
		try{
			//send request to server and get response
			CloseableHttpResponse response = httpclient.execute(getRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			
			//check if response is working, else return status line
			if(statusCode != HttpStatus.SC_OK){
				return response.getStatusLine().toString();
			}
			
			//get headers from response
			returnOutput = getHeaders(response);
		} catch(IOException e){
			e.printStackTrace();
		} finally {
			try{
				httpclient.close();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
		
		return returnOutput;
	}
	
	//post method request with String input
	public String httpPost(String url, String body){
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost getRequest = new HttpPost(url);
		
		//set protocol version to HTTP 1.0
		getRequest.setProtocolVersion(HttpVersion.HTTP_1_0);
		
		//return response String
		String returnOutput = null;
		
		try{
			StringEntity xmlEntity = new StringEntity(body);
			getRequest.setEntity(xmlEntity);
			
			//send request to server and get response
			CloseableHttpResponse response = httpclient.execute(getRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			
			//check if response is working, else return status line
			if(statusCode != HttpStatus.SC_OK){
				return response.getStatusLine().toString();
			}
			
			HttpEntity entity = response.getEntity();
			returnOutput = EntityUtils.toString(entity);
			
			
		} catch(IOException e){
			e.printStackTrace();
		} finally {
			try{
				httpclient.close();
			} catch(IOException e){
				e.printStackTrace();			
			}
		}
		
		return returnOutput;
	}
	
	//post method request with ArrayList input
	public String httpPost(String url, ArrayList<NameValuePair> nvps){
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost getRequest = new HttpPost(url);
		
		//set protocol version to HTTP 1.0
		getRequest.setProtocolVersion(HttpVersion.HTTP_1_0);
		//return response String
		String returnOutput = null;
		
		try{
			getRequest.setEntity(new UrlEncodedFormEntity(nvps));
			
			//send request to server and get response
			CloseableHttpResponse response = httpclient.execute(getRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			
			//check if response is working, else return status line
			if(statusCode != HttpStatus.SC_OK){
				return response.getStatusLine().toString();
			}
			
			HttpEntity entity = response.getEntity();
			returnOutput = EntityUtils.toString(entity);
			
		} catch(IOException e){
			e.printStackTrace();
		} finally {
			try{
				httpclient.close();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
		
		return returnOutput;
	}
	
	//method for getting headers of a response
	public String getHeaders(CloseableHttpResponse response){
		
		String output = null;
		Header[] headers = response.getAllHeaders();
		
		for(int i=0; i < headers.length; i++){
			output += headers[i];
			output += "\n";
		}
		
		return output;
	}
	
	//method for creating ArrayLists
	public ArrayList<NameValuePair> createArrayList(){
		
		ArrayList<NameValuePair> createArray = new ArrayList <NameValuePair>();
		createArray.add(new BasicNameValuePair("green","world"));
		createArray.add(new BasicNameValuePair("lemon","tree"));
		
		return createArray;
	}

}
