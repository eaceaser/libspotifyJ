package libspotifyj.low;

import com.sun.jna.Callback;
import com.sun.jna.Structure;

public class sp_playlist_callbacks extends Structure {

	public Callback tracks_added;
	public Callback tracks_removed;
	public Callback tracks_moved;
	public Callback playlist_renamed;
	public Callback playlist_state_changed;
	public Callback playlist_update_in_progress;
	public Callback playlist_metadata_updated;
	public Callback track_created_changed;
	public Callback track_seen_changed;
	public Callback description_changed;
	public Callback image_changed;
	public Callback track_message_changed;
	public Callback subscribers_changed;
	
}
