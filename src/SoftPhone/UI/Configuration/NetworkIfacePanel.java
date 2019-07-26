package SoftPhone.UI.Configuration;

import javax.swing.JPanel;

import SoftPhone.Network.NetworkIface;

import javax.swing.JLabel;

public class NetworkIfacePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4209023894437004921L;
	
	private NetworkIface iface = null;
	
	
	
	public NetworkIfacePanel(NetworkIface iface) {
		super();
		this.iface= iface;
	}



	private void initComponents() 
    {
		
    }
	
	

}
