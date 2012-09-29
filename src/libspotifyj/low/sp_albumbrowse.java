package libspotifyj.low;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class sp_albumbrowse extends PointerType {

    public sp_albumbrowse(Pointer address) {
        super(address);
    }
	
    public sp_albumbrowse() {
        super();
    }
	
}
