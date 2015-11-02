/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.Message.Event;

import javax.sip.message.Response;

/**
 *
 * @author didier
 */
public class FinalResponseEvent extends ResponseEvent
{

    public FinalResponseEvent(Object source
                              , int resCode
                              , long cseqNum
                              , Response response
                              )
    {
        super(source, resCode, cseqNum, response);
    }


}
