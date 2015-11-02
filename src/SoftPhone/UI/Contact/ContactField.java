/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.UI.Contact;

import javax.swing.DropMode;
import javax.swing.JTextField;
import javax.swing.TransferHandler;

/**
 *
 * @author didier
 */
public class ContactField extends JTextField
{
    private TransferHandler th;

    public ContactField(TransferHandler th)
    {
        this.th = th;
        this.setText("sip:");
        this.setDropMode(DropMode.INSERT);
        this.setTransferHandler(th);


    }



}
