package libspotifyj.low;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class sp_playlist extends PointerType {

    public sp_playlist(Pointer address) {
        super(address);
    }
	
    public sp_playlist() {
        super();
    }
	
}
