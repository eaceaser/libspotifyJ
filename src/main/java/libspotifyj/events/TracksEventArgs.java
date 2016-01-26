package libspotifyj.events;

import libspotifyj.Track;
import libspotifyj.User;

public class TracksEventArgs implements SpotifyEventArgs {

	private Track[] tracks;
	private int[] trackIndices;
	private int position;
	private int newPosition;
	private Track[] currentTracks;
	
	private User user;
	private int when;
	
	private boolean seen;
	private String message;
	
	public TracksEventArgs(Track[] tracks, int[] trackIndices, int position, int newPosition, Track[] currentTracks) {
		this.tracks = tracks;
		this.trackIndices = trackIndices;
		this.position = position;
		this.newPosition = newPosition;
		this.currentTracks = currentTracks;
	}
	
	public TracksEventArgs(int position, User user, int when) {
		this.position = position;
		this.user = user;
		this.when = when;
	}
	
	public TracksEventArgs(int position, boolean seen) {
		this.position = position;
		this.seen = seen;
	}
	
	public TracksEventArgs(int position, String message) {
		this.position = position;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean getSeen() {
		return seen;
	}
	
	public User getUser() {
		return user;
	}
	
	public int getWhen() {
		return when;
	}
	
	public Track[] getTracks() {
		return tracks;
	}
	
	public int[] getTrackIndices() {
		return trackIndices;
	}
	
	public int getPosition() {
		return position;
	}
	
	public int getNewPosition() {
		return newPosition;
	}
	
	public Track[] getCurrentTracks() {
		return currentTracks;
	}
	
}
