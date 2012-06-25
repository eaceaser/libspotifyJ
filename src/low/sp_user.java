package low;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class sp_user extends PointerType {

    public sp_user(Pointer address) {
        super(address);
    }
	
    public sp_user() {
        super();
    }
	
}
