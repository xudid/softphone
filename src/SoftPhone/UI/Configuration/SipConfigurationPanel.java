/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SipConfigurationPanel.java
 *
 * Created on 28 nov. 2009, 23:30:04
 */

package SoftPhone.UI.Configuration;

import SoftPhone.UI.Configuration.ConfigurationFrame;
import SoftPhone.UI.*;
import SoftPhone.Configuration.Sip.SipConfigurationLoader;
import SoftPhone.Network.NetworkUtils;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author didier
 */
public class SipConfigurationPanel extends javax.swing.JPanel {
    private final SipConfigurationLoader sipConfigurationLoader ;
    private JFrame parent;
    /** Creates new form SipConfigurationPanel */


    public SipConfigurationPanel(ConfigurationFrame frame, SipConfigurationLoader sipConfigurationLoader) {
        this.sipConfigurationLoader = sipConfigurationLoader;
        this.parent =  frame;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        sipPortField = new javax.swing.JTextField();
        OkButton = new javax.swing.JButton();
        CancelButton = new javax.swing.JButton();

        jLabel1.setText("Configuration du protocol SIP");

        jLabel2.setText("Port SIP : ");

        sipPortField.setColumns(10);
        sipPortField.setText(this.sipConfigurationLoader.getSipConfiguration().getSipListenningPort());
        sipPortField.setMinimumSize(new java.awt.Dimension(10, 100));

        OkButton.setText("Valider");
        OkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OkButtonActionPerformed(evt);
            }
        });

        CancelButton.setText("Annuler");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CancelButton)
                            .addComponent(sipPortField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(228, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(340, Short.MAX_VALUE)
                .addComponent(OkButton)
                .addGap(116, 116, 116))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(sipPortField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(138, 138, 138)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(OkButton)
                    .addComponent(CancelButton))
                .addContainerGap(45, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void OkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OkButtonActionPerformed
      validateSIPPort( sipPortField.getText()  );
    }//GEN-LAST:event_OkButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CancelButton;
    private javax.swing.JButton OkButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField sipPortField;
    // End of variables declaration//GEN-END:variables
 private void validateSIPPort(String sipPort  )
    {
        try
        {
            int port1 =Integer.parseInt(sipPort);
            if(NetworkUtils.ValidatePortNumber(port1))

        {
            sipConfigurationLoader.getSipConfiguration().setSipListenningPort(sipPort);
            sipConfigurationLoader.save();
            sipConfigurationLoader.setConfigured(true);
            this.parent.dispose();
        }
        else
        {
            showBadPortMessage();
        }
        }
        catch(NumberFormatException ex)
        {
            showBadPortMessage();
        }
}

    private void showBadPortMessage()
    {
        JOptionPane.showMessageDialog(this
                     , "Saisissez un numéro de port valide. " +
                     "\n la valeur doit être entière et supérieur à  1024"
                     ,"Numéro de port invalide !"
                     ,JOptionPane.WARNING_MESSAGE);
    }
}

