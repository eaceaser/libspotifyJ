package libspotifyj.events;

public class SessionEventArgs implements SpotifyEventArgs {

	private int error;
	private String message;
	
	public SessionEventArgs() {}
	
	public SessionEventArgs(int error) {
		this.error = error;
	}
	
	public SessionEventArgs(String message) {
		this.message = message;
	}
	
	public int getError() {
		return error;
	}
	
	public String getMessage() {
		return message;
	}
	
}
