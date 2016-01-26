package libspotifyj;

import libspotifyj.low.SpotifyJ;
import libspotifyj.low.sp_album;
import libspotifyj.low.sp_link;
import libspotifyj.low.sp_track;

public class Track {

  private static SpotifyJ libspotify = SpotifyJ.libspotify;
  sp_track trackPtr;

  private boolean isLoaded = false;
  private int offlineStatus = Constants.TRACK_OFFLINE_NO;
  private int availability = Constants.TRACK_AVAILABILITY_UNAVAILABLE;
  private boolean isLocal = false;
  private int error = Constants.ERROR_RESOURCE_NOT_LOADED;
  private Album album = null;
  private Artist[] artists = null;
  private String name = "";
  private int duration = 0;
  private int popularity = 0;
  private int disc = 0;
  private int index = 0;

  Track(sp_track trackPtr) {
    this.trackPtr = trackPtr;
    synchronized (SpotifyJ.lock) {
      libspotify.sp_track_add_ref(trackPtr);
    }
  }

  void checkLoaded(Session session) {
    if (isLoaded)
      return;

    synchronized (SpotifyJ.lock) {
      isLoaded = libspotify.sp_track_is_loaded(trackPtr);
    }

    if (!isLoaded)
      return;

    synchronized (SpotifyJ.lock) {
      if (session != null) {
        availability = libspotify.sp_track_get_availability(session.sessionPtr, trackPtr);
        isLocal = libspotify.sp_track_is_local(session.sessionPtr, trackPtr);
      }

      offlineStatus = libspotify.sp_track_offline_get_status(trackPtr);
      error = libspotify.sp_track_error(trackPtr);

      sp_album albumPtr = libspotify.sp_track_album(trackPtr);
      if (albumPtr != null)
        album = new Album(albumPtr);

      artists = new Artist[libspotify.sp_track_num_artists(trackPtr)];
      for (int i = 0; i < artists.length; i++)
        artists[i] = new Artist(libspotify.sp_track_artist(trackPtr, i));

      name = libspotify.sp_track_name(trackPtr);
      duration = libspotify.sp_track_duration(trackPtr);
      popularity = libspotify.sp_track_popularity(trackPtr);
      disc = libspotify.sp_track_disc(trackPtr);
      index = libspotify.sp_track_index(trackPtr);
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

  public Link createLink(int offset) {
    synchronized (SpotifyJ.lock) {
      sp_link link = libspotify.sp_link_create_from_track(trackPtr, offset);
      return (link != null) ? new Link(link) : null;
    }
  }

  public boolean isLoaded() {
    checkLoaded(null);
    return isLoaded;
  }

  public boolean isLocal(Session session) {
    checkLoaded(session);
    return isLocal;
  }

  public int getOfflineStatus() {
    checkLoaded(null);
    return offlineStatus;
  }

  public boolean isStarred(Session session) {
    checkLoaded(null);
    synchronized (SpotifyJ.lock) {
      return libspotify.sp_track_is_starred(session.sessionPtr, trackPtr);
    }
  }

  public int getAvailability(Session session) {
    checkLoaded(session);
    return availability;
  }

  public int getError() {
    checkLoaded(null);
    return error;
  }

  public Album getAlbum() {
    checkLoaded(null);
    return album;
  }

  public Artist[] getArtists() {
    checkLoaded(null);
    return artists;
  }

  public String getName() {
    checkLoaded(null);
    return name;
  }

  public int getDuration() {
    checkLoaded(null);
    return duration;
  }

  public int getPopularity() {
    checkLoaded(null);
    return popularity;
  }

  public int getDisc() {
    checkLoaded(null);
    return disc;
  }

  public int getIndex() {
    checkLoaded(null);
    return index;
  }

  protected void finalize() {
    if (trackPtr != null) {
      synchronized (SpotifyJ.lock) {
        libspotify.sp_track_release(trackPtr);
      }
    }
  }

}
