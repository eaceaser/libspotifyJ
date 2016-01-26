package libspotifyj.low;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class sp_audioformat extends Structure {

  public int sample_type;
  public int sample_rate;
  public int channels;

  protected List getFieldOrder() {
    return Arrays.asList(new String[] {
        "sample_type", "sample_rate", "channels"
    });
  }
}
