package SoftPhone.Contact;


import SoftPhone.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.*;import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.logging.*;
/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

/**
 *
 * @author didier
 */
public class ContactBookController {

    private  static ArrayList<Contact> contacts=null;
    private String contactBookFile;
    

    private ContactBookController(String contactBookPath)
    {
        try {
            
                XStream xstream = new XStream(new DomDriver());
            xstream.alias("Contact", Contact.class);
            xstream.processAnnotations(Contact.class);

            File f = new File(contactBookPath+"\\Contacts.xml");
            if (f.exists()){
            byte[] bytes = new byte[(int) f.length()];
            FileInputStream fis=    new FileInputStream(f);
            fis.read(bytes);
            contactBookFile =  new String(bytes);
            contacts =  (ArrayList<Contact>) xstream.fromXML(contactBookFile);
            fis.close();}
            else
            {
                
            ArrayList<Contact>cts =new ArrayList<Contact>();
            String xml =xstream.toXML(cts);
            System.out.print(xml);
            byte[] contactsbytes = new byte[(int)xml.length()];
            FileWriter writer = null;
            f = new File(contactBookPath+"\\Contacts.xml");
            
            writer = new FileWriter(f);
            OutputStreamWriter out = (OutputStreamWriter) writer;

            out.write(xml);
            out.close();
           
           
            FileInputStream fis=    new FileInputStream(f);
            fis.read(contactsbytes);
            contactBookFile =  new String(contactsbytes);
            System.out.println(contactBookFile);
            fis.close();
            
            }


            


            }
        catch (IOException ex)
        {
            Logger.getLogger(ContactBookController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        XStream xstream = new XStream(new DomDriver());
            xstream.alias("Contact", Contact.class);
            xstream.processAnnotations(Contact.class);
        contacts =  (ArrayList<Contact>) xstream.fromXML(contactBookFile);

    }


    public static ContactBook getRegistredContacts(String contactBookPath)
    {
        ContactBookController control =new ContactBookController(contactBookPath);
         contacts =ContactBookController.contacts;
         ContactBook book=new ContactBook(contactBookPath,contacts);
        book.setController(control);
        book.setContacts(contacts);
            
     
         return book;
    }

    public static void saveContactBook(String contactBookPath,ArrayList<Contact> contacts)
    {
        try
            {

                XStream xstream = new XStream(new DomDriver());
                xstream.alias("Contact", Contact.class);
                xstream.processAnnotations(Contact.class);
                String xml = xstream.toXML(contacts);
                System.out.print(xml);
                FileWriter writer = null;
                File f = new File(contactBookPath+"\\Contacts.xml");
                writer = new FileWriter(f);
                OutputStreamWriter out = (OutputStreamWriter) writer;
                out.write(xml);
                out.close();
            }
        catch (IOException ex)
        {
            Logger.getLogger(ContactBookController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

}
