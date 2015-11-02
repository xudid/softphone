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
public class OKCallEvent extends EventObject
{
    private Response  response=null;

    public OKCallEvent(Object source,Response response)
    {
        super(source);
        this.response =response;
    }


}
