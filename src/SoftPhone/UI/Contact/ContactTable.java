/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.UI.Contact;

import SoftPhone.UI.*;
import SoftPhone.Contact.Contact;
import SoftPhone.Contact.ContactBook;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.TableModel;

/**
 *
 * @author didier
 */
public class ContactTable extends JTable
{
    private MouseInputAdapter mouseListener;
    private final ContactDialog contactDialog;
    private ContactBook book;

    

    public ContactTable(TableModel model)
    {
        super(model);
        
       this.contactDialog =new ContactDialog(new JFrame(),this, new Contact());
       this.setFillsViewportHeight(true);
       this.book=(ContactBook) getModel();
       this.contactDialog.addWindowListener(new WindowAdapter()
       {

            @Override
            public void windowClosed(WindowEvent arg0) {
                super.windowClosed(arg0);
            }

       }
       );
        
        //setCellSelectionEnabled(true);
        //setTransferHandler(th);
       // setDragEnabled(true);
      
        mouseListener =new MouseInputAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent evt)
            {
            int button= evt.getButton();
            if(button==3)
            {
                requestFocusInWindow();
                    int x = evt.getX();
                    int y = evt.getY();


                        JPopupMenu menu = new JPopupMenu();
                        JMenuItem addContactItem =new JMenuItem("Ajouter Contact");
                        addContactItem.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent arg0)
                        {
                            System.out.println("Ajouter Contact");
                            contactDialog.setContact(new Contact());
                            contactDialog.setAction("ADD");
                            contactDialog.setVisible(true);
                            
                        }
                    });
                if(book.isNotEmpty())
                    {
                    JMenuItem removeContactItem =new JMenuItem("Supprimer Contact");
                    removeContactItem.addActionListener(
                    new ActionListener()
                      {

                        public void actionPerformed(ActionEvent arg0)
                        {
                           System.out.println("Supprimer Contact");
                           int col = getSelectedColumn();
                           int row =getSelectedRow();
                           if((col >=0)&&(row >=0))
                           {

                                Contact contact=
                                       (Contact)book.getValueAt(row, col);
                                Object[] options = { "Valider", "Annuler" };
                               switch(
                               JOptionPane.showOptionDialog(
                                             null
                                            ,"Confirmez la suppression "
                                            +"de "+contact
                                            ,"Suppression Contact"
                                            ,JOptionPane.DEFAULT_OPTION
                                            ,JOptionPane.WARNING_MESSAGE
                                            ,null
                                            ,options
                                            , options[0]
                                                    ))
                               {

                                    case JOptionPane.YES_OPTION:
                                    {
                                        removeRowSelectionInterval(row, row);
                                        repaint();
                                        ((ContactBook)getModel())
                                                        .removeContact(contact);
                                        break;
                                    }
                                    default:;
                               }
                           }

                           else
                           {
                               JOptionPane.showMessageDialog(getParent()
                                 , "Selectionnez un contact et renouveler l'opération"
                                 ,"Supprimer un contact"
                                 ,JOptionPane.WARNING_MESSAGE);

                           }
                            
                        }
                      }
                    );

                    JMenuItem modifyContactItem =new JMenuItem("Modifier Contact");
                    modifyContactItem.addActionListener(
                    new ActionListener()
                      {

                        public void actionPerformed(ActionEvent arg0)
                        {
                            System.out.println("Modifier Contact");
                            int col =getSelectedColumn();
                            int row =getSelectedRow();
                            if((col >=0)&&(row >=0))
                            {
                                Contact contact=
                                       (Contact)getModel().getValueAt(row, col);

                                contactDialog.setContact(contact);
                                contactDialog.setAction("MODIFY");
                                contactDialog.setVisible(true);

                            }
                            else
                            {
                                JOptionPane.showMessageDialog(getParent()
                                 , "Selectionnez un contact et renouveler l'opération"
                                 ,"Modifier un Contact"
                                 ,JOptionPane.WARNING_MESSAGE);
                            }
                        }
                      }
                     );

                      menu.add(removeContactItem);
                       menu.add(modifyContactItem);
                    }
                       menu.add(addContactItem);
                      
                       menu.show(evt.getComponent(), x, y);




             }
            }
        };
                    
        addMouseListener(mouseListener);
        setTransferHandler(new ContactTableTransferHandler());
        setDragEnabled(true);
    }

public void cancelAddContact()
{
    this.contactDialog.setVisible(false);
}

private void activeContactDialog()
{
    this.contactDialog.setVisible(true);
}
    
public void update()
{
    this.contactDialog.setVisible(false);
    this.repaint();
}

}


