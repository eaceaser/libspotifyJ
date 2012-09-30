package libspotifyj.events;

import libspotifyj.Playlist;

public abstract class PlaylistEventHandler {
	
	public abstract void process(Playlist playlist, PlaylistEventArgs eventArgs);

}
