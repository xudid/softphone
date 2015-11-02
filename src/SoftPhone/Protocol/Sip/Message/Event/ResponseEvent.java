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
public class ResponseEvent extends MessageEvent{
   
    protected Response response;
    protected int reasonCode;
    public ResponseEvent(Object source
                            ,int resCode
                            ,long cseqNum
                            ,Response response
                            )
    {
		super(source);

		this.reasonCode = resCode;
		this.cseqNum    = cseqNum;
		this.response   = response;
		
	}

    public int getResCode() {
        return reasonCode;
    }

    public int getReasonCode() {
        return reasonCode;
    }

    public Response getResponse() {
        return response;
    }

    

}
