/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.UI.Contact;

import SoftPhone.Contact.Contact;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author didier
 */
public class TransferableContact implements Transferable
{
    public final static DataFlavor CONTACT_FLAVOR;

    static {
        try
            {
                String contactFlavorMimeType = DataFlavor
                        .javaJVMLocalObjectMimeType + ";class=" +
                                            TransferableContact.class.getName();
                CONTACT_FLAVOR = new DataFlavor(contactFlavorMimeType);
            }
        catch (ClassNotFoundException ex)
            {
                throw new RuntimeException(ex);
            }
    }

    private Object transferedItem;

    public TransferableContact(Object item)
    {
        this.transferedItem=deepCopy(item);
    }



    public DataFlavor[] getTransferDataFlavors()
    {
        return new DataFlavor[] {CONTACT_FLAVOR};
    }

    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        return CONTACT_FLAVOR.equals(flavor);
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
    {
        if(flavor.equals(CONTACT_FLAVOR))
        {
            return deepCopy(transferedItem);
        }

        else throw new UnsupportedFlavorException(flavor);
        
    }

    private Object deepCopy(Object item)
    {
        Object c = null ;
        if(item instanceof Contact)
          {
            try
                {
                    c = ((Contact) item).clone();
                }
            catch (CloneNotSupportedException ex)
                {
                    Logger.getLogger(TransferableContact.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
          }
        return c;
    }

}
