package low;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public interface SpotifyJ extends Library {

	public static final SpotifyJ libspotify = 
		(SpotifyJ) Native.loadLibrary("spotify", SpotifyJ.class);
	
	// Used in sp_session_config
	public static final int SPOTIFY_API_VERSION = 12;
	// Used internally to control access to the native library between threads
	public static final Object lock = new Object();

	// Error handling
	String sp_error_message(int error);
	
	// Session handling
	int sp_session_create(sp_session_config config, PointerByReference session);
	int sp_session_login(sp_session session, String username, String password, boolean remember_me, String blob);
	int sp_session_relogin(sp_session session);
	//sp_session_rememebered_user();
	String sp_session_user_name(sp_session session);
	int sp_session_forget_me(sp_session session);
	sp_user sp_session_user(sp_session session);
	int sp_session_logout(sp_session session);
	int sp_session_flush_caches(sp_session session);
	int sp_session_connectionstate(sp_session session);
	//sp_session_userdata();
	int sp_session_set_cache_size(sp_session session, NativeLong size);
	int sp_session_process_events(sp_session session, IntByReference next_timeout);
	//sp_session_player_load();
	//sp_session_player_seek();
	//sp_session_player_play();
	//sp_session_player_unload();
	//sp_session_player_prefetch();
	//sp_session_playlistcontainer(sp_session session);
	//sp_session_inbox_create(sp_session session);
	//sp_session_starred_create(sp_session session);
	//sp_session_starred_for_user_create(sp_session session, String canonical_username);
	//sp_session_publishedcontainer_for_user_create(sp_session session, String canonical_username);
	int sp_session_preferred_bitrate(sp_session session, int bitrate);
	int sp_session_preferred_offline_bitrate(sp_session session, int bitrate, boolean allow_resync);
	boolean sp_session_get_volume_normalization(sp_session session);
	int sp_session_set_volume_normalization(sp_session session, boolean on);
	int sp_session_set_private_session(sp_session session, boolean enabled);
	boolean sp_session_is_private_session(sp_session session);
	//sp_session_set_scrobbling();
	//sp_session_is_scrobbling();
	//sp_session_set_social_credentials();
	int sp_session_set_connection_type(sp_session session, int type);
	int sp_offline_tracks_to_sync(sp_session session);
	int sp_offline_num_playlists(sp_session session);
	//boolean sp_offline_sync_get_status();
	int sp_offline_time_left(sp_session session);
	int sp_session_user_country(sp_session session);
	
	// User handling
	String sp_user_canonical_name(sp_user user);
	String sp_user_display_name(sp_user user);
	
}
