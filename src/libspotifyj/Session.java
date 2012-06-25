package libspotifyj;

import com.sun.jna.Callback;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import libspotifyj.low.SpotifyJ;
import libspotifyj.low.sp_session;
import libspotifyj.low.sp_session_callbacks;
import libspotifyj.low.sp_session_config;

public class Session {
	
	/* Provides access to libspotify API */
	private static SpotifyJ libspotify = SpotifyJ.libspotify;
	
	/* Session data */
	private static Map<sp_session, Session> sessions = new HashMap<sp_session, Session>();
	private sp_session sessionPtr = new sp_session(Pointer.NULL);
	
	/* Login data */
	private Lock loginLock = new ReentrantLock();
	private Condition userLoggedIn;
	private int loginCode = Constants.ERROR_IS_LOADING;
	
	/* Main thread data */
	private Thread mainThread;
	private Lock mainThreadLock = new ReentrantLock();
	private Condition mainThreadShouldProcess = mainThreadLock.newCondition();
	
	private Session(char[] applicationKey, String cacheLocation, String settingsLocation, String userAgent) throws SpotifyException {
		sp_session_config config = new sp_session_config();
		sp_session_callbacks callbacks = new sp_session_callbacks();
		
		synchronized (SpotifyJ.lock) {
			//TODO: callbacks
			callbacks.log_message = new LogMessageCallback();
			callbacks.notify_main_thread = new NotifyMainThreadCallback();
			callbacks.logged_in = new LoggedInCallBack();
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
	}
	
	/* Public methods */
	public static Session createInstance(char[] applicationKey, String cacheLocation, String settingsLocation, String userAgent) throws SpotifyException {
		synchronized (SpotifyJ.lock) {
			if (sessions.size() > 0) {
				throw new IllegalStateException("Only one instance of Session is allowed");
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
	
    public int login(String username, String password, long ms) {
    	synchronized (SpotifyJ.lock) {
    		if (userLoggedIn != null) {
    			return Constants.ERROR_IS_LOADING;
    		} else {
    			int code = libspotify.sp_session_login(sessionPtr, username, password, false, null);
    			if (code != Constants.ERROR_OK)
    				return code;
    			else
    				userLoggedIn = loginLock.newCondition();
    		}
    		
    		boolean signal = false;

    		try {
        		loginLock.lock();
    			signal = userLoggedIn.await(ms, TimeUnit.MILLISECONDS);
    			userLoggedIn = null;
    			loginCode = Constants.ERROR_IS_LOADING;
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} finally {
				loginLock.unlock();
			}
			
			return signal ? loginCode : Constants.ERROR_OTHER_TRANSIENT;
    	}
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
	
	/* Private methods */
	private static Session getSession(sp_session sessionPtr) {
		return sessions.get(sessionPtr);
	}
	
    private byte[] toBytes(char[] key){
        byte[] b = new byte[key.length];
        for (int i = 0; i < key.length; i++){
            if (key[i] > 127)
                b[i] = (byte)(key[i] - 256);
            else
                b[i] = (byte)key[i];
        }
        return b;
    }
    
    /* Internal callbacks */
    private class NotifyMainThreadCallback implements Callback {
    	public void callback(sp_session session) {
			Session s = getSession(session);
			if (s == null)
				return;
			
			System.out.println("main");
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
			
			System.out.println("logged_in() called");
			if (s.getConnectionState() == Constants.CONNECTIONSTATE_LOGGED_IN && error == Constants.ERROR_OK) {
				//TODO:
			}
			
			if (s.userLoggedIn == null) {
				//TODO
			} else {
				s.loginCode = error;
				s.loginLock.lock();
				s.userLoggedIn.signal();
				s.loginLock.unlock();
			}
		}
	}
    
	private class LogMessageCallback implements Callback {
		public void callback(sp_session session, String message) {
			Session s = getSession(session);
			if (s == null)
				return;
			
			//TODO
			System.out.println("log_message() called:" + message);
			//s.addEventItem(new JSEventItem(s.logMessageHandler, s, new JSSessionEventArgs(message)));
		}
	}

}
