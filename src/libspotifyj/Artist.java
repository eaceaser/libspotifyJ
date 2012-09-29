package libspotifyj;

import libspotifyj.low.SpotifyJ;
import libspotifyj.low.sp_artist;
import libspotifyj.low.sp_link;

public class Artist {

	private static SpotifyJ libspotify = SpotifyJ.libspotify;
	sp_artist artistPtr;
		
	Artist(sp_artist artistPtr) {
		this.artistPtr = artistPtr;
		synchronized (SpotifyJ.lock) {
			libspotify.sp_artist_add_ref(artistPtr);
		}
	}
	
	public static Artist createFromLink(Link link) {
		Artist result = null;
		
		if (link.linkPtr != null) {
			synchronized (SpotifyJ.lock) {
				sp_artist newArtistPtr = libspotify.sp_link_as_artist(link.linkPtr);
				if (newArtistPtr != null)
					result = new Artist(newArtistPtr);
			}
		}
		
		return result;
	}
	
	public Link createLink() {
		synchronized (SpotifyJ.lock) {
			sp_link link = libspotify.sp_link_create_from_artist(artistPtr);
			return (link != null) ? new Link(link) : null;
		}
	}
	
	public static String artistsToString(Artist[] artists) {
		if (artists.length == 1)
			return artists[0].getName();
		
		StringBuilder sb = new StringBuilder();
		int len = artists.length;
		
		for (int i = 0; i < len-1; i++) {
			sb.append(artists[i].getName() + ", ");
		}
		sb.append(artists[len-1]);
		
		return sb.toString();
	}
	
	public String getName() {
		synchronized (SpotifyJ.lock) {
			return libspotify.sp_artist_name(artistPtr);
		}
	}
	
	public boolean isLoaded() {
		if (artistPtr == null)
			return false;

		synchronized (SpotifyJ.lock) {
			return libspotify.sp_artist_is_loaded(artistPtr);
		}
	}
	
	public String toString() {
		if (isLoaded()) {
			return "Artist: Name = " + getName() + ", Link = " + createLink().toString(); 
		} else {
			return "Artist: Not loaded";
		}
	}
	
	protected void finalize() {
		if (artistPtr != null) {
			synchronized (SpotifyJ.lock) {
				libspotify.sp_artist_release(artistPtr);
			}
		}
	}
	
}
