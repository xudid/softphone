/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.UserAgent;

import SoftPhone.Protocol.Sip.UserAgent.Event.RegistredEvent;

/**
 *
 * @author didier
 */
public interface UserAgentRegistrerListener {

    public void processRegistredEvent(RegistredEvent event);

}
