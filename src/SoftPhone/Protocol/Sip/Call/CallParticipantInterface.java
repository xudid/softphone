/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.Call;

import java.net.InetAddress;

/**
 *
 * @author didier
 */
public interface CallParticipantInterface {

    public String getDisplayName();

    public InetAddress getIPAddress();

    public String getSipAddress();

    public String getUserInfo();

    public int getSipPort();

    public void setDisplayName(String name);

    public void setIPAddress(InetAddress IPAddress);

    public void setSipAddress(String sipAddress);

    public void setSipPort(String port);

}
