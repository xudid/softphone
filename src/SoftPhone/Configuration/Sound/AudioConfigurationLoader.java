/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Configuration.Sound;

import SoftPhone.Configuration.Media.MediaDeviceDetector;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;

/**
 *
 * @author didier
 */
public class AudioConfigurationLoader
{
    private AudioConfiguration configuration;
   
    private boolean configured =false;
    private String preferredSoundDevice;
    private Preferences root;
    private final Preferences node;
  


    public AudioConfigurationLoader(String path)
    {
        
         root = Preferences.userRoot();
         node = root.node("/SoftPhone/SoundConfiguration");
         configured=node.getBoolean("configured", configured);

         /* init Jmf framework
          */
         MediaDeviceDetector.setupJMF(path);

         if(configured)
         {
             preferredSoundDevice =node.get("preferredSoundDevice", preferredSoundDevice);
             if(preferredSoundDevice.equals(""))
          {
            configured =false;
          }
        else
         {
            configured=(VerifyPrefferredDevice(preferredSoundDevice))
                    ? true:false;
         }
            this.configuration =new AudioConfiguration(preferredSoundDevice);

         }
        
             
    }

    public String[] getSoundDevicesNames() 
    {
            String[] soundDevicesNames =new String[0];
            Vector devicesArray =(Vector)CaptureDeviceManager.getDeviceList(null);
            Vector <String> devicesArrayNames =new Vector<String>();
            if(soundDevicesNames.length ==0)
            {  String device="";
                        for(int i=0;i<devicesArray.size();i++)
                        {
                            device=((CaptureDeviceInfo)devicesArray.get(i)).getName();
                            devicesArrayNames.add(device);
                        }
                soundDevicesNames=devicesArrayNames.toArray(new String[devicesArrayNames.size()]);
            }
        return soundDevicesNames;
    }


    private boolean VerifyPrefferredDevice(String prefferredDevice)
    {
      boolean isOK =false;
      String soundDeviceNames[]=getSoundDevicesNames();
      for(String device:soundDeviceNames)
        {
            if(prefferredDevice.equals(device))isOK=true;
        }
      return isOK;
    }

    

    public boolean isConfigured() {
        return configured;
    }

    public void setConfigured(boolean configured)
    {
        this.configured = configured;
         node.putBoolean("configured", configured);
    }

    public void setPreferredSoundDevice(String preferredSoundDevice)
    {
        this.preferredSoundDevice = preferredSoundDevice;
        node.put("preferredSoundDevice",preferredSoundDevice);
    }

    public AudioConfiguration getSoundConfigurationInstance()
    {
        if(this.configuration==null)
        {
            return new AudioConfiguration(preferredSoundDevice);
        }
        else      return this.configuration;
    }

    public void save()
    {
        try {
            node.sync();
            }
        catch (BackingStoreException ex)
        {
            Logger.getLogger(AudioConfigurationLoader.class.getName())
                    .log(Level.SEVERE, null, ex);
        }


    }


}
