/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.Message.Event;

import java.util.EventObject;
import javax.sip.message.Request;

/**
 *
 * @author didier
 */
public class IncommingCallEvent extends IncommingRequestEvent
{
    private String transactionID="";
    public IncommingCallEvent(Object source,String transactionID, Request request, long cseqNum)
    {
        super(source, cseqNum, request);
        this.transactionID=transactionID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    



}
