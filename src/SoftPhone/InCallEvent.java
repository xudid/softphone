/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone;

import java.util.EventObject;
import javax.sip.message.Request;

/**
 *
 * @author didier
 */
public class InCallEvent extends EventObject
{
    private String remoteParticipant="";
    private Request request;

    public InCallEvent(Object source,Request request) {
        super(source);
        this.request=request;
    }


}
