/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.Message.Event;

import java.util.EventObject;

/**
 *
 * @author didier
 */
public class DialogEstablishedEvent extends EventObject
{
    private String dialogID;

    public DialogEstablishedEvent(Object source,String dialogID)
    {
        super(source);
        this.dialogID=dialogID;
    }

    public String getDialogID() {
        return dialogID;
    }






}
