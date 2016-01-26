package libspotifyj.low;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class sp_playlistcontainer extends PointerType {

  public sp_playlistcontainer(Pointer address) {
    super(address);
  }

  public sp_playlistcontainer() {
    super();
  }

}
