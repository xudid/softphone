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
public class CallEstablishedEvent extends EventObject
{
    private String dialogId="";


    public CallEstablishedEvent(Object source,String dialogId)
    {
        super(source);
        this.dialogId=dialogId;
    }

    public String getDialogId() {
        return dialogId;
    }




}
