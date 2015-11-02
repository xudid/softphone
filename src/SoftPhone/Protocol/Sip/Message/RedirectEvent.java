/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.Message;

import java.util.EventObject;
import javax.sip.message.Response;

/**
 *
 * @author didier
 */
public class RedirectEvent extends EventObject
{
    private Response response =null;

    public RedirectEvent(Object source,Response response)
    {
        super(source);
        this.response =response;
    }

    public Response getResponse()
    {
        return response;
    }


}
