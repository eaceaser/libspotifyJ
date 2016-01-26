package libspotifyj.low;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class sp_audio_buffer_stats extends Structure {

  public int samples;
  public int stutter;

  protected List getFieldOrder() {
    return Arrays.asList(new String[] {
        "samples", "stutter"
    });
  }
}
