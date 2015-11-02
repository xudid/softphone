/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Bell;

import SoftPhone.InCallEvent;
import SoftPhone.OKCallEvent;
import SoftPhone.RingCallEvent;
import SoftPhone.TryCallEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sip.address.Address;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author didier
 */
public class Bell implements LineListener 
{
     File f;
    Clip clip;
     AudioInputStream inputStream;
     DataLine.Info	info ;
    private boolean play;
    

    public Bell()
    {
       play=true;
        f =new File("/home/didier/ringtone.wav");
        

    }
    public void update(LineEvent event)
    {
        if (event.getType().equals(LineEvent.Type.STOP))
		{

			clip.close();
            Logger.getLogger(Bell.class.getName()).info("file is close");
            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(Bell.class.getName()).log(Level.SEVERE, null, ex);
            }
		}
		else if (event.getType().equals(LineEvent.Type.CLOSE))
		{
			/*
			 *	There is a bug in the jdk1.3/1.4.
			 *	It prevents correct termination of the VM.
			 *	So we have to exit ourselves.
			 */
			//System.exit(0);
		}

    }

    public void ring()
    {
        try {
            inputStream = AudioSystem.getAudioInputStream(f);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Bell.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Bell.class.getName()).log(Level.SEVERE, null, ex);
        }


        if (inputStream != null)
		{
			AudioFormat	format = inputStream.getFormat();
				info = new DataLine.Info(Clip.class, format);
            try {
                clip = (Clip) AudioSystem.getLine(info);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(Bell.class.getName()).log(Level.SEVERE, null, ex);
            }

		}
		else
		{
			System.out.println("Don't find file :  " + f.getName());
		}
         try {
                
                   clip.open(inputStream);
                    Logger.getLogger(Bell.class.getName()).info("file is open");
                   clip.addLineListener( this);
                   Logger.getLogger(Bell.class.getName()).info( "listener added");
            }
                  catch (LineUnavailableException ex) {
                Logger.getLogger(Bell.class.getName()).log(Level.SEVERE, null, ex);



            }
    catch (IOException ex) {
                Logger.getLogger(Bell.class.getName()).log(Level.SEVERE, null, ex);
            }
       
clip.loop(Clip.LOOP_CONTINUOUSLY);
Logger.getLogger(Bell.class.getName()).info("loop on ring");
        
        while (play)
			{
				/* sleep for 1 second. */
				try{

					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					 clip.stop();
                     clip.removeLineListener(this);
                    
				}
			}
    }

   

    

   


}
