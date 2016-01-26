package libspotifyj.low;

import com.sun.jna.Callback;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class sp_playlistcontainer_callbacks extends Structure {

  public Callback playlist_added;
  public Callback playlist_removed;
  public Callback playlist_moved;
  public Callback container_loaded;

  protected List getFieldOrder() {
    return Arrays.asList(new String[] {
        "playlist_added", "playlist_removed", "playlist_moved", "container_loaded"
    });
  }
}
