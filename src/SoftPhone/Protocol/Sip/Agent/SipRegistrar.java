/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SoftPhone.Protocol.Sip.Agent;

import java.net.*;
import java.util.logging.*;

/**
 *
 * @author didier
 */
public class SipRegistrar
{
    private  String FullQualifiedDomainName;
    private String Ip;
    private String sipAddress="";
    private int port=5060;

    public SipRegistrar(String registarSipAddress)
    {
       

    }

    public SipRegistrar(String sipAddress,int port) {
         this.sipAddress =sipAddress;
         this.port =port;
        if (sipAddress.startsWith("sip:"))
        this.FullQualifiedDomainName = sipAddress.substring(4);
    }



    public SipRegistrar()
    {
       
    }


    public String getIp() {
        try
            {
                Ip =InetAddress
                    .getByName(this.getFullQualifiedDomainName()).getHostAddress();
            }
        catch (UnknownHostException ex)
            {
                Logger.getLogger(SipRegistrar.class.getName())
                .log(Level.SEVERE, null, ex);
            }
        return Ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(String port)
    {
        this.port=Integer.parseInt(port);
    }



    public String getFullQualifiedDomainName()
    {
        return FullQualifiedDomainName;
    }

    public void setFullQualifiedDomainName(String FullQualifiedDomainName)
    {
        this.FullQualifiedDomainName = FullQualifiedDomainName;
    }

    public String getSipAddress() {
        return sipAddress;
    }

    public void setSipAddress(String sipAddress) {
        this.sipAddress = sipAddress;
        this.FullQualifiedDomainName=sipAddress.substring(4);
    }



}
