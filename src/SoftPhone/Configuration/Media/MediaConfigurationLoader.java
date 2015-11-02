/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

/*
 * merge the AudioConfiguratonLoader and the MediaConfigurationLoader
 */

package SoftPhone.Configuration.Media;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Format;

/**
 *
 * @author didier
 */
public class MediaConfigurationLoader
{
    private boolean configured;
    private Preferences root;
    private final Preferences node;
    private MediaConfiguration configuration;


    public MediaConfigurationLoader()
    {
         root = Preferences.userRoot();
         node = root.node("/SoftPhone/MediaConfiguration");
         configured=node.getBoolean("configured", configured);
         if(configured)
         {


         }


    }


    public String [] getDeviceCodecs(String deviceName)
    {
        CaptureDeviceInfo captureDevice =CaptureDeviceManager.
        getDevice(deviceName);
        Format [] formats =captureDevice.getFormats();
        for(Format f:formats)
        {
            System.out.println(f.getEncoding());
        }
            String [] availableCodecs={"PCMU","GSM"};

        return availableCodecs;

    }



    public boolean isConfigured() {
        return configured;
    }

    public void setConfigured(boolean configured)
    {
         this.configured = configured;
         node.putBoolean(" configured", configured);
    }

    public void save()
    {
        try
            {
                node.sync();
            }
        catch (BackingStoreException ex)
            {
                Logger.getLogger(MediaConfigurationLoader.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
    }



}
