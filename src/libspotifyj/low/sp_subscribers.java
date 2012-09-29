package libspotifyj.low;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class sp_subscribers extends Structure {

	public int count;
	public Pointer subscribers;
	
}
