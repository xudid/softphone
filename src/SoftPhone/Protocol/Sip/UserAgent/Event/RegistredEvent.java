/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.UserAgent.Event;

import java.util.EventObject;
import javax.sip.address.Address;

/**
 *
 * @author didier
 */
public class RegistredEvent extends EventObject
{
    private int expires;
    private Address address;

    public RegistredEvent(Object source,Address addr,int expires)
    {
        super(source);
        this.address =addr;
        this.expires=expires;
    }

    public Address getAddress() {
        return address;
    }

    
    public int getExpires() {
        return expires;
    }

    


}
