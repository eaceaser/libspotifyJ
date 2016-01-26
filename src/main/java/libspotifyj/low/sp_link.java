package libspotifyj.low;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class sp_link extends PointerType {

  public sp_link(Pointer address) {
    super(address);
  }

  public sp_link() {
    super();
  }

}
