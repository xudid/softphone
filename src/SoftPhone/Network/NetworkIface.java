package SoftPhone.Network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NetworkIface {
	private String DisplayName="";
	private String Name="";
	private String HWAddress="";
	private boolean status=false;
	private String IPAddress="";
	private boolean isLoopback=false;
	private String deviceType="undefined";
	
	public boolean isLoopback() {
		return isLoopback;
	}

	public void setLoopback(boolean isLoopback) {
		this.isLoopback = isLoopback;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public NetworkIface() {
		
	}

	public NetworkIface(String displayName, String name, String hWAddress) {
		super();
		DisplayName = displayName;
		Name = name;
		HWAddress = hWAddress;
	}

	public String getDisplayName() {
		return DisplayName;
	}

	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getHWAddress() {
		return HWAddress;
	}

	public void setHWAddress(String hWAddress) {
		HWAddress = hWAddress;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String[] getIPAddress() {
		List<String> ipAdresses = new ArrayList<String>();
		
		NetworkInterface iface;
		try {
			iface = NetworkInterface.getByName(Name);
			Enumeration ipAdressesEnumeration = iface.getInetAddresses();
		    while (ipAdressesEnumeration.hasMoreElements())
		    {
		        InetAddress ip = (InetAddress) ipAdressesEnumeration.nextElement();
		        ipAdresses.add(ip.getHostAddress());
		    }
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] ipArray = new String[ ipAdresses.size() ];
		ipAdresses.toArray( ipArray );
		return ipArray;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}
	
	
	
	

}
