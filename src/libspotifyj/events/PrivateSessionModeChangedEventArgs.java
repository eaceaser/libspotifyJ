package libspotifyj.events;

public class PrivateSessionModeChangedEventArgs implements SpotifyEventArgs {
	
	private boolean isPrivate;
	
	public PrivateSessionModeChangedEventArgs(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
	public boolean isPrivate() {
		return isPrivate;
	}

}
