/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.UserAgent;

/**
 *
 * @author didier
 */


import SoftPhone.Protocol.Sip.Message.Event.DialogEstablishedEvent;
import SoftPhone.Protocol.Sip.Message.Event.IncommingCallEvent;
import SoftPhone.Protocol.Sip.UserAgent.Event.*;
public interface UserAgentListener
{
  public void processCallEstablishedEvent(CallEstablishedEvent event);

  public void processCallFails();

  public void processProgressing();

  public void processProvisionnal();

  public void processRedirect();

  public void processRegistredEvent(RegistredEvent event);

  public void processRinging();

  public void processTryingJoin();
  public void processWWWAuthRequired(wwwAuthRequired event);
  public void processProxyAuthenticate(ProxyAuthRequired event);
  public void processIncommingCallEvent(IncommingCallEvent event);
  public void processOutGoingCallEvent(OutGoingCallAcceptedEvent event);
  public void processTerminatedCallEvent(TerminatedCallEvent  event);
  public void processCancelledCallEvent();
  public void processDialogEstablishedEvent(DialogEstablishedEvent event);


}
