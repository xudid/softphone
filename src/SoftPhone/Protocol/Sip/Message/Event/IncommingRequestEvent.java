/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.Message.Event;

import javax.sip.header.ViaHeader;
import javax.sip.message.Request;

/**
 *
 * @author didier
 */
public class IncommingRequestEvent extends MessageEvent
{
    private Request request;
    private String transactionID;
    public IncommingRequestEvent(Object source, long cseqNum2, Request request)
    {
		super(source);
		this.cseqNum=cseqNum2;
		this.method=request.getMethod();
		this.request= request;
        this.transactionID=
                     ((ViaHeader)request.getHeader(ViaHeader.NAME)).getBranch();
	}

    public Request getRequest()
    {
        return request;
    }

    public String getTransactionID()
    {
        return this.transactionID;
    }




}
