/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SoftPhone.Configuration.Sip;


/**
 *
 * @author didier
 */
import SoftPhone.Network.NetworkUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.*;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;





public class SipConfigurationLoader
{
    private final String configurationPath;
    private boolean configured=false;
    private  SipConfiguration  sipConfiguration =null;
    private XStream xstream;

    public SipConfigurationLoader(String configurationPath)
    {
        this.configurationPath =configurationPath;
          xstream = new XStream(new DomDriver());
         xstream.alias("SipConfiguration", SoftPhone.Configuration.Sip.SipConfiguration.class);
        load();
    }

    /*TODO revoir la génération de url des fichiers de conf pour tous les fichiers de conf
     * en fonction de la plateforme d execution et  fixer sa valeur comme variable d instance
     * a l'instantiation  du loader
    */
    private void load()
    {
         try {
    String fromFile = configurationPath+"\\SipConfiguration.xml";
            File f = new File(fromFile);
            byte[] bytes = new byte[(int) f.length()];
            FileInputStream fis= new FileInputStream(f);
            fis.read(bytes);
            fis.close();
            String xml = new String(bytes);
            sipConfiguration = (SipConfiguration) xstream.fromXML(xml);
            configured=verifyConf(sipConfiguration);

            }
         /*The file doesn't exist here we create it and init it with a SipConfiguration empty node*/
         catch (IOException ex)
            {
                try {
                        String fromFile = this.configurationPath+"\\SipConfiguration.xml";
                        File f = new File(fromFile);
                        f.createNewFile();
                        sipConfiguration =new SipConfiguration();
                        configured = verifyConf( sipConfiguration);
                        String xml = xstream.toXML(sipConfiguration);
                         byte[] bytes = new byte[(int) xml.length()];
                FileOutputStream fos =new FileOutputStream(f);
                xstream.toXML(sipConfiguration, fos);
                fos.close();
                        Logger.getLogger(SipConfigurationLoader .class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                catch (IOException ex1)
                {
                    Logger.getLogger(SipConfiguration.class.getName())
                            .log(Level.SEVERE, null, ex1);
                }

           }
        catch(StreamException ex)
        {
            try {
                Logger.getLogger(SipConfiguration.class.getName())
                        .log(Level.SEVERE, null, ex);
                SipConfiguration sipConfiguration = new SipConfiguration();
                String xml = xstream.toXML(sipConfiguration);
                String fromFile = this.configurationPath+"\\SipConfiguration.xml";
                File f = new File(fromFile);
                byte[] bytes = new byte[(int) xml.length()];
                FileOutputStream fos =new FileOutputStream(f);
                xstream.toXML(sipConfiguration, fos);
                fos.close();
                load();

                }
            catch (IOException ex1)
                {
                Logger.getLogger(SipConfigurationLoader .class.getName())
                        .log(Level.SEVERE, null, ex1);

                }
    
        }
    }
    private boolean verifyConf(SipConfiguration sipConfiguration) {
         boolean valid=false;
         String port =sipConfiguration.getSipListenningPort();

         try{
         valid = NetworkUtils.ValidatePortNumber(Integer.parseInt(port));
         }
         catch(NumberFormatException ex)
         {
             return valid;
         }
         return valid;
    }

    public boolean isConfigured() {
        return configured;
    }

    public void setConfigured(boolean Configured) {
        this.configured = Configured;
    }

     public SipConfiguration getSipConfiguration()
    {
        return sipConfiguration;
    }



     public  SipConfiguration getValidSipConfiguration()
     {
        return sipConfiguration;
     }

     public void save()
     {
        FileOutputStream fos = null;
        try {
                String xml = xstream.toXML(sipConfiguration);
                System.out.println(xml);
                String fromFile = configurationPath+"\\SipConfiguration.xml";
                File f = new File(fromFile);
                byte[] bytes = new byte[(int) xml.length()];
                fos = new FileOutputStream(f);
                xstream.toXML(sipConfiguration, fos);
                fos.close();
            }
        catch (IOException ex)
            {
                Logger.getLogger( SipConfigurationLoader.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
       finally
        {
            try
                {
                    fos.close();
                }
            catch (IOException ex)
                {
                    Logger.getLogger( SipConfigurationLoader.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
        }
     }
    }

