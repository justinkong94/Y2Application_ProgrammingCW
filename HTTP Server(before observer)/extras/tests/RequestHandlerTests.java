// make sure to add JUnit to your project to run this class.
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

/* * 3 tests are provided for you, these WILL be part of the marking criteria for functionality tests.
 * It's a good idea to make sure they pass, and add some more tests here.
 * You should not modify the existing tests.
 */
public class RequestHandlerTests {

	private static final String NO_IMPL_GET_1_0 = "HTTP/1.0 501 GET Not Implemented\r\n\r\n";
	private static final String NO_IMPL_HEAD_1_0 = "HTTP/1.0 501 HEAD Not Implemented\r\n\r\n";
	private static final String NO_IMPL_POST_1_0 = "HTTP/1.0 501 POST Not Implemented\r\n\r\n";
	private static final String BAD_REQUEST_1_0 = "HTTP/1.0 400 Bad Request\r\n\r\n";
	private static final String FILE_NOT_FOUND_1_0 = "HTTP/1.0 404 Not Found ";

	@Test
	public void testGetRequestHttp1() {
		String request = "GET / HTTP/1.0\r\n\r\n";
		String expected;
		String actual;
		String[] actualArray = null;
			
		// Request handler needs to have a constructor with no arguments.
		RequestHandler requestHandler = new RequestHandler();
		expected = FILE_NOT_FOUND_1_0;
		actual = new String(requestHandler.processRequest(request.getBytes()));
		actualArray = actual.split("Date:");
		
		Assert.assertEquals(expected, actualArray[0]);
	}
	
	@Test
	public void testHeadRequestHttp1() {
		String request = "HEAD / HTTP/1.0\r\n\r\n";
		String expected;
		String actual;
		
		RequestHandler requestHandler = new RequestHandler();
		expected = FILE_NOT_FOUND_1_0;
		actual = new String(requestHandler.processRequest(request.getBytes()));
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testPostRequestHttp1() {
		String request = "POST / HTTP/1.0\r\n\r\n";
		String expected;
		String actual;
		
		RequestHandler requestHandler = new RequestHandler();
		expected = FILE_NOT_FOUND_1_0;
		actual = new String(requestHandler.processRequest(request.getBytes()));
		Assert.assertEquals(expected, actual);
	}
		
	@Test
	public void testInvalidRequest() {
		
		String request = "Rabble rabble";
		String expected;
		String actual;
		
		RequestHandler requestHandler = new RequestHandler();
		expected = BAD_REQUEST_1_0;
		actual = new String(requestHandler.processRequest(request.getBytes()));
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testInvalidHTTPRequest() {
		
		String request = "POST / HTTP/1.1\r\n\r\n";
		String expected;
		String actual;
		
		RequestHandler requestHandler = new RequestHandler();
		expected = BAD_REQUEST_1_0;
		actual = new String(requestHandler.processRequest(request.getBytes()));
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testOtherHTTPRequest() {
		
		String request = "GET / HTTP/0.9\r\n\r\n";
		String expected;
		String actual;
		
		RequestHandler requestHandler = new RequestHandler();
		expected = NO_IMPL_GET_1_0;
		actual = new String(requestHandler.processRequest(request.getBytes()));
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testNoHTTPRequestGetMethod() {
		
		String request = "GET / \r\n\r\n";
		String expected;
		String actual;
		
		RequestHandler requestHandler = new RequestHandler();
		expected = NO_IMPL_GET_1_0;
		actual = new String(requestHandler.processRequest(request.getBytes()));
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testNoHTTPRequestHeadMethod() {
		
		String request = "HEAD / \r\n\r\n";
		String expected;
		String actual;
		
		RequestHandler requestHandler = new RequestHandler();
		expected = BAD_REQUEST_1_0;
		actual = new String(requestHandler.processRequest(request.getBytes()));
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testNoURI() {
		
		String request = "HEAD \r\n\r\n";
		String expected;
		String actual;
		
		RequestHandler requestHandler = new RequestHandler();
		expected = BAD_REQUEST_1_0;
		actual = new String(requestHandler.processRequest(request.getBytes()));
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testInvalidURI() {
		
		String request = "HEAD Rabble \r\n\r\n";
		String expected;
		String actual;
		
		RequestHandler requestHandler = new RequestHandler();
		expected = BAD_REQUEST_1_0;
		actual = new String(requestHandler.processRequest(request.getBytes()));
		Assert.assertEquals(expected, actual);
	}
}
