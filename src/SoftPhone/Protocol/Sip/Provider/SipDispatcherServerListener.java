/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.Provider;

import SoftPhone.Protocol.Sip.Message.Event.DialogEstablishedEvent;
import javax.sip.RequestEvent;

/**
 *
 * @author didier
 */
public interface SipDispatcherServerListener
{
    public void requestReceived(RequestEvent requestEvent);
    public void processNewDialog(DialogEstablishedEvent establishedEvent);
}
