package libspotifyj;

import com.sun.jna.Memory;
import libspotifyj.low.SpotifyJ;
import libspotifyj.low.sp_link;

public class Link {

  private static SpotifyJ libspotify = SpotifyJ.libspotify;
  sp_link linkPtr;

  Link(sp_link linkPtr) {
    this.linkPtr = linkPtr;
    synchronized (SpotifyJ.lock) {
      libspotify.sp_link_add_ref(linkPtr);
    }
  }

  public static Link createLink(String link) {
    synchronized (SpotifyJ.lock) {
      sp_link newLinkPtr = libspotify.sp_link_create_from_string(link);
      return (newLinkPtr != null) ? new Link(newLinkPtr) : null;
    }
  }

  public int getLinkType() {
    synchronized (SpotifyJ.lock) {
      return libspotify.sp_link_type(linkPtr);
    }
  }

  public String toString() {
    String result = "";
    int bufferSize = 256;

    while (result.length() == 0) {
      int len = bufferSize;
      Memory buffer = new Memory(bufferSize);

      synchronized (SpotifyJ.lock) {
        len = libspotify.sp_link_as_string(linkPtr, buffer, bufferSize);
      }

      if (len < bufferSize)
        result = buffer.getString(0);
      else
        bufferSize *= 2;
    }

    return result;
  }

  protected void finalize() {
    if (linkPtr != null) {
      synchronized (SpotifyJ.lock) {
        libspotify.sp_link_release(linkPtr);
      }
    }
  }

}
