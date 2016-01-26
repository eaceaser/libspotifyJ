package libspotifyj.low;

import com.sun.jna.Callback;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

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

  protected List getFieldOrder() {
    return Arrays.asList(new String[] {
        "logged_in", "logged_out", "metadata_updated", "connection_error",
        "message_to_user", "notify_main_thread", "music_delivery", "play_token_lost",
        "log_message", "end_of_track", "streaming_error", "userinfo_updated",
        "start_playback", "stop_playback", "get_audio_buffer_stats", "offline_status_updated",
        "offline_error", "credentials_blob_updated", "connectionstate_updated",
        "scrobble_error", "private_mode_session_changed"
    });
  }
}
