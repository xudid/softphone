/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.UserAgent.Event;

import java.util.EventObject;

/**
 *
 * @author didier
 */
public class NokResponseEvent extends EventObject
{
    private String reason="Echec appel";

    public NokResponseEvent(Object source,String reason) {
        super(source);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }



}
