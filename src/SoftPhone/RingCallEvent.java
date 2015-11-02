/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone;

import java.util.EventObject;
import javax.sip.message.Response;

/**
 *
 * @author didier
 */
public class RingCallEvent extends EventObject
{
    private Response response=null;
    public RingCallEvent(Object source) {
        super(source);
    }


}
