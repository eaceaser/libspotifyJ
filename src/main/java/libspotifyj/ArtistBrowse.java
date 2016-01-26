package libspotifyj;

import com.sun.jna.Pointer;
import libspotifyj.low.SpotifyJ;
import libspotifyj.low.sp_artistbrowse;

public class ArtistBrowse {

  private static SpotifyJ libspotify = SpotifyJ.libspotify;
  private sp_artistbrowse artistBrowsePtr;

  private int error;
  private Artist artist;
  private String[] portraitIds;
  private Track[] tracks;
  private Track[] tophitTracks;
  private Album[] albums;
  private Artist[] similarArtists;
  private String biography;

  ArtistBrowse(sp_artistbrowse artistBrowsePtr) {
    this.artistBrowsePtr = artistBrowsePtr;

    synchronized (SpotifyJ.lock) {
      error = libspotify.sp_artistbrowse_error(artistBrowsePtr);
      artist = new Artist(libspotify.sp_artistbrowse_artist(artistBrowsePtr));

      portraitIds = new String[libspotify.sp_artistbrowse_num_portraits(artistBrowsePtr)];
      for (int i = 0; i < portraitIds.length; i++) {
        Pointer portraitIdPtr = libspotify.sp_artistbrowse_portrait(artistBrowsePtr, i);

        if (portraitIdPtr != null) {
          byte[] portraitId = new byte[20];
          portraitId = portraitIdPtr.getByteArray(0, portraitId.length);
          portraitIds[i] = Util.imageIdToString(portraitId);
        }
      }

      tracks = new Track[libspotify.sp_artistbrowse_num_tracks(artistBrowsePtr)];
      for (int i = 0; i < tracks.length; i++)
        tracks[i] = new Track(libspotify.sp_artistbrowse_track(artistBrowsePtr, i));

      tophitTracks = new Track[libspotify.sp_artistbrowse_num_tophit_tracks(artistBrowsePtr)];
      for (int i = 0; i < tophitTracks.length; i++)
        tophitTracks[i] = new Track(libspotify.sp_artistbrowse_tophit_track(artistBrowsePtr, i));

      albums = new Album[libspotify.sp_artistbrowse_num_albums(artistBrowsePtr)];
      for (int i = 0; i < albums.length; i++)
        albums[i] = new Album(libspotify.sp_artistbrowse_album(artistBrowsePtr, i));

      similarArtists = new Artist[libspotify.sp_artistbrowse_num_similar_artists(artistBrowsePtr)];
      for (int i = 0; i < similarArtists.length; i++)
        similarArtists[i] = new Artist(libspotify.sp_artistbrowse_similar_artist(artistBrowsePtr, i));

      biography = libspotify.sp_artistbrowse_biography(artistBrowsePtr);

      libspotify.sp_artistbrowse_release(artistBrowsePtr);
    }
  }

  public boolean isLoaded() {
    if (artistBrowsePtr == null)
      return false;

    synchronized (SpotifyJ.lock) {
      return libspotify.sp_artistbrowse_is_loaded(artistBrowsePtr);
    }
  }

  public int getError() {
    return error;
  }

  public Artist getArtist() {
    return artist;
  }

  public Track[] getTracks() {
    return tracks;
  }

  public Track[] tophitTracks() {
    return tophitTracks;
  }

  public Album[] getAlbums() {
    return albums;
  }

  public Artist[] getSimilarArtists() {
    return similarArtists;
  }

  public String[] getPortraitIds() {
    return portraitIds;
  }

  public String getBiography() {
    return biography;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("Artist browse:\n");
    sb.append("Artist = " + artist.getName() + "\n");
    sb.append("Albums =\n");
    for (Album a : albums)
      sb.append(a.getName() + "\n");
    sb.append("Tracks =\n");
    for (Track t : tracks)
      sb.append(t.getName() + "\n");
    sb.append("Tophit tracks =\n");
    for (Track t : tophitTracks)
      sb.append(t.getName() + "\n");
    sb.append("Similar artists =\n");
    for (Artist a : similarArtists)
      sb.append(a.getName() + "\n");
    sb.append("Biography = " + biography + "\n");
    sb.append("Portrait Ids =\n");
    for (String s : portraitIds)
      sb.append(s + "\n");

    return sb.toString();
  }
}
