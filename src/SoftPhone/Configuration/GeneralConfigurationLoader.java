/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SoftPhone.Configuration;

import SoftPhone.Configuration.Network.NetworkConfigurationLoader;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 *
 * @author Administrateur
 */
public class GeneralConfigurationLoader 
{

   
    private  String prefferedPath="";
    private final Preferences node;
    private boolean configured;
    private GeneralConfiguration generalConfiguration;

    public GeneralConfigurationLoader() 
    {
        Preferences root = Preferences.userRoot();
        node = root.node("/SoftPhone/GeneralConfiguration");
        if (node==null)System.out.println("General configuration node is null");
        prefferedPath =node.get("prefferedPath", prefferedPath);
        if(prefferedPath.equals("")||prefferedPath ==null)
          {
            configured =false;
          }
        else
         {
            configured=(VerifyPrefferredPath(prefferedPath))
                    ? true:false;
         }
        if(configured)this.generalConfiguration =
                new GeneralConfiguration(prefferedPath);
    }

    public boolean isConfigured() 
    {
        return  this.configured;
    }
    
     public  GeneralConfiguration getConfiguration() 
     {
        return this.generalConfiguration;
     }
    
    public void setPrefferedPath(String path)
    {
        this.prefferedPath=path;
        node.put("prefferedPath", prefferedPath);
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

    private boolean VerifyPrefferredPath(String prefferedPath) 
    {
        File file =new File(prefferedPath);
       return (file.exists()&&(file.isDirectory()));
    }
    }
    
    

