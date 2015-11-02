/*
 * 
 * TODO/trouver le pathname de la SipFactoryImpl et repasser par sipfactory.createSipStack(properties);
 *
 *
 */

package SoftPhone.Protocol.Sip.Provider;

import SoftPhone.Configuration.Network.NetworkConfiguration;
import SoftPhone.Configuration.Sip.SipConfiguration;

import gov.nist.javax.sip.stack.SIPServerTransaction;
import gov.nist.javax.sip.stack.SIPTransaction;
import java.util.*;
import java.util.logging.*;
import javax.sip.*;
import gov.nist.javax.sip.SipStackImpl;
import javax.sip.TransportNotSupportedException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author didier
 */
public class SipStackActivator {
    private SipStackImpl sipStack;
	private SipFactory sipFactory;
	private SipProvider sipProviderTCP;
    private SipProvider sipProviderUDP;
    private NetworkConfiguration networkConfiguration;
    private SipConfiguration sipConfiguration;


   public  SipStackActivator(NetworkConfiguration networkConfiguration,SipConfiguration sipConfiguration)
    {
       this.sipConfiguration =sipConfiguration;
       this.networkConfiguration =networkConfiguration;
        
		initSipStack(networkConfiguration);
        initListeningPoints();
       
   }


	private void initSipStack(NetworkConfiguration networkConfiguration)
    {
		
        // initialize SipStack with properties
        // DEBUGGING: Information will go to files
		
		Properties properties = new Properties();
		properties.setProperty("javax.sip.STACK_NAME", "SoftPhone");
		properties.setProperty("javax.sip.IP_ADDRESS",
        networkConfiguration.getLocalIp());
		properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "32");
		properties.setProperty("gov.nist.javax.sip.SERVER_LOG",
        "softphone.txt");
		properties.setProperty("gov.nist.javax.sip.DEBUG_LOG",
		"softphonedebug.log");

                sipFactory = SipFactory.getInstance();
            try
                {
                    sipStack = new SipStackImpl(properties);
                }
            catch (PeerUnavailableException ex)
                {
                    JOptionPane.showMessageDialog(new JFrame()
                     , "Veuillez verifier la configuration" +
                     "\n de la carte reseau ," +
                     "\n le cable est peut être débranché"
                     ,"Probleme de configuration"
                     ,JOptionPane.WARNING_MESSAGE);
               System.exit(-1);
                }
		
	}

    private void initListeningPoints()
    {
        ListeningPoint tcp = null;
        ListeningPoint udp = null;
        String localIp =networkConfiguration.getLocalIp();
        int sipPort=Integer.parseInt(this.sipConfiguration.getSipListenningPort());

        try 
            {
            tcp = sipStack
                    .createListeningPoint(localIp, sipPort, "tcp");
            udp = sipStack
                    .createListeningPoint(localIp, sipPort, "udp");
            } 

        catch (TransportNotSupportedException ex) 
            {
           JOptionPane.showMessageDialog(new JFrame()
                     , "Un probleme avec la couche réseau \"transport\""
                     +"\n est survenu"
                     ,"Probleme de configuration"
                     ,JOptionPane.WARNING_MESSAGE);
               System.exit(-1);
            }
        catch (InvalidArgumentException ex)
            {
             JOptionPane.showMessageDialog(new JFrame()
                     , "Un autre programme utilise le port sip" +
                     "\n par defaut du softPhone " +
                     "\n Changez le port  par défaut par " +
                     "\n un port libre sur votre ordinateur" +
                     "\n et essayer à nouveau "
                     ,"Probleme de configuration"
                     ,JOptionPane.WARNING_MESSAGE);
               System.exit(-1);
            }
       
        try
            {
                sipProviderTCP = sipStack.createSipProvider(tcp);
                sipProviderUDP = sipStack.createSipProvider(udp);
            } 
        catch (ObjectInUseException ex) 
            {
              
            }
    }

    public SipStackImpl getSipStack() {
        return sipStack;
    }



    public SipFactory getSipFactory() 
    {
        return sipFactory;
    }

    
    public SipProvider getSipProvider()
    {
        return this.sipProviderUDP;
    }

    public SipProvider getSipProviderTCP() {
        return sipProviderTCP;
    }

    public SipProvider getSipProviderUDP() {
        return sipProviderUDP;
    }

    public void removeTransaction(Transaction t)
    {
        sipStack.removeTransaction((SIPTransaction) t);
     
    }

    

   

}