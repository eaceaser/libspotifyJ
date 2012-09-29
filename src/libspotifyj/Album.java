package libspotifyj;

import com.sun.jna.Pointer;
import libspotifyj.low.SpotifyJ;
import libspotifyj.low.sp_album;
import libspotifyj.low.sp_link;

public class Album {
	
	private static SpotifyJ libspotify = SpotifyJ.libspotify;
	sp_album albumPtr;
		
	Album(sp_album albumPtr) {
		this.albumPtr = albumPtr;
		synchronized (SpotifyJ.lock) {
			libspotify.sp_album_add_ref(albumPtr);
		}
	}
	
	public static Album createFromLink(Link link) {
		Album result = null;
		
		if (link.linkPtr != null) {
			synchronized (SpotifyJ.lock) {
				sp_album newAlbumPtr = libspotify.sp_link_as_album(link.linkPtr);
				if (newAlbumPtr != null)
					result = new Album(newAlbumPtr);
			}
		}
		
		return result;
	}
	
	public Link createLink() {
		synchronized (SpotifyJ.lock) {
			sp_link link = libspotify.sp_link_create_from_album(albumPtr);
			return (link != null) ? new Link(link) : null;
		}
	}
	
	public boolean isLoaded() {
		if (albumPtr == null)
			return false;

		synchronized (SpotifyJ.lock) {
			return libspotify.sp_album_is_loaded(albumPtr);
		}
	}
	
	public boolean isAvailable() {
		if (albumPtr == null)
			return false;
		
		synchronized (SpotifyJ.lock) {
			return libspotify.sp_album_is_available(albumPtr);
		}
	}
	
	public Artist getArtist() {
		synchronized (SpotifyJ.lock) {
			return new Artist(libspotify.sp_album_artist(albumPtr));
		}
	}
	
	public String getName() {
		synchronized (SpotifyJ.lock) {
			return libspotify.sp_album_name(albumPtr);
		}
	}
	
	public int getYear() {
		synchronized (SpotifyJ.lock) {
			return libspotify.sp_album_year(albumPtr);
		}
	}
	
	public int getAlbumType() {
		synchronized (SpotifyJ.lock) {
			return libspotify.sp_album_type(albumPtr);
		}
	}
	
	public String getCoverId() {
		synchronized (SpotifyJ.lock) {
			Pointer coverIdPtr = libspotify.sp_album_cover(albumPtr);
			
			if (coverIdPtr == null)
				return null;
			
			byte[] coverId = new byte[20];
			coverId = coverIdPtr.getByteArray(0, coverId.length);
			
			return Util.imageIdToString(coverId);
		}
	}
	
	public String toString() {
		if (isLoaded()) {
			return "Album: Artist = " + getArtist().getName() + ", Name = " + getName() + ", Year = " + getYear() + ", Link = " + createLink().toString(); 
		} else {
			return "Album: Not loaded";
		}
	}
	
	protected void finalize() {
		if (albumPtr != null) {
			synchronized (SpotifyJ.lock) {
				libspotify.sp_album_release(albumPtr);
			}
		}
	}

}
