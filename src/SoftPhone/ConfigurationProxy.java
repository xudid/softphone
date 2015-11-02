/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SoftPhone;

import SoftPhone.Configuration.Account.AccountLoader;
import SoftPhone.Configuration.GeneralConfiguration;
import SoftPhone.Configuration.GeneralConfigurationLoader;
import SoftPhone.Configuration.Media.MediaConfigurationLoader;
import SoftPhone.Configuration.Network.NetworkConfiguration;
import SoftPhone.Configuration.Network.NetworkConfigurationLoader;
import SoftPhone.Configuration.RTP.RTPConfiguration;
import SoftPhone.Configuration.RTP.RTPConfigurationLoader;
import SoftPhone.Configuration.Sound.AudioConfiguration;
import SoftPhone.Configuration.Sound.AudioConfigurationLoader;
import SoftPhone.Protocol.Sip.Account.SipAccount;
import SoftPhone.UI.Sip.SipAccountPanel;
import SoftPhone.UI.Configuration.AudioConfigurationPanel;
import SoftPhone.UI.Configuration.ConfigurationFrame;
import SoftPhone.UI.Configuration.GeneralConfigurationPanel;
import SoftPhone.UI.Configuration.NetworkConfigurationPanel;
import SoftPhone.UI.Configuration.RTPConfigurationPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Administrateur
 */
public class ConfigurationProxy 
{
        private GeneralConfiguration generalConfiguration;
        private GeneralConfigurationLoader generalConfigurationLoader;
        private  NetworkConfiguration networkConfiguration;
        private  NetworkConfigurationLoader networkConfigurationLoader;
        private  AudioConfigurationLoader soundConfigurationLoader;
        private  MediaConfigurationLoader mediaConfigurationLoader;
        private  AudioConfiguration soundConfiguration;
	private  SipAccount account;
        private  AccountLoader accountLoader;
        private ConfigurationFrame frame;
    private RTPConfiguration rtpConfiguration;
    private RTPConfigurationLoader rtpConfigurationLoader;

    

    public ConfigurationProxy(GeneralConfiguration generalConfiguration
                        ,GeneralConfigurationLoader generalConfigurationLoader,NetworkConfiguration networkConfiguration
                        ,NetworkConfigurationLoader networkConfigurationLoader
                        , RTPConfiguration rtpConfiguration
                        , RTPConfigurationLoader rtpConfigurationLoader
                        , AudioConfiguration soundConfiguration
                        ,AudioConfigurationLoader soundConfigurationLoader
                        ,MediaConfigurationLoader mediaConfigurationLoader
                        ,SipAccount account
                        ,AccountLoader accountLoader)
    {
        this.generalConfiguration=generalConfiguration;
        this.generalConfigurationLoader=generalConfigurationLoader;
        this.networkConfiguration = networkConfiguration;
        this.networkConfigurationLoader = networkConfigurationLoader;
        this.rtpConfiguration=rtpConfiguration;
        this.rtpConfigurationLoader=rtpConfigurationLoader;
        this.soundConfigurationLoader = soundConfigurationLoader;
        this.mediaConfigurationLoader = mediaConfigurationLoader;
        this.soundConfiguration = soundConfiguration;
        this.account = account;
        this.accountLoader = accountLoader;
        
         frame =new ConfigurationFrame();
        JTabbedPane tabbedPane =new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        GeneralConfigurationPanel gcp=
                    new GeneralConfigurationPanel(frame,generalConfigurationLoader);
        tabbedPane.add("General", gcp);
        NetworkConfigurationPanel networkPanel =new
                NetworkConfigurationPanel(frame
                                         ,"Configuration de l'interface réseau"
                                         ,networkConfigurationLoader);
        tabbedPane.add("Reseau", networkPanel);
        
        RTPConfigurationPanel rtpcp=
                        new RTPConfigurationPanel(frame,this.rtpConfigurationLoader);
        tabbedPane.add("Configuration RTP", rtpcp);
        AudioConfigurationPanel audioConfigurationPanel =
                        new AudioConfigurationPanel(frame,soundConfigurationLoader
                                                  ,mediaConfigurationLoader);
        tabbedPane.add("Audio", audioConfigurationPanel);
        SipAccountPanel accountPanel =new SipAccountPanel(frame,accountLoader);
        tabbedPane.add("Compte SIP", accountPanel);
        frame.setTabbedPanel(tabbedPane);
        
    }
    
    public void showConfWINDOW()
    {this.frame.setVisible(true);
    }
    
    
        
        
        
}
