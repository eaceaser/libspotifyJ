package libspotifyj.low;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class sp_artist extends PointerType {

  public sp_artist(Pointer address) {
    super(address);
  }

  public sp_artist() {
    super();
  }

}
