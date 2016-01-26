package libspotifyj.low;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class sp_artistbrowse extends PointerType {

  public sp_artistbrowse(Pointer address) {
    super(address);
  }

  public sp_artistbrowse() {
    super();
  }

}
