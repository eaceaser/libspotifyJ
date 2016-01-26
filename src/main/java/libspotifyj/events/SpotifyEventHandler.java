package libspotifyj.events;

import libspotifyj.Session;

public interface SpotifyEventHandler {
  public void process(Session session, SpotifyEventArgs eventArgs);
}
