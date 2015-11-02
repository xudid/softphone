/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.UI.Contact;


import SoftPhone.UI.*;
import SoftPhone.UI.Contact.ContactTable;
import SoftPhone.Contact.Contact;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 *
 * @author didier
 */
class ContactTableTransferHandler  extends TransferHandler{

    public ContactTableTransferHandler() 
    {


    }

    @Override
    protected Transferable createTransferable(JComponent comp)
    {
        TransferableContact tc = null;
        ContactTable contactTable=(ContactTable)comp;

        int col =contactTable.getSelectedColumn();
        int row =contactTable.getSelectedRow();
        Contact contact=(Contact) contactTable.getModel().getValueAt(row, col);
        tc =new TransferableContact(contact);
        Transferable t= super.createTransferable(comp);
        
        
        return tc;
    }

    @Override
    protected void exportDone(JComponent comp, Transferable tr, int action)
    {
        ContactTable contactTable=(ContactTable)comp;
       
    }

    @Override
     public int getSourceActions(JComponent c)
     {
        return TransferHandler.COPY;
     }





}
