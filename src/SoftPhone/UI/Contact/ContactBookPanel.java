package SoftPhone.UI.Contact;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

/**
 *
 * @author didier
 */
public class ContactBookPanel extends JPanel
{
    private JScrollPane jScrollPane1;
    private JTable contactTable;
    private TableColumn arg0;

    public ContactBookPanel() {
         initComponents();

        addMouseListeners();

    }



  private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        contactTable= new JTable();
        contactTable.setSize(100,100);
        arg0=new TableColumn(70);
        
        contactTable.addColumn(arg0);
       

        jScrollPane1.setViewportView(contactTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE));

  }

   public void addMouseListeners()
    {
         MouseInputAdapter mouseListener = new MouseInputAdapter()
         {
            private JPopupMenu popAdminMenu;

            @Override
             public void mousePressed(MouseEvent e)

            {
              if (isEnabled() && e.isPopupTrigger())
              {
                    requestFocusInWindow();
                    int x = e.getX();
                    int y = e.getY();
                 

                    if (true)
                    {
                        popAdminMenu = new JPopupMenu();
                        if (true)
                        {



                            Action addContact = new AbstractAction("Ajouter Contact") {

                                public void actionPerformed(ActionEvent e) {


                                }
                            };

                            Action removeContact = new AbstractAction("Retirer Contact") {

                                public void actionPerformed(ActionEvent e) {


                                }



                            };

                              Action modifyContact = new AbstractAction("Retirer Contact") {

                                public void actionPerformed(ActionEvent e) {


                                }

                              };

                            popAdminMenu.add(addContact);
                            popAdminMenu.add(removeContact);
                            popAdminMenu.add(modifyContact);
                            popAdminMenu.show(e.getComponent(), x, y);
                        }
                    }
              }
            }

         };
         contactTable.addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

}
