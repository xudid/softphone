/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Contact;

import com.thoughtworks.xstream.XStream.*;
import com.thoughtworks.xstream.annotations.XStreamOmitField.*;
import java.util.ArrayList;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import java.util.EventListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author didier
 */
public class ContactBook extends AbstractTableModel{


     @XStreamOmitField
    private ContactBookController controller;
     @XStreamImplicit(itemFieldName = "Contacts")
    private ArrayList<Contact>contacts =null;
     @XStreamOmitField
    private Contact selectedContact=null;
    private String contactBookPath;


    public ContactBook(String contactBookPath,ArrayList<Contact> contacts)
    {


        this.contacts =contacts;
        this.contactBookPath=contactBookPath;


    }

    public ContactBookController getController()
    {
        return controller;
    }

    public boolean isNotEmpty()
    {
        boolean empty =contacts.isEmpty();
        return !empty;
    }

    public void setController(ContactBookController controller) {
        this.controller = controller;
    }

    @Override
    public void addTableModelListener(TableModelListener arg0)
    {
        if(this.listenerList==null)
        {
            this.listenerList=new EventListenerList();
             this.listenerList.add(EventListener.class, arg0);
        }
        else
        this.listenerList.add(EventListener.class, arg0);
    }
  public  ArrayList<Contact> getContacts(){


        return contacts;
   }

  public void setContacts(ArrayList<Contact> contacts)
  {
      this.contacts=contacts;
  }

  public void addContact(Contact contact)
  {
      this.contacts.add(contact);
      ContactBookController.saveContactBook(contactBookPath,contacts);



  }

  public void removeContact(Contact contact)
  {
      this.contacts.remove(contact);
      ContactBookController.saveContactBook(contactBookPath,contacts);
  }

    public int getRowCount() {
        return contacts.size();
    }

    public int getColumnCount() {
       return 1;
    }

    public Object getValueAt(int arg0, int arg1)
    {
        System.out.println("index : "+ arg0);
        return contacts.get(arg0);
    }

    @Override
    public String getColumnName(int arg0) {
        return "Contacts";
    }

    public void update()
    {
        ContactBookController.saveContactBook(contactBookPath,contacts);
    }













}


