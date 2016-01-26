package libspotifyj;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import libspotifyj.events.PlaylistEventHandler;
import libspotifyj.events.TracksEventHandler;
import libspotifyj.low.*;

import java.util.HashMap;
import java.util.Map;

public class Playlist {

  private static SpotifyJ libspotify = SpotifyJ.libspotify;

  /* Playlist data */
  private static Map<sp_playlist, Playlist> playlists = new HashMap<sp_playlist, Playlist>();
  private sp_playlist playlistPtr;
  private sp_playlist_callbacks callbacks;
  private Session session;

  /* Event handlers */
  private TracksEventHandler tracksAddedHandler;
  private TracksEventHandler tracksRemovedHandler;
  private TracksEventHandler tracksMovedHandler;
  private PlaylistEventHandler playlistRenamedHandler;
  private PlaylistEventHandler playlistStateChangedHandler;
  private PlaylistEventHandler playlistUpdateInProgressHandler;
  private PlaylistEventHandler playlistMetadataUpdatedHandler;
  private TracksEventHandler trackCreatedChangedHandler;
  private TracksEventHandler trackSeenChangedHandler;
  private PlaylistEventHandler descriptionChangedHandler;
  private PlaylistEventHandler imageChangedHandler;
  private TracksEventHandler trackMessageChangedHandler;
  private PlaylistEventHandler subscribersChangedHandler;

  private Playlist(sp_playlist playlistPtr, Session session) {
    synchronized (SpotifyJ.lock) {
      callbacks = new sp_playlist_callbacks();
      callbacks.tracks_added = new TracksAddedCallback();
      callbacks.tracks_removed = new TracksRemovedCallback();
      callbacks.tracks_moved = new TracksMovedCallback();
      callbacks.playlist_renamed = new PlaylistRenamedCallback();
      callbacks.playlist_state_changed = new PlaylistStateChangedCallback();
      callbacks.playlist_update_in_progress = new PlaylistUpdateInProgressCallback();
      callbacks.playlist_metadata_updated = new PlaylistMetadataUpdatedCallback();
      callbacks.track_created_changed = new TrackCreatedChangedCallback();
      callbacks.track_seen_changed = new TrackSeenChangedCallback();
      callbacks.description_changed = new DescriptionChangedCallback();
      callbacks.image_changed = new ImageChangedCallback();
      callbacks.track_message_changed = new TrackMessageChangedCallback();
      callbacks.subscribers_changed = new SubscribersChangedCallback();

      this.playlistPtr = playlistPtr;
      this.session = session;

      libspotify.sp_playlist_add_callbacks(playlistPtr, callbacks, 0);
      playlists.put(playlistPtr, this);
    }
  }

  public static Playlist create(Session session, Link link) {
    sp_playlist playlistPtr = libspotify.sp_playlist_create(session.sessionPtr, link.linkPtr);
    return (playlistPtr != null) ? new Playlist(playlistPtr, session) : null;
  }

  public int getTrackCount() {
    synchronized (SpotifyJ.lock) {
      return libspotify.sp_playlist_num_tracks(playlistPtr);
    }
  }

  public String getName() {
    synchronized (SpotifyJ.lock) {
      return libspotify.sp_playlist_name(playlistPtr);
    }
  }

  public int setName(String newName) {
    int error = Constants.ERROR_INVALID_DATA;

    if (newName != null && newName.trim().length() > 0 && newName.length() < 256) {
      synchronized (SpotifyJ.lock) {
        if (libspotify.sp_playlist_is_loaded(playlistPtr)) {
          error = libspotify.sp_playlist_rename(playlistPtr, newName);
        }
      }
    }

    return error;
  }

  public boolean isLoaded() {
    synchronized (SpotifyJ.lock) {
      return libspotify.sp_playlist_is_loaded(playlistPtr);
    }
  }

  public boolean hasPendingChanges() {
    synchronized (SpotifyJ.lock) {
      return libspotify.sp_playlist_has_pending_changes(playlistPtr);
    }
  }

  public boolean isCollaborative() {
    synchronized (SpotifyJ.lock) {
      return libspotify.sp_playlist_is_collaborative(playlistPtr);
    }
  }

  public void setIsCollaborative(boolean isCollaborative) {
    synchronized (SpotifyJ.lock) {
      libspotify.sp_playlist_set_collaborative(playlistPtr, isCollaborative);
    }
  }

  public User getOwner() {
    synchronized (SpotifyJ.lock) {
      return new User(libspotify.sp_playlist_owner(playlistPtr));
    }
  }

  public Track[] getTracks() {
    synchronized (SpotifyJ.lock) {
      Track[] tracks = new Track[getTrackCount()];

      for (int i = 0; i < getTrackCount(); i++) {
        tracks[i] = new Track(libspotify.sp_playlist_track(playlistPtr, i));
      }

      return tracks;
    }
  }

  public int addTracks(Track[] tracks, int position) {
    int result = Constants.ERROR_INVALID_DATA;

    if (tracks != null && tracks.length > 0) {
      synchronized (SpotifyJ.lock) {
        if (position < 0)
          position = 0;

        int numTracks = getTrackCount();

        if (position > numTracks)
          position = numTracks;

        sp_track[] trackPtrs = new sp_track[tracks.length];
        for (int i = 0; i < tracks.length; i++)
          trackPtrs[i] = tracks[i].trackPtr;

        result = libspotify.sp_playlist_add_tracks(playlistPtr, trackPtrs, trackPtrs.length, position, session.sessionPtr);
      }
    }

    return result;
  }

	/* Playlist callback functions */

  private class TracksAddedCallback implements Callback {
    public void callback(sp_playlist playlist, Pointer tracks, int numTracks, int position, int userdata) {
      System.out.println("Tracks added callback");
    }
  }

  private class TracksRemovedCallback implements Callback {
    public void callback(sp_playlist playlist, Pointer tracks, int numTracks, int userdata) {
      System.out.println("Tracks removed callback");
    }
  }

  private class TracksMovedCallback implements Callback {
    public void callback(sp_playlist playlist, Pointer tracks, int numTracks, int newPosition, int userdata) {
      System.out.println("Tracks moved callback");
    }
  }

  private class PlaylistRenamedCallback implements Callback {
    public void callback(sp_playlist playlist, int userdata) {
      System.out.println("Playlist renamed callback");
    }
  }

  private class PlaylistStateChangedCallback implements Callback {
    public void callback(sp_playlist playlist, int userdata) {
      System.out.println("Playlist state changed callback");
    }
  }

  private class PlaylistUpdateInProgressCallback implements Callback {
    public void callback(sp_playlist playlist, boolean done, int userdata) {
      System.out.println("Playlist update in progress callback");
    }
  }

  private class PlaylistMetadataUpdatedCallback implements Callback {
    public void callback(sp_playlist playlist, int userdata) {
      System.out.println("Playlist metadata updated callback");
    }
  }

  private class TrackCreatedChangedCallback implements Callback {
    public void callback(sp_playlist playlist, int position, sp_user user, int when, int userdata) {
      System.out.println("Track created changed callback");
    }
  }

  private class TrackSeenChangedCallback implements Callback {
    public void callback(sp_playlist playlist, int position, boolean seen, int userdata) {
      System.out.println("Track seen changed callback");
    }
  }

  private class DescriptionChangedCallback implements Callback {
    public void callback(sp_playlist playlist, String desc, int userdata) {
      System.out.println("Description changed callback");
    }
  }

  private class ImageChangedCallback implements Callback {
    public void callback(sp_playlist playlist, Pointer image, int userdata) {
      System.out.println("Image changed callback");
    }
  }

  private class TrackMessageChangedCallback implements Callback {
    public void callback(sp_playlist playlist, int position, String message, int userdata) {
      System.out.println("Track message changed callback");
    }
  }

  private class SubscribersChangedCallback implements Callback {
    public void callback(sp_playlist playlist, int userdata) {
      System.out.println("Subscribers changed callback");
    }
  }
	
	/* Event handler setters */

  public void setTracksAddedHandler(TracksEventHandler tracksAddedHandler) {
    this.tracksAddedHandler = tracksAddedHandler;
  }

  public void setTracksRemovedHandler(TracksEventHandler tracksRemovedHandler) {
    this.tracksRemovedHandler = tracksRemovedHandler;
  }

  public void setTracksMovedHandler(TracksEventHandler tracksMovedHandler) {
    this.tracksMovedHandler = tracksMovedHandler;
  }

  public void setPlaylistRenamedHandler(PlaylistEventHandler playlistRenamedHandler) {
    this.playlistRenamedHandler = playlistRenamedHandler;
  }

  public void setPlaylistStateChangedHandler(PlaylistEventHandler playlistStateChangedHandler) {
    this.playlistStateChangedHandler = playlistStateChangedHandler;
  }

  public void setPlaylistUpdateInProgressHandler(PlaylistEventHandler playlistUpdateInProgressHandler) {
    this.playlistUpdateInProgressHandler = playlistUpdateInProgressHandler;
  }

  public void setPlaylistMetadataUpdatedHandler(PlaylistEventHandler playlistMetadataUpdatedHandler) {
    this.playlistMetadataUpdatedHandler = playlistMetadataUpdatedHandler;
  }

  public void setTrackCreatedChangedHandler(TracksEventHandler trackCreatedChangedHandler) {
    this.trackCreatedChangedHandler = trackCreatedChangedHandler;
  }

  public void setTrackSeenChangedHandler(TracksEventHandler tracksSeenChangedHandler) {
    this.trackSeenChangedHandler = tracksSeenChangedHandler;
  }

  public void setDescriptionChangedHandler(PlaylistEventHandler descriptionChangedHandler) {
    this.descriptionChangedHandler = descriptionChangedHandler;
  }

  public void setImageChangedHandler(PlaylistEventHandler imageChangedHandler) {
    this.imageChangedHandler = imageChangedHandler;
  }

  public void setTrackMessageChangedHandler(TracksEventHandler trackMessageChangedHandler) {
    this.trackMessageChangedHandler = trackMessageChangedHandler;
  }

  private void setSubscribersChangedHandler(PlaylistEventHandler subscribersChangedHandler) {
    this.subscribersChangedHandler = subscribersChangedHandler;
  }

  protected void finalize() {
    playlists.remove(playlistPtr);

    if (playlistPtr != null) {
      synchronized (SpotifyJ.lock) {
        if (session != null && session.getConnectionState() == Constants.CONNECTIONSTATE_LOGGED_IN)
          libspotify.sp_playlist_remove_callbacks(playlistPtr, callbacks, 0);
        playlistPtr = null;
      }
    }
  }

}
