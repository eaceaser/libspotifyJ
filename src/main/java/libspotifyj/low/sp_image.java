package libspotifyj.low;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class sp_image extends PointerType {

  public sp_image(Pointer address) {
    super(address);
  }

  public sp_image() {
    super();
  }

}
