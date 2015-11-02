/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SoftPhone.UI.Configuration;

import SoftPhone.Configuration.Network.NetworkConfigurationLoader;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author didier
 */
public class NetworkConfigurationPanel extends JPanel
{
    private JFrame parent;
    private JPanel interfacepanel;
    private JPanel buttonpanel;
    private ButtonGroup group;
    private JButton cancelButton;
    private JButton okButton;
    private String [] options;
    private NetworkConfigurationLoader configurationLoader;
    

    public NetworkConfigurationPanel(final JFrame parent,String title
                                    ,final NetworkConfigurationLoader networkConfigurationLoader)
    {
        this.parent=parent;
        this.configurationLoader=networkConfigurationLoader;
        interfacepanel = new JPanel();
        buttonpanel =new JPanel();
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),title));
        setLayout(new BorderLayout());
        options =networkConfigurationLoader.getNetworkDevicesNames();
        if(options.length ==0){
        	System.out.println("No card to select");
        }
        if((options.length==1)&(options[0].startsWith("lo")))
        {
            JOptionPane.showMessageDialog(new JFrame()
                     , "Veuillez verifier la configuration" +
                     "\n de la carte reseau ," +
                     "\n le cable est peut être débranché"
                     ,"Probleme de configuration"
                     ,JOptionPane.WARNING_MESSAGE);
               System.exit(-1);
        }
       
       add(interfacepanel);
        
       group = new ButtonGroup();
       if(networkConfigurationLoader.isConfigured())
        {
           String iface = this.configurationLoader
              .getNetworkConfigurationInstance().getPrefferedNetworkInterface();
           int i=0;
           for(String option : options)
        {
                if(!option.startsWith("lo"))
                {
                     JRadioButton radioButton =new JRadioButton(option);
            radioButton.setActionCommand(option);
            interfacepanel.add(radioButton);
            group.add(radioButton);
            radioButton.setSelected(option.equalsIgnoreCase(iface));
                }
            i=i+1;
        }
            
        }
       else
       {
           int i=0;
           for(String option : options)
        {
            JRadioButton radioButton =new JRadioButton(option);
            
            if(!option.startsWith("lo"))
            {
                radioButton.setActionCommand(option);
                interfacepanel.add(radioButton);
                group.add(radioButton);
                radioButton.setSelected(option.equalsIgnoreCase(options[i]));
            }
            
           
        }
       }
        


        okButton =new JButton("Valider");
        cancelButton =new JButton("Annuler");
        buttonpanel.add(cancelButton);
        buttonpanel.add(okButton);
        add(buttonpanel,BorderLayout.SOUTH);
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) 
            {
               String selection =group.getSelection().getActionCommand();

                System.out.println(selection);
                networkConfigurationLoader.setPrefferedNetworkInterface(selection);
                networkConfigurationLoader.setConfigured(true);
                parent.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent arg0)
            {
                parent.dispose();
            }
          
        }
        );
        
        

    }

        private String getSelection()
        {
            return group.getSelection().getActionCommand();
        }

        
            }
           
           
        



