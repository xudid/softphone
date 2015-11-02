/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone;

import javax.sip.address.Address;

/**
 *
 * @author didier
 */
public interface SoftPhoneListener
{

   public void processCallInProgress();

   public void processCallNok();
   public void processIncommingCall(InCallEvent ev);
   public void processCallTryResponse(TryCallEvent ev);
   public void processCallRingResponse(RingCallEvent ev);
   public void processCalloK(OKCallEvent ev);
   public void processTerminateCallByRemotePart();
   public void processRegisterOK(Address address);


}
