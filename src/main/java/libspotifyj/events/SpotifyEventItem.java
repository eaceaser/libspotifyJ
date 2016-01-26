package libspotifyj.events;

import libspotifyj.Session;

public class SpotifyEventItem {

  private SpotifyEventHandler handler;
  private Session session;
  private SpotifyEventArgs eventArgs;

  public SpotifyEventItem(SpotifyEventHandler handler, Session session, SpotifyEventArgs eventArgs) {
    this.handler = handler;
    this.session = session;
    this.eventArgs = eventArgs;
  }

  public void processEvent() {
    if (handler != null)
      handler.process(session, eventArgs);
  }

}
