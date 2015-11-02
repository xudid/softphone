/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SoftPhone.Configuration.RTP;

import SoftPhone.Configuration.Network.NetworkConfigurationLoader;
import SoftPhone.Network.NetworkUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


/**
 *
 * @author Administrateur
 */
public class RTPConfigurationLoader 
{
    private final Preferences node;
    private boolean configured;
    private int firstPrefferredRtpPort=8000;
    private int lastPrefferredRtpPort=8050;
    private RTPConfiguration rtpConfiguration;

    public RTPConfigurationLoader() 
    {
        Preferences root = Preferences.userRoot();
        node = root.node("/SoftPhone/RTPConfiguration");
        firstPrefferredRtpPort=node.getInt("firstPrefferredRtpPort"
                                                    , firstPrefferredRtpPort);
        lastPrefferredRtpPort=node.getInt("lastPrefferredRtpPort"
                                                     , lastPrefferredRtpPort);
        if(!NetworkUtils.ValidatePortNumber(firstPrefferredRtpPort)
                &!NetworkUtils.ValidatePortNumber(lastPrefferredRtpPort))
          {
            configured =false;
          }
        else
         {
            configured=true;
         }

        {
            this.rtpConfiguration =
                new RTPConfiguration(firstPrefferredRtpPort,
                                                        lastPrefferredRtpPort);
        }
    }
    
    
 
    public boolean isConfigured() 
    {
        return  this.configured;
    }
    
     public  RTPConfiguration getConfiguration() 
     {
         
        return this.rtpConfiguration;
     }
    
    public void setRtpPortRange(int first,int last)
    {
        this.firstPrefferredRtpPort=first;
        this.lastPrefferredRtpPort=last;
        node.putInt("firstPrefferredRtpPort", firstPrefferredRtpPort);
        node.putInt("lastPrefferredRtpPort", lastPrefferredRtpPort);
        try
            {
                    node.sync();
            } 
        catch (BackingStoreException ex)
            {
                Logger.getLogger(NetworkConfigurationLoader.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
    }

    public int getFirstPrefferredRtpPort() {
        return firstPrefferredRtpPort;
    }

    public int getLastPrefferredRtpPort() {
        return lastPrefferredRtpPort;
    }
    
    

}
