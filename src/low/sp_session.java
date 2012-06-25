package low;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class sp_session extends PointerType {

    public sp_session(Pointer address) {
        super(address);
    }
	
    public sp_session() {
        super();
    }
	
}
