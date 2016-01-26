package libspotifyj;

import libspotifyj.low.SpotifyJ;
import libspotifyj.low.sp_albumbrowse;

public class AlbumBrowse {

  private static SpotifyJ libspotify = SpotifyJ.libspotify;
  private sp_albumbrowse albumBrowsePtr;

  private int error;
  private Album album;
  private Artist artist;
  private String[] copyrights;
  private Track[] tracks;
  private String review;

  AlbumBrowse(sp_albumbrowse albumBrowsePtr) {
    this.albumBrowsePtr = albumBrowsePtr;

    synchronized (SpotifyJ.lock) {
      error = libspotify.sp_albumbrowse_error(albumBrowsePtr);
      album = new Album(libspotify.sp_albumbrowse_album(albumBrowsePtr));
      artist = new Artist(libspotify.sp_albumbrowse_artist(albumBrowsePtr));

      copyrights = new String[libspotify.sp_albumbrowse_num_copyrights(albumBrowsePtr)];
      for (int i = 0; i < copyrights.length; i++)
        copyrights[i] = libspotify.sp_albumbrowse_copyright(albumBrowsePtr, i);

      tracks = new Track[libspotify.sp_albumbrowse_num_tracks(albumBrowsePtr)];
      for (int i = 0; i < tracks.length; i++)
        tracks[i] = new Track(libspotify.sp_albumbrowse_track(albumBrowsePtr, i));

      review = libspotify.sp_albumbrowse_review(albumBrowsePtr);

      libspotify.sp_albumbrowse_release(albumBrowsePtr);
    }
  }

  public boolean isLoaded() {
    if (albumBrowsePtr == null)
      return false;

    synchronized (SpotifyJ.lock) {
      return libspotify.sp_albumbrowse_is_loaded(albumBrowsePtr);
    }
  }

  public int getError() {
    return error;
  }

  public Album getAlbum() {
    return album;
  }

  public Artist getArtist() {
    return artist;
  }

  public Track[] getTracks() {
    return tracks;
  }

  public String[] getCopyrights() {
    return copyrights;
  }

  public String getReview() {
    return review;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("Album browse:\n");
    sb.append("Album = " + album.getName() + "\n");
    sb.append("Artist = " + artist.getName() + "\n");
    sb.append("Coprights =\n");
    for (String s : copyrights)
      sb.append(s + "\n");
    sb.append("Tracks =\n");
    for (Track t : tracks)
      sb.append(t.getName() + "\n");
    sb.append("Review = " + review + "\n");

    return sb.toString();
  }
}
