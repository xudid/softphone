/*
 * 
 */

package SoftPhone.Media;

/**
 *
 * @author didier
 */

import SoftPhone.Configuration.Sound.AudioConfiguration;
import SoftPhone.Configuration.Media.MediaConfiguration;
import SoftPhone.Configuration.Network.NetworkConfiguration;
import SoftPhone.Network.NetworkUtils;
import SoftPhone.Protocol.Sip.Call.CallParticipantInterface;
import java.io.IOException;
import java.util.logging.*;
import javax.media.*;
import javax.media.control.*;
import javax.media.format.AudioFormat;
import javax.media.protocol.*;
import javax.media.rtp.*;
import javax.media.rtp.event.*;

public class MediaController implements ReceiveStreamListener, ControllerListener {

	private AudioConfiguration soundConfiguration;
	private MediaConfiguration mediaConfiguration;
	private Processor sendingProcessor;
	private DataSource source;
	private NetworkConfiguration networkConfiguration;
	private int localSendingPort;
	private SendStream sendingStream;
	private RTPSessionConfiguration rtpConfiguration;
	private Player player;
	private DataSource incommingsource;
	private boolean outputInitialized = false;
	private RTPManager receivingManager = null;
	private RTPManager sendingManager = null;

	public MediaController(AudioConfiguration soundConfiguration, MediaConfiguration mc, NetworkConfiguration nc) {
		this.soundConfiguration = soundConfiguration;
		this.mediaConfiguration = mc;
		this.networkConfiguration = nc;
	}

	public void initReceivingMedia(CallParticipantInterface remoteCallParticipant) {
		localSendingPort = NetworkUtils.getFreeRtpReceivingPort();
		Logger.getLogger(MediaController.class.getName()).log(Level.INFO,
				"Local RTP Sending port: " + localSendingPort);

		int distReceivingPort = 0;
		initRecevingMedia(networkConfiguration, remoteCallParticipant, localSendingPort, distReceivingPort);
	}

	private void initRecevingMedia(NetworkConfiguration networkConfiguration, CallParticipantInterface rcp,
			int LOCALDATAPORT, int DISTDATAPORT) {

		try {
			receivingManager = RTPManager.newInstance();
			SessionAddress localAddress = new SessionAddress();
			SessionAddress destAddress = new SessionAddress(rcp.getIPAddress(), DISTDATAPORT);
			localAddress.setDataPort(LOCALDATAPORT);
			localAddress.setControlPort(LOCALDATAPORT + 1);
			receivingManager.initialize(localAddress);
			receivingManager.addTarget(destAddress);
		} catch (InvalidSessionAddressException ex) {
			Logger.getLogger(MediaController.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(MediaController.class.getName()).log(Level.SEVERE, null, ex);
		}
		receivingManager.addReceiveStreamListener(this);

	}

	public void update(ReceiveStreamEvent event) {
		if (event instanceof NewReceiveStreamEvent) {
			ReceiveStream receiveStream = event.getReceiveStream();

			try {
				incommingsource = (DataSource) event.getReceiveStream().getDataSource();
				player = Manager.createRealizedPlayer(incommingsource);
				player.start();
			} catch (IOException ex) {
				Logger.getLogger(MediaController.class.getName()).log(Level.SEVERE, null, ex);
			} catch (NoPlayerException ex) {
				Logger.getLogger(MediaController.class.getName()).log(Level.SEVERE, null, ex);
			} catch (CannotRealizeException ex) {
				Logger.getLogger(MediaController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		if (event instanceof RemotePayloadChangeEvent) {

			if (player != null) {

				player.stop();
				receivingManager.removeReceiveStreamListener(this);
				player.close();

				try {

					incommingsource = event.getReceiveStream().getDataSource();
					incommingsource.connect();
					player = Manager.createRealizedPlayer(incommingsource);
					if (player == null) {
						Logger.getLogger(MediaController.class.getName()).log(Level.SEVERE, "Could not create player");
						return;
					}
					while (player.getState() != Player.Realized) {

					}
					receivingManager.addReceiveStreamListener(this);
					player.start();

				} catch (Exception ex) {
					Logger.getLogger(MediaController.class.getName()).log(Level.SEVERE, null, ex);
					
				}

			}
		}
	}

	public void configureCaptureMedia() {
		try {
			String device = soundConfiguration.getPreferredSoundDevice();
			Logger.getLogger(MediaController.class.getName()).log(Level.INFO, "Configure capture media with: "+device);
		

			CaptureDeviceInfo captureDevice = CaptureDeviceManager.getDevice(device);
			sendingProcessor = Manager.createProcessor(captureDevice.getLocator());
			Logger.getLogger(MediaController.class.getName()).log(Level.INFO, "Start to configfure processor");

			sendingProcessor.configure();
			while (sendingProcessor.getState() != Processor.Configured) {
				Logger.getLogger(MediaController.class.getName()).log(Level.INFO,
						" Please wait procsessor configuration is running");
			}
			Logger.getLogger(MediaController.class.getName()).log(Level.INFO, " Processor ok");

		}

		catch (IOException ex) {
			Logger.getLogger(MediaController.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoProcessorException ex) {
			Logger.getLogger(MediaController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void startSendingMedia(RTPSessionConfiguration configuration) {
		this.rtpConfiguration = configuration;
		AudioFormat audioFormat = new AudioFormat(configuration.getAudioFormat());

		sendingProcessor.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW_RTP));
		TrackControl[] track = sendingProcessor.getTrackControls();
		boolean encodingOk = false;
		for (int i = 0; i < track.length; i++) {
			if (!encodingOk && track[i] instanceof FormatControl) {
				/*
				 * Si le flux permet le réglage du format on tente de configurer celui-ci sinon
				 * on ne l'active pas FormatControl.setFormat retourne null si le format n'est
				 * pas supporté dans ce cas le flux n'est pas activé dans le processor; Dès
				 * qu'un flux est configuré avrc un format on sort de la boucle
				 */

				if (((FormatControl) track[i]).setFormat(audioFormat) == null) {
					track[i].setEnabled(false);
				} else {
					encodingOk = true;
				}
			} else {

				track[i].setEnabled(false);
			}
		}
		sendingProcessor.addControllerListener(this);
		sendingProcessor.realize();

	}

	public void controllerUpdate(ControllerEvent event) {
		if (event instanceof RealizeCompleteEvent && !outputInitialized) {
			Logger.getLogger(MediaController.class.getName()).log(Level.INFO,
					" Realize CompleteEvent received updating processor");

			initializeOutput();
			sendingProcessor.start();
		}
	}

	private void initializeOutput() {

		SessionAddress localSessionAddress = rtpConfiguration.getLocalSessionAddress();
		SessionAddress remoteSessionAdress = rtpConfiguration.getRemoteSessionAddress();

		source = sendingProcessor.getDataOutput();

		sendingManager = RTPManager.newInstance();
		try {
			sendingManager.initialize(localSessionAddress);
			Logger.getLogger(MediaController.class.getName()).log(Level.INFO,
					"Local address" + localSessionAddress.toString());

			sendingManager.addTarget(remoteSessionAdress);
			/*
			 * createSendStream(DataSource dataSource,int streamIndex)
			 */
			sendingStream = sendingManager.createSendStream(source, 0);
			sendingStream.start();
			source.start();
		} catch (Exception ex) {
			Logger.getLogger(MediaController.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public void StopStreaming() {
		closeCaptureDevice();
		stopReceivingMedia();

		try {
			if (source != null) {
				source.disconnect();
				source.stop();
			}
		} catch (IOException ex) {
			Logger.getLogger(MediaController.class.getName()).log(Level.SEVERE, null, ex);
		}
		if (sendingStream != null)
			sendingStream.close();
		if (sendingManager != null)
			sendingManager.dispose();
	}

	public void closeCaptureDevice() {
		sendingProcessor.removeControllerListener(this);
		if (sendingProcessor.getState() <= Processor.Configured)

		{
			
			Logger.getLogger(MediaController.class.getName()).log(Level.INFO, "Sending processor state: "+sendingProcessor.getState());
			sendingProcessor.deallocate();
			Logger.getLogger(MediaController.class.getName()).log(Level.INFO, "Sending processor state: "+sendingProcessor.getState());
			Logger.getLogger(MediaController.class.getName()).log(Level.INFO, "Sending processor state: "+sendingProcessor.getState());
		
			Logger.getLogger(MediaController.class.getName()).log(Level.INFO, "Sending processor Unrealized");
			sendingProcessor.close();
			Logger.getLogger(MediaController.class.getName()).log(Level.INFO, "Sending processor closed ");
			
		} else {
			sendingProcessor.stop();
			sendingProcessor.close();

		}
		sendingProcessor = null;

	}

	public void stopReceivingMedia() {
		receivingManager.removeReceiveStreamListener(this);

		receivingManager.removeTargets("");
		receivingManager.dispose();
		receivingManager = null;

		if (player != null) {
			player.stop();
			player.deallocate();
			player.close();
		}

	}

}
