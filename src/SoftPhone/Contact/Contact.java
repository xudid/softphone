/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Contact;

import SoftPhone.Network.NetworkUtils;
import java.net.InetAddress;
import com.thoughtworks.xstream.annotations.*;

/**
 *
 * @author didier
 */
public class Contact implements Cloneable
{
    private String contactName=" ";
    private String sipAddress;
    private int sipPort=5060;

    private String IP="0.0.0.0";
    @XStreamOmitField
    private InetAddress IPAddress;
    @XStreamOmitField
    private int ReceivingPort;

    public Contact()
    {

    }

    public Contact(String sipAddress)
    {
        this.sipAddress = sipAddress;
    }

    public Contact(String sipAddress,String username) {
        this.sipAddress = sipAddress;
        this.contactName=username;
    }



    public String getIP()
    {
        return this .IP;
    }

    public void setIP(String IP)
    {
        this.IP = IP;
    }


    public String getSipPort()
    {
       return Integer.toString(sipPort);
    }

    public void setSipPort(int sipPort)
    {
        this.sipPort = sipPort;
    }



    public String getUsername()
    {
       return this.contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }




    public String getSipAddress()
    {
        return sipAddress;
    }

    public void setSipAddress(String sipAddress)
    {
        this.sipAddress = sipAddress;
    }

    @Override
    public String toString()
    {
        return this.contactName;
    }

   public  InetAddress getIPAddress() {
       IPAddress =  NetworkUtils.StringToInet4Address(IP);
            return IPAddress;

    }

    public void setIPAddress(InetAddress IPAddress) {
        this.IPAddress = IPAddress;
    }



    public int geReceivingPort() {
       return ReceivingPort;
    }

    public void setReceivingPort(int ReceivingPort) {
        this.ReceivingPort = ReceivingPort;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Contact contact=(Contact)super.clone();
        contact.IPAddress=NetworkUtils.StringToInet4Address(IP);
        return contact;
    }




}
