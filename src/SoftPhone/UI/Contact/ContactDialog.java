/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.UI.Contact;

import SoftPhone.UI.Contact.ContactPanel;
import SoftPhone.UI.Contact.ContactTable;
import SoftPhone.Contact.Contact;
import SoftPhone.Contact.ContactBook;
import java.awt.Frame;
import javax.swing.JDialog;

/**
 *
 * @author didier
 */
public class ContactDialog extends JDialog
{
    private ContactPanel contactPanel;
    private ContactTable  contactTable;

    private Contact contact;

    public ContactDialog(Frame f,ContactTable table, Contact contact)
    {
        super(f);
        this.contactTable=table;
        this.contact=contact;
        
        this.contactPanel =new ContactPanel(contactTable,this.contact);
        this.add(contactPanel);
        setTitle("Contact");
        setSize(460,200);
    }

    public void setContact(Contact contact)
    {
        this.contactPanel.setContact(contact);
    }

    void setAction(String action) {
        this.contactPanel.setAction(action);
    }

}
