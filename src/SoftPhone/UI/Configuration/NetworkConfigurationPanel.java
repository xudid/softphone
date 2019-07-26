/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SoftPhone.UI.Configuration;

import SoftPhone.Configuration.Network.NetworkConfigurationLoader;
import SoftPhone.Network.NetworkIface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;

/**
 *
 * @author didier
 */
public class NetworkConfigurationPanel extends javax.swing.JPanel {
	private JFrame parent;
	private JPanel interfacepanel;
	private JPanel buttonpanel;
	private ButtonGroup group;
	private JButton cancelButton;
	private JButton okButton;
	private JLabel messageLabel;
	private JComboBox deviceComboBox;
	private String[] networkDeviceNames;
	private JLabel nameDeviceLabel;
	private JLabel macLabel;
	private JLabel deviceTypeLabel;
	private JLabel ipv4Label;
	private NetworkConfigurationLoader configurationLoader;
	private JPanel selectPanel;
	private JPanel ipPanel;

	public NetworkConfigurationPanel(final JFrame parent, String title,
			final NetworkConfigurationLoader networkConfigurationLoader) {
		this.parent = parent;
		this.configurationLoader = networkConfigurationLoader;
		this.networkDeviceNames = networkConfigurationLoader.getNetworkDevicesNames();
		initComponents();

		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title));
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		// No network device found
		if (networkDeviceNames.length == 0) {
			this.messageLabel.setText("Pas de carte a sélectionner .Echec de la configuration");
		}

		// There is only the loopback interface found
		if ((networkDeviceNames.length == 1) & (networkDeviceNames[0].startsWith("lo"))) {
			this.messageLabel.setText("Veuillez verifier la configuration" + "\n de la carte reseau ,"
					+ "\n le cable est peut Ãªtre dÃ©branchÃ© ," + "Probleme de configuration");

		}

		// Network have been choosed and is stored in configuration
		if (networkConfigurationLoader.isConfigured()) {

		} else {

		}

		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				String selection = (String) deviceComboBox.getSelectedItem();
				networkConfigurationLoader.setPrefferedNetworkInterface(selection);
				networkConfigurationLoader.setConfigured(true);
				parent.dispose();
			}
		});

		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				parent.dispose();
			}

		});

		deviceComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				String ifaceName = (String) cb.getSelectedItem();
				NetworkIface iface = configurationLoader.getNetworkIfaceByName(ifaceName);
				if (iface != null) {
					nameDeviceLabel.setText("Interface: " + iface.getName());
					deviceTypeLabel.setText("Type d'interface: " + iface.getDeviceType());
					String[] adresses = iface.getIPAddress();
					ipPanel.removeAll();
					for (String address : adresses) {
						JLabel iplabel = new JLabel(address);
						iplabel.setAlignmentX(LEFT_ALIGNMENT);
						ipPanel.add(iplabel);
						ipPanel.revalidate();
						ipPanel.repaint();
						parent.pack();
					}

					macLabel.setText("MAC: " + iface.getHWAddress());

				}

			}
		});

	}

	private void initComponents() {
		System.out.println(getAlignmentX());
		setAlignmentX(LEFT_ALIGNMENT);
		setAlignmentY(TOP_ALIGNMENT);
		System.out.println(getAlignmentX());
		this.selectPanel = new JPanel();
		this.selectPanel.setBackground(Color.blue);
		this.selectPanel.setAlignmentX(LEFT_ALIGNMENT);
		this.selectPanel.setAlignmentY(TOP_ALIGNMENT);
		System.out.println(selectPanel.getAlignmentY());
		this.deviceComboBox = new JComboBox<String>(this.networkDeviceNames);
		selectPanel.add(this.deviceComboBox);
		selectPanel.setPreferredSize(deviceComboBox.getPreferredSize());

		this.interfacepanel = new JPanel();
		this.interfacepanel.setLayout(new BoxLayout(this.interfacepanel, BoxLayout.PAGE_AXIS));
		this.interfacepanel.setAlignmentX(LEFT_ALIGNMENT);
		this.interfacepanel.setAlignmentY(TOP_ALIGNMENT);
		System.out.println(interfacepanel.getAlignmentX());

		this.messageLabel = new JLabel();
		this.interfacepanel.add(messageLabel);

		String ifaceName = (String) deviceComboBox.getSelectedItem();
		NetworkIface iface = this.configurationLoader.getNetworkIfaceByName(ifaceName);
		if (iface != null) {
			this.nameDeviceLabel = new JLabel("Interface: " + iface.getName());
			this.interfacepanel.add(this.nameDeviceLabel);
			this.deviceTypeLabel = new JLabel("Type d'interface: " + iface.getDeviceType());
			this.interfacepanel.add(this.deviceTypeLabel);
			this.ipPanel = new JPanel();
			this.ipPanel.setLayout(new BoxLayout(this.ipPanel, BoxLayout.PAGE_AXIS));
			this.ipPanel.setAlignmentX(LEFT_ALIGNMENT);
			this.interfacepanel.add(ipPanel);
			this.ipv4Label = new JLabel("IP: ");
			this.ipPanel.add(this.ipv4Label);

			String[] adresses = iface.getIPAddress();
			for (String address : adresses) {
				JLabel iplabel = new JLabel(address);
				this.ipPanel.add(iplabel);
			}

			this.macLabel = new JLabel("MAC: " + iface.getHWAddress());
			this.interfacepanel.add(this.macLabel);

		}

		this.buttonpanel = new JPanel();
		this.okButton = new JButton("Valider");
		this.cancelButton = new JButton("Annuler");
		this.buttonpanel.add(cancelButton);
		this.buttonpanel.add(okButton);
		this.interfacepanel.add(buttonpanel);
		this.interfacepanel.revalidate();
		this.interfacepanel.repaint();
		add(this.selectPanel);

		add(this.interfacepanel);
		

	}

	private String getSelection() {
		return group.getSelection().getActionCommand();
	}

}
