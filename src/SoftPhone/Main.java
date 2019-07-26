/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*TODO finish the init workflow with the new design*/

 


package SoftPhone;

import SoftPhone.Configuration.RTP.RTPConfiguration;
import SoftPhone.Protocol.Sip.Provider.TransactionsRepository;
import SoftPhone.Protocol.Sip.Provider.DialogsRepository;
import SoftPhone.Protocol.Sip.Provider.SipStackActivator;
import SoftPhone.Protocol.Sip.Account.SipAccount;
import SoftPhone.Media.MediaController;
import SoftPhone.Configuration.Media.MediaConfiguration;
import SoftPhone.Configuration.Network.NetworkConfiguration;
import SoftPhone.Configuration.Sound.AudioConfiguration;
import SoftPhone.Contact.ContactBookController;
import SoftPhone.Contact.ContactBook;
import SoftPhone.UI.SoftPhoneUI;
import SoftPhone.Protocol.Sip.Message.SipMessageBank;
import SoftPhone.Protocol.Sip.Message.SipHeaderBankFactory;
import SoftPhone.Protocol.Sip.Message.SipHeaderBank;
import SoftPhone.Protocol.Sip.Message.SipMessageBankFactory;
import SoftPhone.Protocol.Sip.Message.SipMessageDispatcher;
import SoftPhone.Configuration.Sip.SipConfiguration;
import SoftPhone.Configuration.Sip.SipConfigurationLoader;
import SoftPhone.Configuration.Account.AccountLoader;
import SoftPhone.Configuration.GeneralConfiguration;
import SoftPhone.Configuration.GeneralConfigurationLoader;
import SoftPhone.Configuration.Media.MediaConfigurationLoader;
import SoftPhone.Configuration.Network.NetworkConfigurationLoader;
import SoftPhone.Configuration.RTP.RTPConfigurationLoader;
import SoftPhone.Configuration.Sound.AudioConfigurationLoader;
import SoftPhone.Media.SdpProcessor;
import SoftPhone.UI.Sip.SipAccountPanel;
import SoftPhone.UI.Configuration.AudioConfigurationPanel;
import SoftPhone.UI.Configuration.ConfigurationFrame;
import SoftPhone.UI.Configuration.NetworkConfigurationPanel;
import SoftPhone.UI.Configuration.RTPConfigurationPanel;
import SoftPhone.UI.Configuration.SipConfigurationPanel;
import SoftPhone.Protocol.Sip.UserAgent.UserAgentServer;
import SoftPhone.Protocol.Sip.UserAgent.UserAgentClient;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author didier
 */
public class Main
{       
        private static ConfigurationFrame frame;
        private static GeneralConfiguration generalConfiguration;
        private static GeneralConfigurationLoader generalConfigurationLoader;
        private static NetworkConfiguration networkConfiguration;
        private static NetworkConfigurationLoader networkConfigurationLoader;
        private static AudioConfigurationLoader soundConfigurationLoader;
        private static MediaConfigurationLoader mediaConfigurationLoader;
        private static AudioConfiguration soundConfiguration;
        private static SipConfiguration sipConfiguration;
        private static SipConfigurationLoader sipConfigurationLoader;
	private static SipAccount account;
        private static AccountLoader accountLoader;
	private static SipMessageDispatcher sipMessageDispatcher;
        private static MediaController mediaController;
        private static MediaConfiguration mediaConfiguration;
        private static SipStackActivator sipStackActivator;
        private static SipHeaderBank sipHeaderBank;
        private static UserAgentServer userAgentServer;
        private static UserAgentClient userAgentClient;
        private static ContactBook contactBook;
        private static SoftPhone softPhone;
        private static SoftPhoneUI softPhoneUI;
        private static DialogsRepository dialogs;
        private static TransactionsRepository transactions;
        private static SdpProcessor sdpProcessor;
        private static SipMessageBank sipMessageBank;
        private static boolean configured =false;
        private static RTPConfiguration rtpConfiguration;
        private static RTPConfigurationLoader rtpConfigurationLoader;

        public static void main(String[] args)
        {
              frame =new ConfigurationFrame();
             
              configurePath();
             
            networkConfigurationLoader =new NetworkConfigurationLoader();
            if(networkConfigurationLoader.isConfigured())
            networkConfiguration =networkConfigurationLoader
                    .getNetworkConfigurationInstance();

            else
            {
               NetworkConfigurationPanel panel =new
                NetworkConfigurationPanel(frame,"Configuration de l'interface réseau"
                                          ,networkConfigurationLoader);
               frame.setPanel(panel);
               frame.setVisible(true);
               while(!networkConfigurationLoader.isConfigured())
               {
                   try
                   {
                        Thread.sleep(2);
                   }
                   catch (InterruptedException ex)
                   {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                   }
               }
                networkConfiguration =networkConfigurationLoader
                    .getNetworkConfigurationInstance();
            }
            
            rtpConfigurationLoader=new RTPConfigurationLoader();
            if(rtpConfigurationLoader.isConfigured())
            {
                rtpConfiguration =rtpConfigurationLoader.getConfiguration();
            }
            else
            {
                RTPConfigurationPanel rtpcp= 
                            new RTPConfigurationPanel(frame,rtpConfigurationLoader);
                frame.setPanel(rtpcp);
                frame.setVisible(true);
                while(!rtpConfigurationLoader.isConfigured())
                {
                try {
                    Thread.sleep(2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
            }

            sipConfigurationLoader =new SipConfigurationLoader(generalConfiguration.getPrefferedPath());
            if(sipConfigurationLoader.isConfigured())
            {
                sipConfiguration =sipConfigurationLoader.getValidSipConfiguration();
            }
            else
            {
                SipConfigurationPanel rtpcp=
                            new SipConfigurationPanel(frame,sipConfigurationLoader);
                frame.setPanel(rtpcp);
                frame.setVisible(true);
                while(!sipConfigurationLoader.isConfigured())
                {
                try {
                    Thread.sleep(2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
            }

            /*Stun protocol disabled*/
           // networkConfiguration.initPublicAddressAndNAT();

            soundConfigurationLoader =new AudioConfigurationLoader(generalConfiguration.getPrefferedPath());
            mediaConfigurationLoader=new MediaConfigurationLoader();
            if(soundConfigurationLoader.isConfigured())
            soundConfiguration =soundConfigurationLoader
                                            .getSoundConfigurationInstance();
            else
            {
                AudioConfigurationPanel audioConfigurationPanel =
                        new AudioConfigurationPanel(frame,soundConfigurationLoader
                                                  ,mediaConfigurationLoader);
                frame.setPanel(audioConfigurationPanel);
                frame.setVisible(true);
                while(!soundConfigurationLoader.isConfigured())
                {
                try {
                    Thread.sleep(2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
                 soundConfiguration =soundConfigurationLoader
                                            .getSoundConfigurationInstance();
            }



            accountLoader= 
                    new AccountLoader(generalConfiguration.getPrefferedPath());
            if(accountLoader.isConfigured())
            account =accountLoader.getConfiguredAccount();
            else
            {
                SipAccountPanel accountPanel =new SipAccountPanel(frame,accountLoader);
                frame.setPanel(accountPanel);
                frame.setVisible(true);
                while(!accountLoader.isConfigured())
                {
                try {
                    Thread.sleep(2);

                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
                account =accountLoader.getConfiguredAccount();
            }


            mediaConfiguration =new MediaConfiguration();


            mediaController =new MediaController(soundConfiguration
                                                ,mediaConfiguration
                                                ,networkConfiguration
                                                );
            sdpProcessor =new SdpProcessor(mediaConfiguration
                                          ,networkConfiguration
                                          );

            contactBook=
                ContactBookController
                 .getRegistredContacts(generalConfiguration.getPrefferedPath());

            sipStackActivator= new SipStackActivator(networkConfiguration
                                                    ,sipConfiguration);



            sipHeaderBank=SipHeaderBankFactory
                                        .getSipHeaderBank(
                                                           sipStackActivator
                                                          );



            sipMessageBank =SipMessageBankFactory
                                .getSipMessageBankInstance(
                                                           sipHeaderBank
                                                           ,networkConfiguration
                                                           , sipConfiguration);

            dialogs = new DialogsRepository();
            transactions =new TransactionsRepository();



            sipMessageDispatcher =new SipMessageDispatcher(sipStackActivator
                                                          ,dialogs
                                                          ,transactions
                                                          );

            userAgentServer = new  UserAgentServer(sipMessageDispatcher
                                                ,sipMessageBank
                                                ,networkConfiguration
                                                ,account
                                                ,dialogs
                                                ,transactions
                                                );

            userAgentClient = new UserAgentClient(sipMessageDispatcher
                                                  ,sipMessageBank
                                                  ,dialogs
                                                  ,transactions
                                                 );
             ConfigurationProxy cp=new ConfigurationProxy(
                        generalConfiguration
                        , generalConfigurationLoader
                        , networkConfiguration
                        , networkConfigurationLoader
                        ,  rtpConfiguration
                        ,  rtpConfigurationLoader
                        ,  soundConfiguration
                        ,soundConfigurationLoader
                        , mediaConfigurationLoader
                        , account
                        , accountLoader);

            softPhone =new SoftPhone(account
                                    ,userAgentClient
                                    ,userAgentServer
                                    ,contactBook
                                    ,mediaController
                                    ,sdpProcessor,cp);


            softPhoneUI =new SoftPhoneUI(softPhone, contactBook);
            softPhoneUI.setVisible(true);
            softPhone.Register();
           
            
            

    }
        
        private static void configurePath()
        {
            generalConfigurationLoader =
                                               new GeneralConfigurationLoader();
             if(generalConfigurationLoader.isConfigured())
             {
                 generalConfiguration = generalConfigurationLoader.getConfiguration();
             }
              
             else
             {
              JFileChooser fileChooser =new JFileChooser(new File("."));
              fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
              int result =fileChooser.showDialog(frame, "Selectionner");
              if(result == JFileChooser.APPROVE_OPTION)
              {
                  String path =fileChooser.getSelectedFile().getPath();
                  generalConfigurationLoader.setPrefferedPath(path);
              }
              else
              {
                  JOptionPane.showMessageDialog(new JFrame()
                     , "Veuillez selectionner le répertoire" +
                     "\n ou vous désirez placer le fichier de " +
                     "\n configuration de votre compte SIP" +
                     "\n et votre carnet de contact"
                     ,"Répertoire de configuration"
                     ,JOptionPane.WARNING_MESSAGE);
                  configurePath();
              }
             }
            
        }

}
