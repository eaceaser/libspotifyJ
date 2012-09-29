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
	
}
