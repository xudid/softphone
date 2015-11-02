/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.UserAgent.Event;

import java.util.EventObject;

/**
 *
 * @author didier
 */
public class OutGoingCallAcceptedEvent extends EventObject
{
    private String sdpResponse=" ";
    private String dialogId=" ";

    public OutGoingCallAcceptedEvent(Object source
                                    ,String dialogId
                                    ,String sdpResponse
                                    )
    {
        super(source);
        this.sdpResponse = sdpResponse;
        this.dialogId= dialogId;
    }

    public String getSdpResponse() {
        return sdpResponse;
    }

    public String getDialogId() {
        return dialogId;
    }





}
