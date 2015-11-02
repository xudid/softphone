/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.Call;

import SoftPhone.Protocol.Sip.Call.CallParticipantInterface;
import SoftPhone.Network.NetworkUtils;
import java.net.InetAddress;

/**
 *
 * @author didier
 */
public class CallParticipant implements CallParticipantInterface
{
    private String displayNAME="";
    private String sipAddress;
    private int sipPort=5060;
    private String IP="0.0.0.0";
    private InetAddress IPAddress;

    public CallParticipant(String sipAddress)
    {
        this.sipAddress=sipAddress;
    }

    public String getDisplayName()
    {
        return this.displayNAME;
    }

    public String getSipAddress()
    {
       return this.sipAddress;
    }

    public int getSipPort()
    {
        return this.sipPort;
    }

    public void setDisplayName(String name)
    {
        this.displayNAME=name ;
    }

    public void setSipAddress(String sipAddress)
    {
       this.sipAddress=sipAddress;
    }

    public void setSipPort(String port)
    {
        if(NetworkUtils.ValidatePortNumber(Integer.parseInt(port)))
        this.sipPort=Integer.parseInt(port);
    }

    public String getIP()
    {
        return this.IP;
    }

    public void setIP(String ip)
    {
        this.IP=ip;
    }

    public String getUserInfo()
    {
        String userinfo="";
        if(sipAddress.startsWith("sip:"))
        {
            userinfo =sipAddress.substring(4);
            if(userinfo.contains("@"))
            {
                int i=userinfo.indexOf("@");
                userinfo=userinfo.substring(0, i);
            }
        }
          return userinfo;
    }

    public void setIPAddress(InetAddress IPAddress)
    {
        this.IPAddress =IPAddress;

    }

    public InetAddress getIPAddress()
    {
         IPAddress =  NetworkUtils.StringToInet4Address(IP);
            return IPAddress;
    }

}
