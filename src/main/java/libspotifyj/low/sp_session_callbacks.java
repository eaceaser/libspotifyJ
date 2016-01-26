package libspotifyj.low;

import com.sun.jna.Callback;
import com.sun.jna.Structure;

public class sp_session_callbacks extends Structure implements Structure.ByReference {
	
	public Callback logged_in;
	public Callback logged_out;
	public Callback metadata_updated;
	public Callback connection_error;
	public Callback message_to_user;
	public Callback notify_main_thread;
	public Callback music_delivery;
	public Callback play_token_lost;
	public Callback log_message;
	public Callback end_of_track;
	public Callback streaming_error;
	public Callback userinfo_updated;
	public Callback start_playback;
	public Callback stop_playback;
	public Callback get_audio_buffer_stats;
	public Callback offline_status_updated;
	public Callback offline_error;
	public Callback credentials_blob_updated;
	public Callback connectionstate_updated;
	public Callback scrobble_error;
	public Callback private_session_mode_changed;
	
}
