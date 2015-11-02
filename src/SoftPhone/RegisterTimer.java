/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.sdp.Phone;

/**
 *
 * @author didier
 */
public class RegisterTimer implements Runnable
{

    private SoftPhone softPhone;

    public RegisterTimer(SoftPhone phone)
    {

        this.softPhone=phone;
    }

    public void run()
    {
        softPhone.Register();
    }








}
