package libspotifyj.low;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class sp_album extends PointerType {

    public sp_album(Pointer address) {
        super(address);
    }
	
    public sp_album() {
        super();
    }
	
}
