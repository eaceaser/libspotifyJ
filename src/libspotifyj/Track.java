package libspotifyj;

import libspotifyj.low.SpotifyJ;
import libspotifyj.low.sp_track;

public class Track {

	private static SpotifyJ libspotify = SpotifyJ.libspotify;
	private sp_track trackPtr;
	
	 Track(sp_track trackPtr) {
		this.trackPtr = trackPtr;
		synchronized (SpotifyJ.lock) {
			libspotify.sp_track_add_ref(trackPtr);
		}
	}
	
	public static Track createFromLink(Link link) {
		Track result = null;
		
		if (link.linkPtr != null) {
			synchronized (SpotifyJ.lock) {
				sp_track newTrackPtr = libspotify.sp_link_as_track(link.linkPtr);
				if (newTrackPtr != null)
					result = new Track(newTrackPtr);
			}
		}
				
		return result;
	}
	
	public String getName() {
		synchronized (SpotifyJ.lock) {
			return libspotify.sp_track_name(trackPtr);
		}
	}
	
	protected void finalize() {
		if (trackPtr != null) {
			synchronized (SpotifyJ.lock) {
				libspotify.sp_track_release(trackPtr);
			}
		}
	}
	
}
