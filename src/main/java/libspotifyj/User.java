package libspotifyj;

import libspotifyj.low.SpotifyJ;
import libspotifyj.low.sp_link;
import libspotifyj.low.sp_user;

public class User {
	
	private static SpotifyJ libspotify = SpotifyJ.libspotify;
	private sp_user userPtr;
	
	User(sp_user userPtr) {
		this.userPtr = userPtr;
	}
	
	public static User createFromLink(Link link) {
		User result = null;
		
		if (link.linkPtr != null) {
			synchronized (SpotifyJ.lock) {
				sp_user newUserPtr = libspotify.sp_link_as_user(link.linkPtr);
				if (newUserPtr != null)
					result = new User(newUserPtr);
			}
		}
		
		return result;
	}
	
	public Link createLink() {
		synchronized (SpotifyJ.lock) {
			sp_link link = libspotify.sp_link_create_from_user(userPtr);
			return (link != null) ? new Link(link) : null;
		}
	}
	
	public String getCanonicalName() {
		if (userPtr == null)
			return "Unknown";
		
		synchronized (SpotifyJ.lock) {
			return libspotify.sp_user_canonical_name(userPtr);
		}
	}
	
	public String getDisplayName() {
		if (userPtr == null)
			return "Unknown";
		
		synchronized (SpotifyJ.lock) {
			return libspotify.sp_user_display_name(userPtr);
		}
	}
	
	public boolean isLoaded() {
		if (userPtr == null)
			return false;
		
		synchronized (SpotifyJ.lock) {
			return libspotify.sp_user_is_loaded(userPtr);
		}
	}
	
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof User))
			return false;

		User user = (User) o;
		return userPtr.equals(user.userPtr);
	}

}
