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
public class InviteOKResponseEvent extends OkFinalResponseEvent
{
    private String dialogId="";

    public InviteOKResponseEvent(
                                  Object source
                                , int resCode
                                , long cseqNum
                                , Response response
                                , String dialogId
                                )
    {
        super(source, resCode, cseqNum, response);
        this.dialogId=dialogId;
    }

    public String getDialogId() {
        return dialogId;
    }




}
