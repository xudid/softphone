/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SoftPhone.Configuration.Network;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author didier
 */
import java.net.*;
import net.java.stun4j.*;
import net.java.stun4j.stack.*;
import net .java.stun4j.client.*;
import java.util.*;
import java.util.logging.*;

import SoftPhone.Network.NetworkIface;

public class NetworkConfiguration {
    private NetworkIface prefferedNetworkInterface=null;
   
   
   
    private InetAddress localaddress;
    private InetAddress publicAddress;
    private String localIp="NotConfigured";
    private String publicIp="NotConfigured";
    private String Nat="NotConfigured";
    public NetworkConfiguration(NetworkIface networkInterface)
    {
        this.prefferedNetworkInterface=networkInterface;
        ScanNetworkLocalAddress(prefferedNetworkInterface.getName());
        
    }

    public String getPrefferedNetworkInterface() {
        return prefferedNetworkInterface.getDisplayName();
    }


    private void ScanNetworkLocalAddress(String networkInterface)
    {
        try {
        	System.out.println("preferred iface");
        	System.out.println(networkInterface);
            NetworkInterface iface          
                    = NetworkInterface.getByName(networkInterface);
           
           if(iface.isUp()){
            Enumeration<InetAddress>addresses=iface.getInetAddresses();
             while(addresses.hasMoreElements())
        {
         InetAddress address =   addresses.nextElement();
        if(address instanceof Inet4Address && !(address.isLoopbackAddress())){
            localaddress=address;
             localIp =address.toString().substring(1);
        }
             }
           }
        } catch (SocketException ex) {
            Logger.getLogger(NetworkConfiguration.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
 }

    
    public void initPublicAddressAndNAT()
    {
        StunStack stunStack=null;
        StunDiscoveryReport stunDiscoveryReport =null;
        NetAccessPointDescriptor apDescriptor;
        StunProvider stunProvider;
        NetworkConfigurationDiscoveryProcess  discoveryProcess = null;
        StunAddress serverAddress =new StunAddress("stun01.sipphone.com", 3478);
        StunAddress localStunAddress =new StunAddress(localaddress, 1024 +
        (int) (Math.random() * 64512));
        apDescriptor =new NetAccessPointDescriptor(localStunAddress);
        stunStack = StunStack.getInstance();
        try {
            discoveryProcess =new NetworkConfigurationDiscoveryProcess(apDescriptor, serverAddress);
            discoveryProcess.start();
            stunDiscoveryReport=discoveryProcess.determineAddress();
            discoveryProcess.shutDown();
            System.out.println(stunDiscoveryReport.getPublicAddress().getSocketAddress().getAddress().getHostAddress());
            System.out.println(stunDiscoveryReport.getNatType());
        } catch (StunException ex) {
            Logger.getLogger(NetworkConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    

    public String getLocalIp() {
        return localIp;
    }

    public InetAddress getLocaladdress() {
        return localaddress;
    }


    public String getNat() {
        return Nat;
    }

    public String getPublicIp() {
        return publicIp;
    }



}
