/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.Provider;

import gov.nist.javax.sip.stack.SIPDialog;
import java.util.HashMap;

/**
 *
 * @author didier
 */
public class DialogsRepository
{
    private HashMap<String,SIPDialog>dialogs;

    public DialogsRepository() {
        dialogs =new HashMap<String, SIPDialog>();
    }

   public void add(String dialogID,SIPDialog dialog)
   {
        dialogs.put(dialogID, dialog);
   }

   public void remove(String dialogID)
   {
        dialogs.remove(dialogID);
   }

   public SIPDialog get(String dialogID)
   {
       if(dialogs.isEmpty())return null;

       else if(dialogs.containsKey(dialogID))return dialogs.get(dialogID);
       else return null;
   }

}
