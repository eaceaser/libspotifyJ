package libspotifyj.events;

public class MusicDeliveryEventArgs implements SpotifyEventArgs {

  private int channels;
  private int rate;
  private byte[] samples;
  private int frames;
  private int consumedFrames;

  public MusicDeliveryEventArgs(int channels, int rate, byte[] samples, int frames) {
    this.channels = channels;
    this.rate = rate;
    this.samples = samples;
    this.frames = frames;
    this.consumedFrames = 0;
  }

  public int getChannels() {
    return channels;
  }

  public int getRate() {
    return rate;
  }

  public byte[] getSamples() {
    return samples;
  }

  public int getFrames() {
    return frames;
  }

  public int getConsumedFrames() {
    return consumedFrames;
  }

  public void setConsumedFrames(int consumedFrames) {
    this.consumedFrames = consumedFrames;
  }

}
