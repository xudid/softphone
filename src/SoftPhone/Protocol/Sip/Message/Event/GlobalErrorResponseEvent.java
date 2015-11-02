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
public class GlobalErrorResponseEvent extends FinalResponseEvent
{

    public GlobalErrorResponseEvent(
                                    Object source
                                    , int resCode
                                    , long cseqNum
                                    , Response response
                                    )
    {
        super(source, resCode, cseqNum, response);
    }


}
