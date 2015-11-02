/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Bell;

import SoftPhone.InCallEvent;
import SoftPhone.OKCallEvent;
import SoftPhone.RingCallEvent;
import SoftPhone.SoftPhoneListener;
import SoftPhone.TryCallEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.sip.address.Address;

/**
 *
 * @author didier
 */
public class BellController implements SoftPhoneListener
{
    private Bell bell;
    private Future<?> runningTask;
    private ExecutorService backgroundExec=null;
    public BellController(Bell bell)
    {
        this.bell = bell;
        backgroundExec =Executors.newCachedThreadPool();
    }

    private void ringing()
    {
         if(runningTask ==null)
   {
       runningTask=backgroundExec.submit(new Runnable()
       {
            public void run()
            {
               bell.ring();
            }
       });
   }
    }

   public  void stopRinging()
    {
        if(runningTask!=null)
      {
          runningTask.cancel(true);
          runningTask=null;
      }
    }

    public void processCallInProgress()
    {

    }

    public void processCallNok()
    {

    }

    public void processIncommingCall(InCallEvent ev)
    {
        ringing();
    }

    public void processCallTryResponse(TryCallEvent ev)
    {

    }

    public void processCallRingResponse(RingCallEvent ev)
    {

    }

    public void processCalloK(OKCallEvent ev)
    {

    }

    public void processTerminateCallByRemotePart()
    {

    }

    public void processRegisterOK(Address address)
    {
        
    }


}
