package libspotifyj.events;

import libspotifyj.Playlist;

public abstract class TracksEventHandler {

  public abstract void process(Playlist playlist, TracksEventArgs eventArgs);

}
