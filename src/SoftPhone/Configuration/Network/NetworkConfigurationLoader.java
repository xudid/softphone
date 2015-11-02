/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Configuration.Network;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import SoftPhone.Network.NetworkIface;

/**
 *
 * @author didier
 * TODO design a Network interface class
 * design a Network interface structure where we can retrieve an iface by displayName
 * change the attribut preffered network interface in networkConfiguration class
 * by an attribut typed with networkinterface class
 * put a status label in networkconfiguration panel
 * ?design a networkinterfaces monitor /manger to deal with ifaces status and informations ?
 */
public class NetworkConfigurationLoader
{
    private String prefferedNetworkInterface="";
    private String [] networkDevicesNames;
     private Enumeration<NetworkInterface> networkDevices;
     private boolean configured=false;
     private final Preferences node;
     private NetworkConfiguration networkConfiguration;
     private HashMap<String , NetworkIface> netIfaceMap;

    public NetworkConfigurationLoader()
    {
    	String OS = System.getProperty("os.name").toLowerCase();
    	System.out.println(OS);
    	netIfaceMap = new HashMap<>();
        networkDevices=searchNetWorkInterfaces();
        networkDevicesNames =new String[0];
        Preferences root = Preferences.userRoot();
        if(root==null){System.out.println("userRoot node is null");}
        else{
        	System.out.print("user root name: ");
        	System.out.println(root.name());
        	
        }
        node = root.node("/SoftPhone/NetworkConfiguration");
        if(node==null){System.out.println("userRoot  NetworkConfiguration node is null");}
        else{
        	System.out.print("user root NetworkConfiguration name: ");
        	System.out.println(node.absolutePath());
        	try
            {
                    node.sync();
            } 
        catch (BackingStoreException ex)
            {
                Logger.getLogger(NetworkConfigurationLoader.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        prefferedNetworkInterface=node.get(
                 "prefferedNetworkInterface", prefferedNetworkInterface);

        if(prefferedNetworkInterface.equals("")||prefferedNetworkInterface ==null)
          {
            configured =false;
            getNetworkIfacesMap();
          }
        else
         {
            configured=(VerifyPrefferredDevice(prefferedNetworkInterface))
                    ? true:false;
            //TODO WTF if it is not
         }
        if(configured)this.networkConfiguration =
                new NetworkConfiguration(netIfaceMap.get(prefferedNetworkInterface));
    }
//si l'initialisation de la configuration à échoué la configuration est null
 /*on l'initialise avec la valeur configurer par l'utilisateur
  sinon on renvoi la configuration chargé depuis le fichier et vérifiée
  on appel cette fonction que si le loader est dans l'état configuré*/
    public NetworkConfiguration getNetworkConfigurationInstance()
    {
        if(this.networkConfiguration==null)
        {
            return new NetworkConfiguration(netIfaceMap.get(prefferedNetworkInterface));
        }
        else
        {
            return this.networkConfiguration;
        }
    }

    
    public String []getNetworkDevicesNames()
    {
        
            networkDevicesNames=netIfaceMap.keySet().toArray(new String[netIfaceMap.size()]);
            System.out.println();
          return networkDevicesNames;
        
    }
    
    public HashMap<String, NetworkIface> getNetworkIfacesMap(){
    	if(netIfaceMap.size() ==0)
        {
           
            String displayName="";
            String name="";

            while(networkDevices.hasMoreElements())
                {
            	NetworkInterface netIface =networkDevices.nextElement() ;
            	NetworkIface iface = new NetworkIface();
            	
                name=netIface.getName();
            	displayName=netIface.getDisplayName();
            	iface.setName(name);
            	iface.setDisplayName(displayName);
            	
            	 
            	
            	 if(netIface.getParent()==null){
            		 System.out.println("physical iface");
            		 
            	 }
            	if(netIface.isVirtual()==true){
            		System.out.println("virtual iface");
            	}
            	try {
            		if(netIface.getHardwareAddress()!=null){
            		byte[] mac = netIface.getHardwareAddress();
            		 
            		
             
            		StringBuilder sb = new StringBuilder();
            		for (int i = 0; i < mac.length; i++) {
            			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
            		}
            		String macAddress = sb.toString();
            		iface.setHWAddress(macAddress);
            		
            		}
            		
            		 if(netIface.isLoopback()){
            			 System.out.println("loopback iface");
            			 }
					if(netIface.isPointToPoint()){
						System.out.println("point to point iface");
						}
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                    netIfaceMap.put(displayName, iface);

                }
           
          return netIfaceMap;
        }
        else return netIfaceMap;
    }

    private Enumeration<NetworkInterface> searchNetWorkInterfaces()
    {
        Enumeration<NetworkInterface> devices=null;
        try
            {
                return  devices= NetworkInterface.getNetworkInterfaces();
             
            }
        catch (SocketException ex)
            {
                Logger.getLogger(NetworkConfiguration.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
        finally
            {
                return devices;
            }

    }

    public Enumeration<NetworkInterface> getNetworkDevices()
    {
        return networkDevices;
    }

    private boolean VerifyPrefferredDevice(String prefferredDevice)
    {
      boolean isOK =false;
      HashMap<String, NetworkIface> ifacesMap = getNetworkIfacesMap();
      if(!ifacesMap.isEmpty()){
    	  if (ifacesMap.containsKey(prefferredDevice))isOK=true;
      }
      return isOK;
    }

    public void setPrefferedNetworkInterface(String prefferedNetworkInterface)
    {
        this.prefferedNetworkInterface = prefferedNetworkInterface;
        
        node.put("prefferedNetworkInterface",prefferedNetworkInterface);
        try
            {
                    node.sync();
            } 
        catch (BackingStoreException ex)
            {
                Logger.getLogger(NetworkConfigurationLoader.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
    }
    
    public String getNetworkInterfaceByDisplayName(String displayName){
    	String device ="";
    	 
    		 while(networkDevices.hasMoreElements())
             {
         	NetworkInterface interface1 =networkDevices.nextElement() ;
         		
                
         	device=interface1.getDisplayName();
                System.out.println( interface1.getDisplayName());
                
                
                 
                if(interface1.getDisplayName()==displayName){
                	device=interface1.getName();
                	System.out.println( interface1.getName());
                	return device;
                }

                     
         
             }
    	return device;
    }

    public boolean isConfigured() {
        return configured;
    }

    public void setConfigured(boolean configured) {
        this.configured = configured;
    }

    
}
