package libspotifyj.low;

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public interface SpotifyJ extends Library {

  public static final SpotifyJ libspotify =
      (SpotifyJ) Native.loadLibrary("spotify", SpotifyJ.class);

  /* Used in sp_session_config */
  public static final int SPOTIFY_API_VERSION = 12;
  /* Used internally to control access to the native library between threads */
  public static final Object lock = new Object();

  /* Error handling */
  String sp_error_message(int error);

  /* Session handling */
  int sp_session_create(sp_session_config config, PointerByReference session);

  int sp_session_login(sp_session session, String username, String password, boolean remember_me, String blob);

  int sp_session_relogin(sp_session session);

  int sp_session_rememebered_user(sp_session session, Memory buffer, int buffer_size);

  String sp_session_user_name(sp_session session);

  int sp_session_forget_me(sp_session session);

  sp_user sp_session_user(sp_session session);

  int sp_session_logout(sp_session session);

  int sp_session_flush_caches(sp_session session);

  int sp_session_connectionstate(sp_session session);

  //sp_session_userdata();
  int sp_session_set_cache_size(sp_session session, long size);

  int sp_session_process_events(sp_session session, IntByReference next_timeout);

  int sp_session_player_load(sp_session session, sp_track track);

  int sp_session_player_seek(sp_session session, int offset);

  int sp_session_player_play(sp_session session, boolean play);

  int sp_session_player_unload(sp_session session);

  int sp_session_player_prefetch(sp_session session, sp_track track);

  sp_playlistcontainer sp_session_playlistcontainer(sp_session session);

  sp_playlist sp_session_inbox_create(sp_session session);

  sp_playlist sp_session_starred_create(sp_session session);

  sp_playlist sp_session_starred_for_user_create(sp_session session, String canonical_username);

  sp_playlist sp_session_publishedcontainer_for_user_create(sp_session session, String canonical_username);

  int sp_session_preferred_bitrate(sp_session session, int bitrate);

  int sp_session_preferred_offline_bitrate(sp_session session, int bitrate, boolean allow_resync);

  boolean sp_session_get_volume_normalization(sp_session session);

  int sp_session_set_volume_normalization(sp_session session, boolean on);

  int sp_session_set_private_session(sp_session session, boolean enabled);

  boolean sp_session_is_private_session(sp_session session);

  int sp_session_set_scrobbling(sp_session session, int provider, int state);

  int sp_session_is_scrobbling(sp_session session, int provider, IntByReference state);

  int sp_session_set_social_credentials(sp_session session, int provider, String username, String password);

  int sp_session_set_connection_type(sp_session session, int type);

  int sp_session_set_connection_rules(sp_session session, int rules);

  int sp_offline_tracks_to_sync(sp_session session);

  int sp_offline_num_playlists(sp_session session);

  //boolean sp_offline_sync_get_status();
  int sp_offline_time_left(sp_session session);

  int sp_session_user_country(sp_session session);

  /* Link handling */
  sp_link sp_link_create_from_string(String link);

  sp_link sp_link_create_from_track(sp_track track, int offset);

  sp_link sp_link_create_from_album(sp_album album);

  sp_link sp_link_create_from_album_cover(sp_album album, int size);

  sp_link sp_link_create_from_artist(sp_artist artist);

  sp_link sp_link_create_from_artist_portrait(sp_artist artist, int size);

  sp_link sp_link_create_from_artistbrowse_portrait(sp_artistbrowse arb, int index);

  sp_link sp_link_create_from_search(sp_search search);

  sp_link sp_link_create_from_playlist(sp_playlist playlist);

  sp_link sp_link_create_from_user(sp_user user);

  sp_link sp_link_create_from_image(sp_image image);

  int sp_link_as_string(sp_link link, Memory buffer, int buffer_size);

  int sp_link_type(sp_link link);

  sp_track sp_link_as_track(sp_link link);

  sp_track sp_link_as_track_and_offset(sp_link link, IntByReference offset);

  sp_album sp_link_as_album(sp_link link);

  sp_artist sp_link_as_artist(sp_link link);

  sp_user sp_link_as_user(sp_link link);

  int sp_link_add_ref(sp_link link);

  int sp_link_release(sp_link link);

  /* Track handling */
  boolean sp_track_is_loaded(sp_track track);

  int sp_track_error(sp_track track);

  int sp_track_offline_get_status(sp_track track);

  int sp_track_get_availability(sp_session session, sp_track track);

  boolean sp_track_is_local(sp_session session, sp_track track);

  boolean sp_track_is_autolinked(sp_session session, sp_track track);

  sp_track sp_track_get_playable(sp_session session, sp_track track);

  boolean sp_track_is_placeholder(sp_track track);

  boolean sp_track_is_starred(sp_session session, sp_track track);

  //FIXME: the below might be wrong
  int sp_track_set_starred(sp_session session, sp_track[] tracks, int num_tracks, boolean star);

  int sp_track_num_artists(sp_track track);

  sp_artist sp_track_artist(sp_track track, int index);

  sp_album sp_track_album(sp_track track);

  String sp_track_name(sp_track track);

  int sp_track_duration(sp_track track);

  int sp_track_popularity(sp_track track);

  int sp_track_disc(sp_track track);

  int sp_track_index(sp_track track);

  sp_track sp_localtrack_create(String artist, String title, String album, int length);

  int sp_track_add_ref(sp_track track);

  int sp_track_release(sp_track track);

  /* User handling */
  String sp_user_canonical_name(sp_user user);

  String sp_user_display_name(sp_user user);

  boolean sp_user_is_loaded(sp_user user);

  void sp_user_add_ref(sp_user user);

  void sp_user_release(sp_user user);

  /* Album handling */
  boolean sp_album_is_loaded(sp_album album);

  boolean sp_album_is_available(sp_album album);

  sp_artist sp_album_artist(sp_album album);

  Pointer sp_album_cover(sp_album album);

  String sp_album_name(sp_album album);

  int sp_album_year(sp_album album);

  int sp_album_type(sp_album album);

  void sp_album_add_ref(sp_album album);

  void sp_album_release(sp_album album);

  /* Artist handling */
  String sp_artist_name(sp_artist artist);

  boolean sp_artist_is_loaded(sp_artist artist);

  void sp_artist_add_ref(sp_artist artist);

  void sp_artist_release(sp_artist artist);

  /* Album browse handling */
  sp_albumbrowse sp_albumbrowse_create(sp_session session, sp_album album, Callback callback, int user_data);

  boolean sp_albumbrowse_is_loaded(sp_albumbrowse alb);

  int sp_albumbrowse_error(sp_albumbrowse alb);

  sp_album sp_albumbrowse_album(sp_albumbrowse alb);

  sp_artist sp_albumbrowse_artist(sp_albumbrowse alb);

  int sp_albumbrowse_num_copyrights(sp_albumbrowse alb);

  String sp_albumbrowse_copyright(sp_albumbrowse alb, int index);

  int sp_albumbrowse_num_tracks(sp_albumbrowse alb);

  sp_track sp_albumbrowse_track(sp_albumbrowse alb, int index);

  String sp_albumbrowse_review(sp_albumbrowse alb);

  int sp_albumbrowse_backend_request_duration(sp_albumbrowse alb);

  void sp_albumbrowse_add_ref(sp_albumbrowse alb);

  void sp_albumbrowse_release(sp_albumbrowse alb);

  /* Artist browse handling */
  sp_artistbrowse sp_artistbrowse_create(sp_session session, sp_artist artist, int type, Callback callback, int userdata);

  boolean sp_artistbrowse_is_loaded(sp_artistbrowse arb);

  int sp_artistbrowse_error(sp_artistbrowse arb);

  sp_artist sp_artistbrowse_artist(sp_artistbrowse arb);

  int sp_artistbrowse_num_portraits(sp_artistbrowse arb);

  Pointer sp_artistbrowse_portrait(sp_artistbrowse arb, int index);

  int sp_artistbrowse_num_tracks(sp_artistbrowse arb);

  sp_track sp_artistbrowse_track(sp_artistbrowse arb, int index);

  int sp_artistbrowse_num_tophit_tracks(sp_artistbrowse arb);

  sp_track sp_artistbrowse_tophit_track(sp_artistbrowse arb, int index);

  int sp_artistbrowse_num_albums(sp_artistbrowse arb);

  sp_album sp_artistbrowse_album(sp_artistbrowse arb, int index);

  int sp_artistbrowse_num_similar_artists(sp_artistbrowse arb);

  sp_artist sp_artistbrowse_similar_artist(sp_artistbrowse arb, int index);

  String sp_artistbrowse_biography(sp_artistbrowse arb);

  int sp_artistbrowse_backend_request_duration(sp_artistbrowse arb);

  void sp_artistbrowse_add_ref(sp_artistbrowse arb);

  void sp_artistbrowse_release(sp_artistbrowse arb);

  /* Image handling */
  sp_image sp_image_create(sp_session session, byte[] image_id);

  sp_image sp_image_create_from_link(sp_session session, sp_link l);

  void sp_image_add_load_callback(sp_image image, Callback callback, int userdata);

  boolean sp_image_is_loaded(sp_image image);

  int sp_image_error(sp_image image);

  Pointer sp_image_data(sp_image image, IntByReference data_size);

  void sp_image_add_ref(sp_image image);

  void sp_image_release(sp_image image);

  /* Search handling */
  sp_search sp_search_create(sp_session session, String query, int track_offset, int track_count, int album_offset, int album_count, int artist_offset, int artist_count, int playlist_offset, int playlist_count, int search_type, Callback callback, int userdata);

  boolean sp_search_is_loaded(sp_search search);

  int sp_search_error(sp_search search);

  int sp_search_num_tracks(sp_search search);

  sp_track sp_search_track(sp_search search, int index);

  int sp_search_num_albums(sp_search search);

  sp_album sp_search_album(sp_search search, int index);

  int sp_search_num_playlists(sp_search search);

  String sp_search_playlist_name(sp_search search, int index);

  String sp_search_playlist_uri(sp_search search, int index);

  String sp_search_playlist_image_uri(sp_search search, int index);

  int sp_search_num_artists(sp_search search);

  sp_artist sp_search_artist(sp_search search, int index);

  String sp_search_query(sp_search search);

  String sp_search_did_you_mean(sp_search search);

  int sp_search_total_tracks(sp_search search);

  int sp_search_total_albums(sp_search search);

  int sp_search_total_artists(sp_search search);

  int sp_search_total_playlists(sp_search search);

  void sp_search_add_ref(sp_search search);

  void sp_search_release(sp_search search);

  /* Playlist handling */
  boolean sp_playlist_is_loaded(sp_playlist playlist);

  int sp_playlist_add_callbacks(sp_playlist playlist, sp_playlist_callbacks callbacks, int userdata);

  int sp_playlist_remove_callbacks(sp_playlist playlist, sp_playlist_callbacks callbacks, int userdata);

  int sp_playlist_num_tracks(sp_playlist playlist);

  sp_track sp_playlist_track(sp_playlist playlist, int index);

  int sp_playlist_create_time(sp_playlist playlist, int index);

  sp_user sp_playlist_track_creator(sp_playlist playlist, int index);

  boolean sp_playlist_track_seen(sp_playlist playlist, int index);

  int sp_playlist_set_seen(sp_playlist playlist, int index, boolean seen);

  String sp_playlist_track_message(sp_playlist playlist, int index);

  String sp_playlist_name(sp_playlist playlist);

  int sp_playlist_rename(sp_playlist playlist, String new_name);

  sp_user sp_playlist_owner(sp_playlist playlist);

  boolean sp_playlist_is_collaborative(sp_playlist playlist);

  int sp_playlist_set_collaborative(sp_playlist playlist, boolean collaborative);

  int sp_playlist_set_autolink_tracks(sp_playlist playlist, boolean link);

  String sp_playlist_get_description(sp_playlist playlist);

  boolean sp_playlist_get_image(sp_playlist playlist, byte[] image);

  boolean sp_playlist_has_pending_changes(sp_playlist playlist);

  int sp_playlist_add_tracks(sp_playlist playlist, sp_track[] tracks, int num_tracks, int position, sp_session session);

  int sp_playlist_remove_tracks(sp_playlist playlist, int[] tracks, int num_tracks);

  int sp_playlist_reorder_tracks(sp_playlist playlist, int[] tracks, int num_tracks, int new_position);

  int sp_playlist_num_subscribers(sp_playlist playlist);

  sp_subscribers sp_playlist_subscribers(sp_playlist playlist);

  int sp_playlist_subscribers_free(sp_subscribers subscribers);

  int sp_playlist_update_subscribers(sp_session session, sp_playlist playlist);

  boolean sp_playlist_is_in_ram(sp_session session, sp_playlist playlist);

  int sp_playlist_set_in_ram(sp_session session, sp_playlist playlist, boolean in_ram);

  sp_playlist sp_playlist_create(sp_session session, sp_link link);

  int sp_playlist_set_offline_mode(sp_session session, sp_playlist playlist, boolean offline);

  int sp_playlist_get_offline_status(sp_session session, sp_playlist playlist);

  int sp_playlist_get_offline_download_completed(sp_session session, sp_playlist playlist);

  int sp_playlist_add_ref(sp_playlist playlist);

  int sp_playlist_release(sp_playlist playlist);

  /* Playlist container handling */
  int sp_playlistcontainer_add_callbacks(sp_playlistcontainer pc, sp_playlistcontainer_callbacks callbacks, int userdata);

  int sp_playlistcontainer_num_playlists(sp_playlistcontainer pc);

  boolean sp_playlistcontainer_is_loaded(sp_playlistcontainer pc);

  sp_playlist sp_playlistcontainer_playlist(sp_playlistcontainer pc, int index);

  int sp_playlistcontainer_playlist_type(sp_playlistcontainer pc, int index);

  int sp_playlistcontainer_folder_name(sp_playlistcontainer pc, int index, Memory buffer, int buffer_size);

  long sp_playlistcontainer_playlist_folder_id(sp_playlistcontainer pc, int index);

  sp_playlist sp_playlistcontainer_add_new_playlist(sp_playlistcontainer pc, String name);

  sp_playlist sp_playlistcontainer_add_playlist(sp_playlistcontainer pc, sp_link link);

  int sp_playlistcontainer_remove_playlist(sp_playlistcontainer pc, int index);

  int sp_playlistcontainer_move_playlist(sp_playlistcontainer pc, int index, int new_position, boolean dry_run);

  int sp_playlistcontainer_add_folder(sp_playlistcontainer pc, int index, String name);

  sp_user sp_playlistcontainer_owner(sp_playlistcontainer pc);

  int sp_playlistcontainer_add_ref(sp_playlistcontainer pc);

  int sp_playlistcontainer_release(sp_playlistcontainer pc);

  int sp_playlistcontainer_get_unseen_tracks(sp_playlistcontainer pc, sp_playlist playlist, Pointer tracks, int num_tracks);

  int sp_playlistcontainer_clear_unseen_tracks(sp_playlistcontainer pc, sp_playlist playlist);

}
