
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class FileServerTests {

	private static final String OK_200_1_0 = "HTTP/1.0 200 OK ";
	private static final String NOT_FOUND_1_0 = "HTTP/1.0 404 Not Found ";
	private static final String CREATED_1_0 = "HTTP/1.0 201 Created ";
	private static final String BAD_REQUEST_1_0 = "HTTP/1.0 400 Bad Request\r\n\r\n";
	private static final String NOT_MODIFIED_1_0 = "HTTP/1.0 304 Not Modified ";
	private static final String SAME_DATE = "Mon, 12 Nov 2012 04:17:52 GMT";
	private static final String DIFFERENT_DATE = "Mon, 12 Nov 2010 04:17:52 GMT";
	private static final String VALID_URL_LINK = "src/webfiles/index.html";
	private static final String INVALID_URL_LINK = "src/webfiles/tree.html";
	private static final String INVALID_URL_LINK_CREATE_FILE = "src/webfiles/monster.html";
	private static final String INVALID_GET_REQUEST = "GET sdf/sdfd/ HTTP/1.0";
	
	@Test
	public void testHttpBadRequest() {
		String request = INVALID_GET_REQUEST;
		String expected;
		String actual = "";
		
		RequestHandler requestHandler = new RequestHandler();
		expected = BAD_REQUEST_1_0;
		
		try{
			actual = new String(requestHandler.processRequest(request.getBytes()));
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testHttpGetRequest() {
		String request = VALID_URL_LINK;
		String expected;
		String actual = "";
		String[] actualArray = null;
		
		FileServer fileServer = new FileServer();
		expected = OK_200_1_0;
		
		try{
			actual = new String(fileServer.httpGet(request));
			actualArray = actual.split("\n");
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		Assert.assertEquals(expected, actualArray[0]);
	}
	
	@Test(expected = Exception.class) 
	public void testHttpGetRequestInvalid() {
		String request = INVALID_URL_LINK;
		String expected;
		String actual = "";
		String[] actualArray = null;
		
		FileServer fileServer = new FileServer();
		expected = NOT_FOUND_1_0;
		
		try{
			actual = new String(fileServer.httpGet(request));
			actualArray = actual.split("\n");
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		Assert.assertEquals(expected, actualArray[0]);
	}
	
	@Test
	public void testHttpGetConditionalRequestDifferentDate() {
		String request = VALID_URL_LINK;
		String expected;
		String actual = "";
		String[] actualArray = null;
		
		String dateString = DIFFERENT_DATE;
		FileServer fileServer = new FileServer();
		expected = OK_200_1_0;
		
		try{
			Date date = new SimpleDateFormat("E',' dd MMM yyyy hh:mm:ss zzz").parse(dateString);
			actual = new String(fileServer.httpGETconditional(request,date));
			actualArray = actual.split("\n");
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		Assert.assertEquals(expected, actualArray[0]);
	}
	
	@Test
	public void testHttpGetConditional() {
		String request = VALID_URL_LINK;
		String expected;
		String actual = "";
		String[] actualArray = null;
		
		String dateString = SAME_DATE;
		FileServer fileServer = new FileServer();
		expected = NOT_MODIFIED_1_0;
		
		try{
			Date date = new SimpleDateFormat("E',' dd MMM yyyy hh:mm:ss zzz").parse(dateString);
			actual = new String(fileServer.httpGETconditional(request,date));
			actualArray = actual.split("\n");
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		Assert.assertEquals(expected, actualArray[0]);
	}
	
	@Test(expected = Exception.class) 
	public void testHttpGetConditionalInvalid() {
		String request = INVALID_URL_LINK;
		String expected;
		String actual = "";
		String[] actualArray = null;
		
		String dateString = SAME_DATE;
		FileServer fileServer = new FileServer();
		expected = NOT_FOUND_1_0;
		
		try{
			Date date = new SimpleDateFormat("E',' dd MMM yyyy hh:mm:ss zzz").parse(dateString);
			actual = new String(fileServer.httpGETconditional(request,date));
			actualArray = actual.split("\n");
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		Assert.assertEquals(expected, actualArray[0]);
	}
	
	@Test
	public void testHttpHeadRequest() {
		String request = VALID_URL_LINK;
		String expected;
		String actual = "";
		String[] actualArray = null;
		
		FileServer fileServer = new FileServer();
		expected = OK_200_1_0;
		
		try{
			actual = new String(fileServer.httpHEAD(request));
			actualArray = actual.split("\n");
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		Assert.assertEquals(expected, actualArray[0]);
	}
	
	@Test(expected = Exception.class) 
	public void testHttpHeadRequestInvalid() {
		String request = INVALID_URL_LINK;
		String expected;
		String actual = "";
		String[] actualArray = null;
		
		FileServer fileServer = new FileServer();
		expected = NOT_FOUND_1_0;
		
		try{
			actual = new String(fileServer.httpHEAD(request));
			actualArray = actual.split("\n");
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		Assert.assertEquals(expected, actualArray[0]);
	}
	
	@Test
	public void testHttpPostRequestValidFile() {
		String request = VALID_URL_LINK;
		String expected;
		String actual = "";
		String[] actualArray = null;
		
		FileServer fileServer = new FileServer();
		expected = OK_200_1_0;
		
		try{
			actual = new String(fileServer.httpPOST(request,("HELLO").getBytes()));
			actualArray = actual.split("\n");
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		Assert.assertEquals(expected, actualArray[0]);
	}
	
	@Test
	public void testHttpPostRequestInvalidFile() {
		String request = INVALID_URL_LINK_CREATE_FILE;
		String expected;
		String actual = "";
		String[] actualArray = null;
		
		FileServer fileServer = new FileServer();
		expected = CREATED_1_0;
		
		try{
			actual = new String(fileServer.httpPOST(request,("HELLO").getBytes()));
			actualArray = actual.split("\n");
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		Assert.assertEquals(expected, actualArray[0]);
	}
}
