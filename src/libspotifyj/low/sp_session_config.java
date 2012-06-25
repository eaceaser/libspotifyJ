package libspotifyj.low;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class sp_session_config extends Structure {
	
    public int api_version;
    public String cache_location;
    public String settings_location;
    public Pointer application_key;
    public int application_key_size;
    public String user_agent = "jspotify";
    public sp_session_callbacks callbacks;
    public Pointer userdata;
    public boolean compress_playlists;
    public boolean dont_save_metadata_for_playlists;
    public boolean initially_unload_playlists;
	public String device_id;
	public String proxy;
	public String proxy_username;
	public String tracefile;
	
}
