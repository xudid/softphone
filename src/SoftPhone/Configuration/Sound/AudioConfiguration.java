package SoftPhone.Configuration.Sound;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author didier
 */

public class AudioConfiguration {
    private String preferredSoundDevice="";
   

    public AudioConfiguration(String psd)
    {
        preferredSoundDevice=psd;
    }
    

    public String getPreferredSoundDevice()
    {
        return preferredSoundDevice;
    }

    

    
}
