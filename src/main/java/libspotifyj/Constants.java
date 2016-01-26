package libspotifyj;

public final class Constants {

  /* Corresponds to sp_error */
  public static final int ERROR_OK = 0;
  public static final int ERROR_BAD_API_VERSION = 1;
  public static final int ERROR_API_INITIALIZATION_FAILED = 2;
  public static final int ERROR_TRACK_NOT_AVAILABLE = 3;
  public static final int ERROR_RESOURCE_NOT_LOADED = 4;
  public static final int ERROR_APPLICATION_KEY = 5;
  public static final int ERROR_BAD_USERNAME_OR_PASSWORD = 6;
  public static final int ERROR_USER_BANNED = 7;
  public static final int ERROR_UNABLE_TO_CONTACT_SERVER = 8;
  public static final int ERROR_CLIENT_TOO_OLD = 9;
  public static final int ERROR_OTHER_PERMANENT = 10;
  public static final int ERROR_BAD_USER_AGENT = 11;
  public static final int ERROR_MISSING_CALLBACK = 12;
  public static final int ERROR_INVALID_DATA = 13;
  public static final int ERROR_INDEX_OUT_OF_RANGE = 14;
  public static final int ERROR_USER_NEEDS_PREMIUM = 15;
  public static final int ERROR_OTHER_TRANSIENT = 16;
  public static final int ERROR_IS_LOADING = 17;
  public static final int ERROR_NO_STREAM_AVAILABLE = 18;
  public static final int ERROR_PERMISSION_DENIED = 19;
  public static final int ERROR_INBOX_IS_FULL = 20;
  public static final int ERROR_NO_CACHE = 21;
  public static final int ERROR_NO_SUCH_USER = 22;
  public static final int ERROR_NO_CREDENTIALS = 23;
  public static final int ERROR_NETWORK_DISABLED = 24;
  public static final int ERROR_INVALID_DEVICE_ID = 25;
  public static final int ERROR_CANT_OPEN_TRACE_FILE = 26;
  public static final int ERROR_APPLICATION_BANNED = 27;
  public static final int ERROR_OFFLINE_TOO_MANY_TRACKS = 31;
  public static final int ERROR_OFFLINE_DISK_CACHE = 32;
  public static final int ERROR_OFFLINE_EXPIRED = 33;
  public static final int ERROR_OFFLINE_NOT_ALLOWED = 34;
  public static final int ERROR_OFFLINE_LICENSE_LOST = 35;
  public static final int ERROR_OFLINE_LICENSE_ERROR = 36;
  public static final int ERROR_LASTFM_AUTH_ERROR = 39;
  public static final int ERROR_INVALID_ARGUMENT = 40;
  public static final int ERROR_SYSTEM_FAILURE = 41;

  /* Corresponds to sp_connectionstate */
  public static final int CONNECTIONSTATE_LOGGED_OUT = 0;
  public static final int CONNECTIONSTATE_LOGGED_IN = 1;
  public static final int CONNECTIONSTATE_DISCONNECTED = 2;
  public static final int CONNECTIONSTATE_UNDEFINED = 3;
  public static final int CONNECTIONSTATE_OFFLINE = 4;

  /* Corresponds to sp_bitrate */
  public static final int BITRATE_160k = 0;
  public static final int BITRATE_320k = 1;
  public static final int BITRATE_96k = 2;

  /* Corresponds to sp_connection_type */
  public static final int CONNECTION_TYPE_UNKNOWN = 0;
  public static final int CONNECTION_TYPE_NONE = 1;
  public static final int CONNECTION_TYPE_MOBILE = 2;
  public static final int CONNECTION_TYPE_MOBILE_ROAMING = 3;
  public static final int CONNECTION_TYPE_WIFI = 4;
  public static final int CONNECTION_TYPE_WIRED = 5;

  /* Corresponds to sp_connection_rules */
  public static final int CONNECTION_RULES_NETWORK = (0x1);
  public static final int CONNECTION_RULES_NETWORK_IF_ROAMIN = (0x2);
  public static final int CONNECTION_RULES_ALLOW_SYNC_OVER_MOBILE = (0x4);
  public static final int CONNECTION_RULES_ALLOW_SYNC_OVER_WIFE = (0x8);

  /* Corresponds to sp_linktype */
  public static final int LINKTYPE_INVALID = 0;
  public static final int LINKTYPE_TRACK = 1;
  public static final int LINKTYPE_ALBUM = 2;
  public static final int LINKTYPE_ARTIST = 3;
  public static final int LINKTYPE_SEARCH = 4;
  public static final int LINKTYPE_PLAYLIST = 5;
  public static final int LINKTYPE_PROFILE = 6;
  public static final int LINKTYPE_STARRED = 7;
  public static final int LINKTYPE_LOCALTRACK = 8;
  public static final int LINKTYPE_IMAGE = 9;

  /* Corresponds to sp_image_size */
  public static final int IMAGE_SIZE_NORMAL = 0;
  public static final int IMAGE_SIZE_SMALL = 1;
  public static final int IMAGE_SIZE_LARGE = 2;

  /* Corresponds to sp_track_offline_status */
  public static final int TRACK_OFFLINE_NO = 0;
  public static final int TRACK_OFFLINE_WAITING = 1;
  public static final int TRACK_OFFLINE_DOWNLOADING = 2;
  public static final int TRACK_OFFLINE_DONE = 3;
  public static final int TRACK_OFFLINE_ERROR = 4;
  public static final int TRACK_OFFLINE_DONE_EXPIRED = 5;
  public static final int TRACK_OFFLINE_LIMIT_EXPIRED = 6;
  public static final int TRACK_OFFLINE_DONE_RESYNC = 7;

  /* Corresponds to sp_track_availability */
  public static final int TRACK_AVAILABILITY_UNAVAILABLE = 0;
  public static final int TRACK_AVAILABILITY_AVAILABLE = 1;
  public static final int TRACK_AVAILABILITY_NOT_STREAMABLE = 2;
  public static final int TRACK_AVAILABILITY_BANNED_BY_ARTIST = 3;

  /* Corresponds to sp_albumtype */
  public static final int ALBUMTYPE_ALBUM = 0;
  public static final int ALBUMTYPE_SINGLE = 1;
  public static final int ALBUMTYPE_COMPILATION = 2;
  public static final int ALBUMTYPE_UNKNOWN = 3;

  /* Corresponds to sp_search_type */
  public static final int SEARCH_STANDARD = 0;
  public static final int SEARCH_SUGGEST = 1;

  /* Corresponds to sp_artistbrowse_type */
  public static final int ARTISTBROWSE_FULL = 0;
  public static final int ARTISTBROWSE_NO_TRACKS = 1;
  public static final int ARTISTBROWSE_NO_ALBUMS = 2;

  /* Corresponds to sp_imageformat */
  public static final int IMAGE_FORMAT_UNKNOWN = -1;
  public static final int IMAGE_FORMAT_JPEG = 0;

  /* Corresponds to sp_social_provider */
  public static final int SOCIAL_PROVIDER_SPOTIFY = 0;
  public static final int SOCIAL_PROVIDER_FACEBOOK = 1;
  public static final int SOCIAL_PROVIDER_LASTFM = 2;

  /* Corresponds to sp_scrobbling_state */
  public static final int SCROBBLING_STATE_USE_GLOBAL_SETTING = 0;
  public static final int SCROBBLING_STATE_LOCAL_ENABLED = 1;
  public static final int SCROBBLING_STATE_LOCAL_DISABLED = 2;
  public static final int SCROBBLING_STATE_GLOBAL_ENABLED = 3;
  public static final int SCROBBLING_STATE_GLOBAL_DISABLED = 4;

  /* Corresponds to sp_playlist_offline_status */
  public static final int PLAYLIST_OFFLINE_STATUS_NO = 0;
  public static final int PLAYLIST_OFFLINE_STATUS_YES = 1;
  public static final int PLAYLIST_OFFLINE_STATUS_DOWNLOADING = 2;
  public static final int PLAYLIST_OFFLINE_STATUS_WAITING = 3;

  /* Corresponds to sp_playlist_type */
  public static final int PLAYLIST_TYPE_PLAYLIST = 0;
  public static final int PLAYLIST_TYPE_START_FOLDER = 1;
  public static final int PLAYLIST_TYPE_END_FOLDER = 2;
  public static final int PLAYLIST_TYPE_PLACEHOLDER = 3;

  private Constants() {
  }

}
