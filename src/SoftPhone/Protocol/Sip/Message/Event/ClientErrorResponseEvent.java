/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.Message.Event;

import javax.sip.message.Request;
import javax.sip.message.Response;

/**
 *
 * @author didier
 */
public class ClientErrorResponseEvent extends FinalResponseEvent
{
    private Request request;
    

    public ClientErrorResponseEvent(
                                    Object source
                                    ,Request request, Response response
                                    , long cseqNum
                                    , int resCode)
    {
        super(source, resCode, cseqNum, response);
        this.request =request;
    }

    public Request getRequest() {
        return request;
    }

    

   



}
