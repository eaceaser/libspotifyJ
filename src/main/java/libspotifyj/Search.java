package libspotifyj;

import libspotifyj.low.SpotifyJ;
import libspotifyj.low.sp_album;
import libspotifyj.low.sp_artist;
import libspotifyj.low.sp_search;
import libspotifyj.low.sp_track;

public class Search {
	
	private static SpotifyJ libspotify = SpotifyJ.libspotify;
	private sp_search searchPtr;
	
	private int error;
	private Track[] tracks;
	private Album[] albums;
	private Artist[] artists;
	private String[] playlists;
	private String[] playlistURIs;
	private String[] playlistImageURIs;
	private String query;
	private String didYouMean;
	private int totalTracks;
	private int totalAlbums;
	private int totalArtists;

	Search(sp_search searchPtr) {
		this.searchPtr = searchPtr;
		
		synchronized (SpotifyJ.lock) {
			error = libspotify.sp_search_error(searchPtr);
			
			tracks = new Track[libspotify.sp_search_num_tracks(searchPtr)];
			for (int i = 0; i < tracks.length; i++) {
				sp_track trackPtr = libspotify.sp_search_track(searchPtr, i);
				tracks[i] = new Track(trackPtr);
			}
			
			albums = new Album[libspotify.sp_search_num_albums(searchPtr)];
			for (int i = 0; i < albums.length; i++) {
				sp_album albumPtr = libspotify.sp_search_album(searchPtr, i);
				albums[i] = new Album(albumPtr);
			}
			
			artists = new Artist[libspotify.sp_search_num_artists(searchPtr)];
			for (int i = 0; i < artists.length; i++) {
				sp_artist artistPtr = libspotify.sp_search_artist(searchPtr, i);
				artists[i] = new Artist(artistPtr);
			}
			
			playlists = new String[libspotify.sp_search_num_playlists(searchPtr)];
			playlistURIs = new String[libspotify.sp_search_num_playlists(searchPtr)];
			playlistImageURIs = new String[libspotify.sp_search_num_playlists(searchPtr)];
			for (int i = 0; i < playlists.length; i++) {
				String playlist = libspotify.sp_search_playlist_name(searchPtr, i);
				playlists[i] = playlist;
				String playlistURI = libspotify.sp_search_playlist_uri(searchPtr, i);
				playlistURIs[i] = playlistURI;
				String playlistImageURI = libspotify.sp_search_playlist_image_uri(searchPtr, i);
				playlistImageURIs[i] = playlistImageURI;
			}
			
			query = libspotify.sp_search_query(searchPtr);
			didYouMean = libspotify.sp_search_did_you_mean(searchPtr);
			
			totalTracks = libspotify.sp_search_total_tracks(searchPtr);
			totalAlbums = libspotify.sp_search_total_albums(searchPtr);
			totalArtists = libspotify.sp_search_total_artists(searchPtr);
		}
	}
	
	public boolean isLoaded() {
		if (searchPtr == null)
			return false;

		synchronized (SpotifyJ.lock) {
			return libspotify.sp_search_is_loaded(searchPtr);
		}
	}
	
	public int getError() {
		return error;
	}
	
	public Track[] getTracks() {
		return tracks;
	}
	
	public Album[] getAlbums() {
		return albums;
	}
	
	public Artist[] getArtists() {
		return artists;
	}
	
	public String[] getPlaylistNames() {
		return playlists;
	}
	
	public String[] getPlaylistURIs() {
		return playlistURIs;
	}
	
	public String[] getPlaylistImageURIs() {
		return playlistImageURIs;
	}
	
	public String getQuery() {
		return query;
	}
	
	public String getDidYouMean() {
		return didYouMean;
	}
	
	public int getTotalTracks() {
		return totalTracks;
	}
	
	public int getTotalAlbums() {
		return totalAlbums;
	}
	
	public int getTotalArtists() {
		return totalArtists;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Search: Query = " + query + "\n");
		sb.append("Num tracks = " + tracks.length + "\n");
		sb.append("Num albums = " + albums.length + "\n");
		sb.append("Num artists = " + artists.length + "\n");
		sb.append("Tot tracks = " + totalTracks + "\n");
		sb.append("Tot albums = " + totalAlbums + "\n");
		sb.append("Tot artists = " + totalArtists + "\n");
		sb.append("Did you mean = " + didYouMean);
		
		return sb.toString();
	}
	
	protected void finalize() {
		if (searchPtr != null) {
			synchronized (SpotifyJ.lock) {
				libspotify.sp_search_release(searchPtr);
			}
		}
	}
	
}
