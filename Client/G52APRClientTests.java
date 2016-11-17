import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Assert;
import org.junit.Test;


public class G52APRClientTests {
	
	private static final String NO_IMPL_GET_1_0 = "HTTP/1.0 501 GET Not Implemented";
	private static final String NO_IMPL_HEAD_1_0 = "HTTP/1.0 501 HEAD Not Implemented";
	private static final String NO_IMPL_POST_1_0 = "HTTP/1.0 501 POST Not Implemented";
	private static final String POST_STRING = "Hello World!";
	private static final String URL_4444 = "http://localhost:4444";

	@Test
	public void testGetRequest() {
		String expected;
		String actual;
		
		G52APRClient client = new G52APRClient();
		expected = NO_IMPL_GET_1_0;
		actual = new String(client.httpGet(URL_4444));
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testHeadRequest() {
		String expected;
		String actual;
		
		G52APRClient client = new G52APRClient();
		expected = NO_IMPL_HEAD_1_0;
		actual = new String(client.httpHead(URL_4444));
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testPostRequestString() {
		String expected;
		String actual;
		
		G52APRClient client = new G52APRClient();
		expected = NO_IMPL_POST_1_0;
		actual = new String(client.httpPost(URL_4444,POST_STRING));
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testPostRequestArrayList() {
		String expected;
		String actual;
		
		ArrayList<NameValuePair> testArray = new ArrayList <NameValuePair>();
		testArray.add(new BasicNameValuePair("green","world"));
		testArray.add(new BasicNameValuePair("lemon","tree"));
		
		G52APRClient client = new G52APRClient();
		expected = NO_IMPL_POST_1_0;
		actual = new String(client.httpPost(URL_4444,testArray));
		Assert.assertEquals(expected, actual);
	}
	

}
