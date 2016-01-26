package libspotifyj.low;

import com.sun.jna.Structure;

public class sp_audioformat extends Structure {

  public int sample_type;
  public int sample_rate;
  public int channels;

}
