/*
 * @author Didier MOINDREAU
 */

package SoftPhone.Protocol.Sip.UserAgent.Event;

import java.util.EventObject;
import javax.sip.message.Request;
import javax.sip.message.Response;

/**
 *
 * @author didier
 */
public class wwwAuthRequired extends EventObject
{
    private Response response;
    private Request request;
    private long cseqNum;

    public wwwAuthRequired(
                           Object source
                          ,Request request
                          , Response response
                          ,long cseqNum
                          )
    {
        super(source);
        this.response =response;
        this.request =request;
        this.cseqNum =cseqNum;
    }

    public Response getResponse()
    {
        return response;
    }

    public Request getRequest() {
        return request;
    }

    public long getCseqNum() {
        return cseqNum;
    }
    

    






}
