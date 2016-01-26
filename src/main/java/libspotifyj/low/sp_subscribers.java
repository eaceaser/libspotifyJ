package libspotifyj.low;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class sp_subscribers extends Structure {

  public int count;
  public Pointer subscribers;

  protected List getFieldOrder() {
    return Arrays.asList(new String[] {
        "count", "subscribers"
    });
  }
}
