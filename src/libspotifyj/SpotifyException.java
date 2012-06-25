package libspotifyj;

import libspotifyj.low.SpotifyJ;

@SuppressWarnings("serial")
public class SpotifyException extends Exception {

	private static final SpotifyJ libspotify = SpotifyJ.libspotify;
	private int errorCode;
	
	public SpotifyException(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getMessage() {
		return "Error from libspotify: " + libspotify.sp_error_message(errorCode);
	}
	
}
