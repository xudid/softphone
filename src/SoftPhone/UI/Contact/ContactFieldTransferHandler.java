/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.UI.Contact;

import SoftPhone.UI.*;
import SoftPhone.Contact.Contact;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.TransferHandler;

/**
 *
 * @author didier
 */
public class ContactFieldTransferHandler extends TransferHandler
{
    private JTextField  contactField;

    public ContactFieldTransferHandler() 
    {

    }


    @Override
    public boolean canImport(JComponent comp, DataFlavor[] flavors)
    {
        return (comp instanceof ContactField);
    }

    @Override
    public boolean importData(JComponent comp, Transferable t)
    {
        boolean imported =false;
        if(canImport(comp, t.getTransferDataFlavors()))
        {
            try
                {
                   Contact contact = (Contact)
                          t.getTransferData(TransferableContact.CONTACT_FLAVOR);
                   contactField.setText(" ");
                   contactField.setText(contact.getSipAddress());
                }

            catch (UnsupportedFlavorException ex)
                {
                    Logger.getLogger(ContactFieldTransferHandler
                            .class.getName()).log(Level.SEVERE, null, ex);
                }            catch (IOException ex)
                {
                    Logger.getLogger(ContactFieldTransferHandler
                            .class.getName()).log(Level.SEVERE, null, ex);
                }
            imported=true;
        }

        return imported;
    }

    public void setContactField(JTextField contactField)
    {
        this.contactField = contactField;
    }


}
