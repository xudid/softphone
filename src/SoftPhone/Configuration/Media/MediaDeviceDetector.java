/*
 * thanks to SIP Communicator
 * for help to understand JMF init and device registration
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 *
 * File based on:
 * @(#)JMFInit.java 1.14 03/04/30
 * Copyright (c) 1996-2002 Sun Microsystems, Inc.  All rights reserved.
 */

package SoftPhone.Configuration.Media;




import java.io.*;
import com.sun.media.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Probes for available capture and playback devices and initializes the
 * jmf.properties accordingly.
 *
 * @author Emil Ivov
 */
public class MediaDeviceDetector
{

    private static final Logger logger = Logger.getLogger(MediaDeviceDetector.class.getName());

    /**
     * The JMF property that specifies whether we'd have the right to capture
     * when run from webstart or an applet.
     */
    private static final String PROP_ALLOW_CAPTURE_FROM_APPLETS
        = "secure.allowCaptureFromApplets";

    /**
     * The JMF property that specifies whether we'd have the right to save
     * files when run from webstart or an applet.
     */
    private static final String PROP_ALLOW_SAVE_FILE_FROM_APPLETS
        = "secure.allowSaveFileFromApplets";

    /**
     * The JMF registry property that specifies that have initilized the
     * currently valid repository.
     */
    private static final String PROP_REGISTRY_AUTHOR
        = "registry.author";

    /**
     * The value of the JMF registry property that determines whether we have
     * initilized the currentl repository.
     */
    private static final String PROP_REGISTRY_AUTHOR_VALUE
        = "SoftPhone";

    /**
     * The name of the file that the JMF registry uses for storing and loading
     * jmf properties.
     */
    private static final String JMF_PROPERTIES_FILE_NAME = "jmf.properties";

    /**
     * Default constructor - does nothing.
     */
    public MediaDeviceDetector()
    {
    }

    /**
     * Detect all capture devices
     */
    private void initialize()
    {
        String author = (String)Registry.get(PROP_REGISTRY_AUTHOR);

        if(author != null)
        {
            return;
        }

        Registry.set(PROP_ALLOW_CAPTURE_FROM_APPLETS, new Boolean(true));
        Registry.set(PROP_ALLOW_SAVE_FILE_FROM_APPLETS, new Boolean(true));

        Registry.set(PROP_REGISTRY_AUTHOR, PROP_REGISTRY_AUTHOR_VALUE);

        try
        {
            Registry.commit();
        }
        catch (Exception exc)
        {
             Logger.getLogger(MediaDeviceDetector.class.getName())
                        .log(Level.SEVERE, null, exc);
        }


        detectCaptureDevices();
    }

    /* At the moment detect if JavaSound is present
     * TODO make an extension point here when SoftPhone will be Pluginified
     */
    private void detectCaptureDevices()
    {
        // check if JavaSound capture is available
        logger.info("Looking for Audio capturer");

        JavaSoundAuto javaSoundAuto = new JavaSoundAuto();
    }



    /**
     * Runs JMFInit the first time the application is started so that capture
     * devices are properly detected and initialized by JMF.
     */
    public static void setupJMF(String path)
    {
        try
        {


            // we'll be storing our jmf.properties file inside the
            //SoftPhone  directory.
            File jmfPropsFile = null;
            try
            {

                    jmfPropsFile
                    = new File(path
                               + File.separator
                               + "jmf.properties");

                //force reinitialization
                if(jmfPropsFile.exists())
                    jmfPropsFile.delete();
                jmfPropsFile.createNewFile();
            }
            catch (Exception exc)
            {
                throw new RuntimeException(
                    "Failed to create the jmf.properties file.", exc);
            }
            String classpath =  System.getProperty("java.class.path");

            classpath = jmfPropsFile.getParentFile().getAbsolutePath()
                + System.getProperty("path.separator")
                + classpath;

            System.setProperty("java.class.path", classpath);

            /** @todo run this only if necessary and in parallel. Right now
             * we're running detection no matter what. We should be more
             * intelligent and detect somehow whether new devices are present
             * before we run our detection tests.*/
            MediaDeviceDetector detector = new MediaDeviceDetector();
            detector.initialize();
        }
        finally
        {
            
        }

    } 
   
}
