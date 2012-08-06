package libspotifyj.events;

import libspotifyj.Session;

public abstract class SessionEventHandler implements SpotifyEventHandler {
	
	public abstract void process(Session session, SessionEventArgs eventArgs);

	public void process(Session session, SpotifyEventArgs eventArgs) {
		process(session, (SessionEventArgs) eventArgs);
	}
	
}
