
public interface ILogger {
	
	public void setArguments(String info);

	public void checkArguments();
	
	public void writeRequestDetails(String fullRequest);
	
	public void writeResponseDetails(String fullResponse);

}
