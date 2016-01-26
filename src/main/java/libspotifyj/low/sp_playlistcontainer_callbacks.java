package libspotifyj.low;

import com.sun.jna.Callback;
import com.sun.jna.Structure;

public class sp_playlistcontainer_callbacks extends Structure {
	
	public Callback playlist_added;
	public Callback playlist_removed;
	public Callback playlist_moved;
	public Callback container_loaded;

}
