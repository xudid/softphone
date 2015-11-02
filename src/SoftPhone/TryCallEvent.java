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
public class TryCallEvent extends EventObject
{
    private Response response=null;
    public TryCallEvent(Object source,Response response)
    {
        super(source);
        this.response=response;
    }


}
