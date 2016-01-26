package libspotifyj.low;

import com.sun.jna.Callback;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class sp_playlist_callbacks extends Structure implements Structure.ByReference {

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

  protected List getFieldOrder() {
    return Arrays.asList(new String[] {
        "tracks_added", "tracks_removed", "tracks_moved", "playlist_renamed",
        "playlist_state_changed", "playlist_update_in_progress", "playlist_metadata_updated",
        "track_created_changed", "track_seen_changed", "description_changed", "image_changed",
        "track_message_changed", "subscribers_changed"
    });
  }
}
