package libspotifyj;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;

import libspotifyj.events.MusicDeliveryEventArgs;
import libspotifyj.events.SessionEventArgs;
import libspotifyj.events.SessionEventHandler;
import libspotifyj.events.SpotifyEventHandler;
import libspotifyj.events.SpotifyEventItem;
import libspotifyj.low.SpotifyJ;
import libspotifyj.low.sp_albumbrowse;
import libspotifyj.low.sp_artistbrowse;
import libspotifyj.low.sp_audioformat;
import libspotifyj.low.sp_image;
import libspotifyj.low.sp_search;
import libspotifyj.low.sp_session;
import libspotifyj.low.sp_session_callbacks;
import libspotifyj.low.sp_session_config;

import com.sun.jna.Callback;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public class Session {
	
	/* Provides access to libspotify API */
	private static SpotifyJ libspotify = SpotifyJ.libspotify;
	
	/* Session data */
	private static Map<sp_session, Session> sessions = new HashMap<sp_session, Session>();
	sp_session sessionPtr = new sp_session(Pointer.NULL);
	
	/* Login data */
	private Lock loginLock = new ReentrantLock();
	private Condition userLoggedIn;
	private int loginCode = Constants.ERROR_IS_LOADING;
	
	/* Logout data */
	private Lock logoutLock = new ReentrantLock();
	private Condition userLoggedOut;
	
	/* Main thread data */
	private Thread mainThread;
	private Lock mainThreadLock = new ReentrantLock();
	private Condition mainThreadShouldProcess = mainThreadLock.newCondition();
	
	/* Event thread data */
	private Thread eventThread;
	private Lock eventThreadLock = new ReentrantLock();
	private Condition eventThreadShouldProcess = eventThreadLock.newCondition();
	private Queue<SpotifyEventItem> eventItemQueue = new LinkedList<SpotifyEventItem>();
	
	/* Spotify event handlers */
	private SpotifyEventHandler logMessageHandler;
	private SpotifyEventHandler loginHandler;
	private SpotifyEventHandler logoutHandler;
	private SpotifyEventHandler metaUpdatedHandler;
	private SpotifyEventHandler connectionErrorHandler;
	private SpotifyEventHandler messageToUserHandler;
	private SpotifyEventHandler playTokenLostHandler;
	private SpotifyEventHandler musicDeliveryHandler;
	private SpotifyEventHandler endOfTrackHandler;
	private SpotifyEventHandler streamingErrorHandler;
	private SpotifyEventHandler userinfoUpdatedHandler;
	private SpotifyEventHandler startPlaybackHandler;
	private SpotifyEventHandler stopPlaybackHandler;
	private SpotifyEventHandler getAudioBufferStatsPlaybackHandler;
	private SpotifyEventHandler offlineStatusUpdatesHandler;
	private SpotifyEventHandler offlineErrorHandler;
	private SpotifyEventHandler credentialsBlobUpdatedHandler;
	private SpotifyEventHandler connectionStateUpdatedHandler;
	private SpotifyEventHandler scrobbleErrorHandler;
	private SpotifyEventHandler privateSessionModeChangedHandler;
	
	/* State data */
	private HashMap<Integer, Object> states = new HashMap<Integer, Object>();
	private short internalStateCounter = 1;
	private short userStateCounter = 1;
	
	private Session(char[] applicationKey, String cacheLocation, String settingsLocation, String userAgent) throws SpotifyException {
		sp_session_config config = new sp_session_config();
		sp_session_callbacks callbacks = new sp_session_callbacks();
		
		synchronized (SpotifyJ.lock) {
			callbacks.logged_in = new LoggedInCallBack();
			callbacks.logged_out = new LoggedOutCallback();
			callbacks.metadata_updated = new MetadataUpdatedCallback();
			callbacks.connection_error = new ConnectionErrorCallback();
			callbacks.message_to_user = new MessageToUserCallback();
			callbacks.notify_main_thread = new NotifyMainThreadCallback();
			callbacks.music_delivery = new MusicDeliveryCallback();
			callbacks.play_token_lost = new PlayTokenLostCallback();
			callbacks.log_message = new LogMessageCallback();
			callbacks.end_of_track = new EndOfTrackCallback();
			callbacks.streaming_error = new StreamingErrorCallback();
			callbacks.userinfo_updated = new UserInfoUpdatedCallback();
			callbacks.start_playback = new StartPlaybackCallback();
			callbacks.stop_playback = new StopPlaybackCallback();
			callbacks.get_audio_buffer_stats = new GetAudioBufferStatsPlayback();
			callbacks.offline_status_updated = new OfflineStatusUpdatesCallback();
			callbacks.offline_error = new OfflineErrorCallback();
			callbacks.credentials_blob_updated = new CredentialsBlobUpdatedCallback();
			callbacks.connectionstate_updated = new ConnectionStateUpdatedCallback();
			callbacks.scrobble_error = new ScrobbleErrorCallback();
			callbacks.private_session_mode_changed = new PrivateSessionModeChangedCallback();
		}
		
		PointerByReference sessionPbr = new PointerByReference();
        Pointer applicationKeyPtr = new Memory(applicationKey.length);
        applicationKeyPtr.write(0, toBytes(applicationKey), 0, applicationKey.length);
        
		config.api_version = SpotifyJ.SPOTIFY_API_VERSION;
		config.cache_location = cacheLocation;
		config.settings_location = settingsLocation;
		config.user_agent = userAgent;
	    config.application_key = applicationKeyPtr;
	    config.application_key_size = applicationKey.length;
		config.callbacks = callbacks;
        
        int code = libspotify.sp_session_create(config, sessionPbr);
        
        if (code != Constants.ERROR_OK) {
        	throw new SpotifyException(code);
        }
        
        sessionPtr = new sp_session(sessionPbr.getValue());
        libspotify.sp_session_process_events(sessionPtr, new IntByReference());
                                        
        mainThread = new MainThread();
        mainThread.start();
        eventThread = new EventThread();
        eventThread.start();
	}
	
	public static Session createInstance(char[] applicationKey, String cacheLocation, String settingsLocation, String userAgent) throws SpotifyException {
		synchronized (SpotifyJ.lock) {
			if (sessions.size() > 0) {
				throw new IllegalStateException("Only one instance of a Spotify session is allowed");
			} else {
				Session session = new Session(applicationKey, cacheLocation, settingsLocation, userAgent);
				sessions.put(session.sessionPtr, session);
				return session;
			}
		}
	}
	
    public int getConnectionState() {
    	if (sessionPtr != null) {
    		synchronized (SpotifyJ.lock) {
    			return libspotify.sp_session_connectionstate(sessionPtr);
    		}
    	} else {
    		return Constants.CONNECTIONSTATE_UNDEFINED;
    	}
    }
    
    public User getCurrentUser() {
    	synchronized (SpotifyJ.lock) {
    		return new User(libspotify.sp_session_user(sessionPtr));
    	}
    }
	
    public int login(String username, String password, long ms) {
    	if (userLoggedIn != null) {
    		return Constants.ERROR_IS_LOADING;
    	} else {
    		int code = Constants.ERROR_OK;
    		synchronized (SpotifyJ.lock) {
    			code = libspotify.sp_session_login(sessionPtr, username, password, false, null);
    		}
    		if (code != Constants.ERROR_OK)
    			return code;
    		else
    			userLoggedIn = loginLock.newCondition();
    	}
    	
    	boolean wasInterrupted = false;

    	try {
        	loginLock.lock();
        	wasInterrupted = userLoggedIn.await(ms, TimeUnit.MILLISECONDS);
    		userLoggedIn = null;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			loginLock.unlock();
		}
		
		return wasInterrupted ? loginCode : Constants.ERROR_OTHER_TRANSIENT;
    }
    
    public int logout(long ms) {
    	synchronized (SpotifyJ.lock) {
    		if (getConnectionState() == Constants.CONNECTIONSTATE_LOGGED_IN) {
    			int code = libspotify.sp_session_logout(sessionPtr);
    			if (code != Constants.ERROR_OK)
    				return code;
    			else
        			userLoggedOut = logoutLock.newCondition();
    		} else {
    			return Constants.ERROR_OTHER_TRANSIENT;
    		}
    	}
    	
    	boolean wasInterrupted = false;
    	    	
    	try {
    		logoutLock.lock();
    		wasInterrupted = userLoggedOut.await(ms, TimeUnit.MILLISECONDS);
    		userLoggedOut = null;
    	} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
    	} finally {
    		logoutLock.unlock();
    	}
    	
    	return wasInterrupted ? Constants.ERROR_OK : Constants.ERROR_OTHER_TRANSIENT;
    }
    
    public Search search(String query, int trackOffset, int trackCount, int albumOffset, int albumCount, int artistOffset, int artistCount, int playlistOffset, int playlistCount, int searchType, long timeout) {
    	ReentrantLock lock = new ReentrantLock();
    	Condition waitHandler = lock.newCondition();
    	SyncResponseData data = new SyncResponseData(lock, waitHandler);
    	    	
    	int id = getInternalStateId();
    	
    	synchronized (SpotifyJ.lock) {
    		states.put(id, data);
    		
    		if (libspotify.sp_search_create(sessionPtr, query, trackOffset, trackCount, 
    				albumOffset, albumCount, artistOffset, artistCount, 
    				playlistOffset, playlistCount, searchType, 
    				new SearchCompleteCallback(), id) == null) {
    			states.remove(id);
    			return null;
    		}
    	}
    	
    	Search search = (Search) getSyncResponse(id, data, timeout);
    	return search;
    }
    
    public AlbumBrowse browseAlbum(Album album, long timeout) {
    	ReentrantLock lock = new ReentrantLock();
    	Condition waitHandler = lock.newCondition();
    	SyncResponseData data = new SyncResponseData(lock, waitHandler);
    	
    	int id = getInternalStateId();
    	
    	synchronized (SpotifyJ.lock) {
    		states.put(id, data);
    		
    		if (libspotify.sp_albumbrowse_create(sessionPtr, album.albumPtr, 
    				new AlbumBrowseCompleteCallback(), id) == null) {
    			states.remove(id);
    			return null;
    		}
    	}
    	
    	AlbumBrowse albumBrowse = (AlbumBrowse) getSyncResponse(id, data, timeout);
    	return albumBrowse;
    }
    
    public ArtistBrowse browseArtist(Artist artist, int browseType, long timeout) {
    	ReentrantLock lock = new ReentrantLock();
    	Condition waitHandler = lock.newCondition();
    	SyncResponseData data = new SyncResponseData(lock, waitHandler);
    	
    	int id = getInternalStateId();
    	
    	synchronized (SpotifyJ.lock) {
    		states.put(id, data);
    		
    		if (libspotify.sp_artistbrowse_create(sessionPtr, artist.artistPtr, browseType,
    				new ArtistBrowseCompleteCallback(), id) == null) {
    			states.remove(id);
    			return null;
    		}
    	}
    	
    	ArtistBrowse artistBrowse = (ArtistBrowse) getSyncResponse(id, data, timeout);
    	return artistBrowse;
    }
    
    public BufferedImage loadImage(String id, long timeout) throws IOException {
    	if (id == null || id.length() != 40)
    		throw new IllegalArgumentException("Length of id must be 40");
    	
    	byte[] byteId = Util.stringToImageId(id);
    	
    	ReentrantLock lock = new ReentrantLock();
    	Condition waitHandler = lock.newCondition();
    	SyncResponseData data = new SyncResponseData(lock, waitHandler);
    	
    	int stateId = getInternalStateId();
    	
    	synchronized (SpotifyJ.lock) {
    		states.put(stateId, data);
    		
    		sp_image imagePtr = libspotify.sp_image_create(sessionPtr, byteId);
    		if (libspotify.sp_image_is_loaded(imagePtr))
    			new ImageLoadedCallback().callback(imagePtr, stateId);
    		else
    			libspotify.sp_image_add_load_callback(imagePtr, new ImageLoadedCallback(), stateId);
    	}
    	System.out.println("testig");
    	BufferedImage image = (BufferedImage) getSyncResponse(stateId, data, timeout);
    	return image;
    }
    
	/* Spotify event handler setters */
    public void setLogMessageHandler(SessionEventHandler logMessageHandler) {
    	this.logMessageHandler = logMessageHandler;
    }
    
    public void setLoginHandler(SessionEventHandler loginHandler) {
    	this.loginHandler = loginHandler;
    }
    
    public void setLogoutHandler(SessionEventHandler logoutHandler) {
    	this.logoutHandler = logoutHandler;
    }
    
    public void setMetaUpdatedHandler(SessionEventHandler metaUpdatedHandler) {
    	this.metaUpdatedHandler = metaUpdatedHandler;
    }
    
    public void setConnectionErrorHandler(SessionEventHandler connectionErrorHandler) {
    	this.connectionErrorHandler = connectionErrorHandler;
    }
    
    public void setMessageToUserHandler(SessionEventHandler messageToUserHandler) {
    	this.messageToUserHandler = messageToUserHandler;
    }
    
    public void setPlayTokenLostHandler(SessionEventHandler playTokenLostHandler) {
    	this.playTokenLostHandler = playTokenLostHandler;
    }
    
    public void setMusicDeliveryHandler(SessionEventHandler musicDeliveryHandler) {
    	this.musicDeliveryHandler = musicDeliveryHandler;
    }
    
    public void setEndOfTrackHandler(SessionEventHandler endOfTrackHandler) {
    	this.endOfTrackHandler = endOfTrackHandler;
    }
    
    public void setStreamingErrorHandler(SessionEventHandler streamingErrorHandler) {
    	this.streamingErrorHandler = streamingErrorHandler;
    }
    
    public void setUserinfoUpdatedHandler(SessionEventHandler userInfoupdatedHandler) {
    	this.userinfoUpdatedHandler = userinfoUpdatedHandler;
    }
    
    public void setStartPlaybackHandler(SessionEventHandler startPlaybackHandler) {
    	this.startPlaybackHandler = startPlaybackHandler;
    }
    
    public void setStopPlaybackHandler(SessionEventHandler stopPlaybackHandler) {
    	this.stopPlaybackHandler = stopPlaybackHandler;
    }
    
    public void setGetAudioBufferStatsPlaybackHandler(SessionEventHandler getAudioBufferStatsPlaybackHandler) {
    	this.getAudioBufferStatsPlaybackHandler = getAudioBufferStatsPlaybackHandler;
    }
    
    public void setOfflineStatusUpdatesHandler(SessionEventHandler offlineStatusUpdatesHandler) {
    	this.offlineStatusUpdatesHandler = offlineStatusUpdatesHandler;
    }
    
    public void setOfflineErrorHandler(SessionEventHandler offlineErrorHandler) {
    	this.offlineErrorHandler = offlineErrorHandler;
    }
    
    public void setCredentialsBlobUpdatedHandler(SessionEventHandler credentialsBlobUpdatedHandler) {
    	this.credentialsBlobUpdatedHandler = credentialsBlobUpdatedHandler;
    }
    
    public void setConnectionStateUpdatedHandler(SessionEventHandler credentialsBlobUpdatedHandler) {
    	this.credentialsBlobUpdatedHandler = credentialsBlobUpdatedHandler;
    }
    
    public void setScrobbleErrorHandler(SessionEventHandler scrobbleErrorHandler) {
    	this.scrobbleErrorHandler = scrobbleErrorHandler;
    }
    
    public void setPrivateSessionModeChangedHandler(SessionEventHandler privateSessionModeChangedHandler) {
    	this.privateSessionModeChangedHandler = privateSessionModeChangedHandler;
    }
	
	/* Threads */
	private class MainThread extends Thread {
    	public void run() {
    		IntByReference nextTimeout = new IntByReference(0);

    		while (true) {
    			try {
        			mainThreadLock.lock();
    				if (nextTimeout.getValue() == 0)
    					mainThreadShouldProcess.await();
    				mainThreadLock.unlock();
    				
    				do {
    					synchronized (SpotifyJ.lock) {
    						int code = libspotify.sp_session_process_events(sessionPtr, nextTimeout);
    						if (code != Constants.ERROR_OK)
    							nextTimeout.setValue(1000);
    					}
    				} while (nextTimeout.getValue() == 0);		
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
    		}
    	}
	}
	
	private class EventThread extends Thread {
		public void run() {
			Queue<SpotifyEventItem> workList = new LinkedList<SpotifyEventItem>();
			
			while (true) {
				try {
					eventThreadLock.lock();
					while (eventItemQueue.isEmpty())
						eventThreadShouldProcess.await();
				
					while (eventItemQueue.size() > 0)
						workList.add(eventItemQueue.poll());

					for (SpotifyEventItem item : workList)
						item.processEvent();
					
					workList.clear();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} finally {
					eventThreadLock.unlock();
				}
			}
		}
	}
		
	/* Private methods */
	private static Session getSession(sp_session sessionPtr) {
		return sessions.get(sessionPtr);
	}
	
	private void enqueueSpotifyEventItem(SpotifyEventItem item) {
		eventThreadLock.lock();
		eventItemQueue.offer(item);
		eventThreadShouldProcess.signal();
		eventThreadLock.unlock();
	}
	
	private int getUserStateId() {
		int result;
		synchronized (SpotifyJ.lock) {
			result = userStateCounter++;
		}
		return result;
	}
	
	private int getInternalStateId() {
		int result;
		synchronized (SpotifyJ.lock) {
			result = internalStateCounter++;
		}
		return result;	
	}
	
	private Object getSyncResponse(int id, SyncResponseData data, long timeout) {
		 boolean wasInterrupted = false;
		
    	try {
    		data.lock.lock();
    		wasInterrupted = data.waitHandler.await(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			data.lock.unlock();
		}
    	
		synchronized (SpotifyJ.lock) {
			try {
				if (wasInterrupted && states.containsKey(id))
					return states.get(id);
				else
					return null;
			} finally {
				states.remove(id);
			}
		}
	}
	
    private byte[] toBytes(char[] key){
        byte[] b = new byte[key.length];
        for (int i = 0; i < key.length; i++){
            if (key[i] > 127)
                b[i] = (byte) (key[i] - 256);
            else
                b[i] = (byte) key[i];
        }
        return b;
    }
    
    /* Internal callbacks */
    private class NotifyMainThreadCallback implements Callback {
    	public void callback(sp_session session) {
			Session s = getSession(session);
			if (s == null)
				return;
			
			try {
				s.mainThreadLock.lock();
				s.mainThreadShouldProcess.signal();
			} finally {
				s.mainThreadLock.unlock();
			}
		}
    }
    
	private class LoggedInCallBack implements Callback {
		public void callback(sp_session session, int error) {
			Session s = getSession(session);
			if (s == null)
				return;
			
			if (s.getConnectionState() == Constants.CONNECTIONSTATE_LOGGED_IN && error == Constants.ERROR_OK) {
				//TODO:
			}
			
			if (s.userLoggedIn != null) {
				s.enqueueSpotifyEventItem(new SpotifyEventItem(s.loginHandler, s, new SessionEventArgs(error)));
				s.loginLock.lock();
				s.loginCode = error;
				s.userLoggedIn.signal();
				s.loginLock.unlock();
			}
		}
	}
	
	private class LoggedOutCallback implements Callback {
		public void callback(sp_session session) {
			Session s = getSession(session);
			if (s == null)
				return;
			
			//TODO:
			/*if (s.playlistContainter != null) {
				
			}*/
			
			if (s.userLoggedIn != null) {
				s.loginLock.lock();
				s.userLoggedIn.signal();
				s.loginLock.unlock();
			}
			
			if (s.userLoggedOut != null) {
				s.enqueueSpotifyEventItem(new SpotifyEventItem(s.logoutHandler, s, new SessionEventArgs()));
				s.logoutLock.lock();
				s.userLoggedOut.signal();
				s.logoutLock.unlock();
			}
		}
	}
	
	private class SearchCompleteCallback implements Callback {
		public void callback(sp_search result, int userData) {
			Search search = new Search(result);
			Object state = states.get(userData);
			
			if (state != null && state instanceof SyncResponseData) {
				states.put(userData, search);
				ReentrantLock lock = ((SyncResponseData) state).lock;
				Condition waitHandler = ((SyncResponseData) state).waitHandler;
				
				try {
					lock.lock();
					waitHandler.signal();
				} finally {
					lock.unlock();
				}
			}
		}
	}
	
	private class AlbumBrowseCompleteCallback implements Callback {
		public void callback(sp_albumbrowse result, int userData) {
			AlbumBrowse albumBrowse = new AlbumBrowse(result);
			Object state = states.get(userData);
			
			if (state != null && state instanceof SyncResponseData) {
				states.put(userData, albumBrowse);
				ReentrantLock lock = ((SyncResponseData) state).lock;
				Condition waitHandler = ((SyncResponseData) state).waitHandler;
				
				try {
					lock.lock();
					waitHandler.signal();
				} finally {
					lock.unlock();
				}
			}
		}
	}
	
	private class ArtistBrowseCompleteCallback implements Callback {
		public void callback(sp_artistbrowse result, int userData) {
			ArtistBrowse artistBrowse = new ArtistBrowse(result);
			Object state = states.get(userData);
			
			if (state != null && state instanceof SyncResponseData) {
				states.put(userData, artistBrowse);
				ReentrantLock lock = ((SyncResponseData) state).lock;
				Condition waitHandler = ((SyncResponseData) state).waitHandler;
				
				try {
					lock.lock();
					waitHandler.signal();
				} finally {
					lock.unlock();
				}
			}
		}
	}
	
	private class ImageLoadedCallback implements Callback {
		public void callback(sp_image result, int userData) throws IOException {
			Object state = states.get(userData);
			
			if (state != null && state instanceof SyncResponseData) {
				ReentrantLock lock = ((SyncResponseData) state).lock;
				Condition waitHandler = ((SyncResponseData) state).waitHandler;
				
				IntByReference size = new IntByReference();
				Pointer data = libspotify.sp_image_data(result, size);
				
				byte[] imageData = data.getByteArray(0, size.getValue());
				states.put(userData, ImageIO.read(new ByteArrayInputStream(imageData)));
				
				try {
					lock.lock();
					waitHandler.signal();
				} finally {
					lock.unlock();
					libspotify.sp_image_release(result);
				}
			}
		}
	}
    
	private class LogMessageCallback implements Callback {
		public void callback(sp_session session, String message) {
			Session s = getSession(session);
			if (s == null)
				return;
			
			s.enqueueSpotifyEventItem(new SpotifyEventItem(s.logMessageHandler, s, new SessionEventArgs(message)));
		}
	}
	
	private class MetadataUpdatedCallback implements Callback {
		public void callback(sp_session session) {
			Session s = getSession(session);
			if (s == null)
				return;
			
			s.enqueueSpotifyEventItem(new SpotifyEventItem(s.metaUpdatedHandler, s, new SessionEventArgs()));
		}
	}
	
	private class ConnectionErrorCallback implements Callback {
		public void callback(sp_session session, int error) {
			Session s = getSession(session);
			if (s == null)
				return;
			
			s.enqueueSpotifyEventItem(new SpotifyEventItem(s.connectionErrorHandler, s, new SessionEventArgs(error)));
		}
	}
	
	private class MessageToUserCallback implements Callback {
		public void callback(sp_session session, String message) {
			Session s = getSession(session);
			if (s == null)
				return;
			
			s.enqueueSpotifyEventItem(new SpotifyEventItem(s.messageToUserHandler, s, new SessionEventArgs(message)));
		}
	}
	
	private class MusicDeliveryCallback implements Callback {
		public int callback(sp_session session, sp_audioformat format, Pointer frames, int numFrames) {
			Session s = getSession(session);
			if (s == null)
				return 0;
			
			int consumed = 0;
			byte[] sampleBytes = null;
			
			if (numFrames > 0) {
				sampleBytes = new byte[numFrames * format.channels * 2];
				sampleBytes = frames.getByteArray(0, sampleBytes.length);
			} else {
				sampleBytes = new byte[0];
			}
			
			if (s.musicDeliveryHandler != null) {
				MusicDeliveryEventArgs eventArgs = new MusicDeliveryEventArgs(format.channels, format.sample_rate, sampleBytes, numFrames);
				s.musicDeliveryHandler.process(s, eventArgs);
				consumed = eventArgs.getConsumedFrames();
			}
			
			return consumed;
		}
	}
	
	private class PlayTokenLostCallback implements Callback {
		public void callback(sp_session session) {
			Session s = getSession(session);
			if (s == null)
				return;
			
			s.enqueueSpotifyEventItem(new SpotifyEventItem(s.playTokenLostHandler, s, new SessionEventArgs()));
		}
	}
	
	private class EndOfTrackCallback implements Callback {
		public void callback(sp_session session) {
			Session s = getSession(session);
			if (s == null)
				return;
			
			s.enqueueSpotifyEventItem(new SpotifyEventItem(s.endOfTrackHandler, s, new SessionEventArgs()));

		}
	}
	
	private class StreamingErrorCallback implements Callback {
		public void callback(sp_session session, int error) {
			Session s = getSession(session);
			if (s == null)
				return;
			
			s.enqueueSpotifyEventItem(new SpotifyEventItem(s.streamingErrorHandler, s, new SessionEventArgs(error)));
		}
	}
	
	private class UserInfoUpdatedCallback implements Callback {
		public void callback(sp_session session) {
			Session s = getSession(session);
			if (s == null)
				return;
			
			s.enqueueSpotifyEventItem(new SpotifyEventItem(s.userinfoUpdatedHandler, s, new SessionEventArgs()));
		}
	}
	
	private class StartPlaybackCallback implements Callback {
		public void callback(sp_session session) {
			System.out.println("startplayback() called");
		}
	}
	
	private class StopPlaybackCallback implements Callback {
		public void callback(sp_session session) {
			System.out.println("stopplayback() called");
		}
	}
	
	private class GetAudioBufferStatsPlayback implements Callback {
		public void callback(sp_session session, PointerByReference stats) {
			System.out.println("getaudiobuffer() called");
		}
	}
	
	private class OfflineStatusUpdatesCallback implements Callback {
		public void callback(sp_session session) {
			System.out.println("offlinestatus() called");
		}
	}
	
	private class OfflineErrorCallback implements Callback {
		public void callback(sp_session session, int error) {
			System.out.println("offlinerror() called");
		}
	}
	
	private class CredentialsBlobUpdatedCallback implements Callback {
		public void callback(sp_session session, String blob) {
			System.out.println("credblob() called");
		}
	}
	
	private class ConnectionStateUpdatedCallback implements Callback {
		public void callback(sp_session session) {
			System.out.println("connstate() called");
		}
	}
	
	private class ScrobbleErrorCallback implements Callback {
		public void callback(sp_session session, int error) {
			System.out.println("scrobberror() called");
		}
	}
	
	private class PrivateSessionModeChangedCallback implements Callback {
		public void callback(sp_session session, boolean is_private) {
			System.out.println("privatesession() called");
		}
	}
	
	private class SyncResponseData {
		ReentrantLock lock;
		Condition waitHandler;
		
		SyncResponseData(ReentrantLock lock, Condition waitHandler) {
			this.lock = lock;
			this.waitHandler = waitHandler;
		}
	}

}
