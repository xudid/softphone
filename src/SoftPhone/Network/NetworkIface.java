package SoftPhone.Network;

public class NetworkIface {
	private String DisplayName="";
	private String Name="";
	private String HWAddress="";
	private boolean status=false;
	private String IPAddress="";
	private boolean isLoopback=false;
	
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

	public String getIPAddress() {
		return IPAddress;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}
	
	
	
	

}
