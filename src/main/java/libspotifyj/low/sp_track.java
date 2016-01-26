package libspotifyj.low;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class sp_track extends PointerType {

  public sp_track(Pointer address) {
    super(address);
  }

  public sp_track() {
    super();
  }

}
