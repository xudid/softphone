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

import SoftPhone.Configuration.Media.JavaSoundDetector;
import SoftPhone.Configuration.Media.MediaDeviceDetector;
import SoftPhone.Network.NetworkIface;

/**
 *
 * @author didier TODO design a Network interface class design a Network
 *         interface structure where we can retrieve an iface by displayName
 *         change the attribut preffered network interface in
 *         networkConfiguration class by an attribut typed with networkinterface
 *         class put a status label in networkconfiguration panel ?design a
 *         networkinterfaces monitor /manger to deal with ifaces status and
 *         informations ?
 */
public class NetworkConfigurationLoader {
	private static final Logger logger = Logger.getLogger(JavaSoundDetector.class.getName());
	private String prefferedNetworkInterface = "";
	private String[] networkDevicesNames;
	private Enumeration<NetworkInterface> networkDevices;
	private boolean configured = false;
	private final Preferences node;
	private NetworkConfiguration networkConfiguration;
	private HashMap<String, NetworkIface> netIfaceMap;

	public NetworkConfigurationLoader() {
		String OS = System.getProperty("os.name").toLowerCase();
		Logger.getLogger(MediaDeviceDetector.class.getName()).log(Level.INFO, "System OS: " + OS );

		networkDevices = searchNetWorkInterfaces();
		netIfaceMap = this.initNetworkIfacesMap();

		networkDevicesNames = new String[0];
		Preferences root = Preferences.userRoot();
		if (root == null) {
			Logger.getLogger(MediaDeviceDetector.class.getName()).log(Level.INFO,"userRoot node is null");
			System.out.println("");
		} else {
			Logger.getLogger(MediaDeviceDetector.class.getName()).log(Level.INFO,
					"User root name: " + root.name());

		}
		node = root.node("/SoftPhone/NetworkConfiguration");
		if (node == null) {
			Logger.getLogger(MediaDeviceDetector.class.getName()).log(Level.INFO,
					"User root NetworkConfiguration node is null");

		} else {
			Logger.getLogger(MediaDeviceDetector.class.getName()).log(Level.INFO,
					"User root NetworkConfiguration name: " + node.absolutePath());

			try {
				node.sync();
			} catch (BackingStoreException ex) {
				Logger.getLogger(NetworkConfigurationLoader.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		prefferedNetworkInterface = node.get("prefferedNetworkInterface", prefferedNetworkInterface);

		if (prefferedNetworkInterface.equals("") || prefferedNetworkInterface == null) {
			configured = false;
			getNetworkIfacesMap();
		} else {
			configured = (VerifyPrefferredDevice(prefferedNetworkInterface)) ? true : false;
			// TODO WTF if it is not
		}
		if (configured)
			this.networkConfiguration = new NetworkConfiguration(netIfaceMap.get(prefferedNetworkInterface));
	}

//si l'initialisation de la configuration à échoué la configuration est null
	/*
	 * on l'initialise avec la valeur configurer par l'utilisateur sinon on renvoi
	 * la configuration chargé depuis le fichier et vérifiée on appel cette fonction
	 * que si le loader est dans l'état configuré
	 */
	public NetworkConfiguration getNetworkConfigurationInstance() {
		if (this.networkConfiguration == null) {
			return new NetworkConfiguration(netIfaceMap.get(prefferedNetworkInterface));
		} else {
			return this.networkConfiguration;
		}
	}

	public String[] getNetworkDevicesNames() {

		networkDevicesNames = netIfaceMap.keySet().toArray(new String[netIfaceMap.size()]);

		return networkDevicesNames;

	}

	public HashMap<String, NetworkIface> initNetworkIfacesMap() {
		HashMap<String, NetworkIface> map = new HashMap<String, NetworkIface>();
		while (this.networkDevices.hasMoreElements()) {
			NetworkInterface netIface = networkDevices.nextElement();
			NetworkIface iface = new NetworkIface();
			iface.setName(netIface.getName());
			iface.setDisplayName(netIface.getDisplayName());

			try {
				if (netIface.getHardwareAddress() != null) {
					byte[] mac = netIface.getHardwareAddress();
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < mac.length; i++) {
						sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
					}
					String macAddress = sb.toString();
					iface.setHWAddress(macAddress);

					if (netIface.getParent() == null) {
						iface.setDeviceType("Physical");
					}
					if (netIface.isVirtual() == true) {
						iface.setDeviceType("Virtual");
					}
					if (netIface.isLoopback()) {
						iface.setLoopback(true);
					}
					if (netIface.isPointToPoint()) {
						iface.setDeviceType("Point to Point");
					}

				}

			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (iface.getDeviceType().equalsIgnoreCase("physical")) {
				map.put(iface.getDisplayName(), iface);
			}

		}
		return map;
	}

	public HashMap<String, NetworkIface> getNetworkIfacesMap() {

		return this.netIfaceMap;
	}

	private Enumeration<NetworkInterface> searchNetWorkInterfaces() {
		Enumeration<NetworkInterface> devices = null;
		try {
			return devices = NetworkInterface.getNetworkInterfaces();

		} catch (SocketException ex) {
			Logger.getLogger(NetworkConfiguration.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			return devices;
		}

	}

	public NetworkIface getNetworkIfaceByName(String name) {
		NetworkIface iface = null;

		if (this.netIfaceMap.containsKey(name)) {
			iface = this.netIfaceMap.get(name);
		}

		return iface;
	}

	public Enumeration<NetworkInterface> getNetworkDevices() {
		return networkDevices;
	}

	private boolean VerifyPrefferredDevice(String prefferredDevice) {
		boolean isOK = false;
		HashMap<String, NetworkIface> ifacesMap = getNetworkIfacesMap();
		if (!ifacesMap.isEmpty()) {
			if (ifacesMap.containsKey(prefferredDevice))
				isOK = true;
		}
		return isOK;
	}

	public void setPrefferedNetworkInterface(String prefferedNetworkInterface) {
		this.prefferedNetworkInterface = prefferedNetworkInterface;

		node.put("prefferedNetworkInterface", prefferedNetworkInterface);
		try {
			node.sync();
		} catch (BackingStoreException ex) {
			Logger.getLogger(NetworkConfigurationLoader.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public String getNetworkInterfaceByDisplayName(String displayName) {
		String device = "";

		while (networkDevices.hasMoreElements()) {
			NetworkInterface interface1 = networkDevices.nextElement();

			device = interface1.getDisplayName();

			if (interface1.getDisplayName() == displayName) {
				device = interface1.getName();

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
