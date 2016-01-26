package libspotifyj.events;

public class PlaylistEventArgs implements SpotifyEventArgs {

  private boolean done;
  private String description;
  byte[] image;

  public PlaylistEventArgs() {
    this.done = true;
  }

  public PlaylistEventArgs(boolean done) {
    this.done = done;
  }

  public PlaylistEventArgs(String description) {
    this.description = description;
  }

  public PlaylistEventArgs(byte[] image) {
    this.image = image;
  }

  public String getDescription() {
    return description;
  }

  public boolean getDone() {
    return done;
  }

  public byte[] getImage() {
    return image;
  }

}
