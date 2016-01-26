package libspotifyj;

import java.util.Formatter;

public class Util {

  static String imageIdToString(byte[] id) {
    if (id == null)
      return "";

    StringBuilder sb = new StringBuilder(id.length * 2);
    Formatter formatter = new Formatter(sb);

    for (byte b : id)
      formatter.format("%02x", b);

    return sb.toString();
  }

  static byte[] stringToImageId(String id) {
    if (id == null || id.length() != 40)
      return null;

    byte[] result = new byte[20];
    for (int i = 0; i < id.length(); i += 2) {
      result[i / 2] = (byte) ((Character.digit(id.charAt(i), 16) << 4)
          + Character.digit(id.charAt(i + 1), 16));
    }

    return result;
  }

}
