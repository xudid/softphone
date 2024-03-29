/**
 *
 *
 *
*
 * SIP Communicator, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 *
 * File based on:
 * @(#)JavaSoundDetector.java   1.2 01/03/13
 * Copyright (c) 1999-2001 Sun Microsystems, Inc. All Rights Reserved.
 */

package SoftPhone.Configuration.Media;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
this class aims to detect if there is an audio device detected by javaSound
*/

import javax.sound.sampled.*;

public class JavaSoundDetector {
	private static final Logger logger = Logger.getLogger(JavaSoundDetector.class.getName());

	boolean supported = false;

	public JavaSoundDetector() {
		try {
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, null, AudioSystem.NOT_SPECIFIED);
			Logger.getLogger(MediaDeviceDetector.class.getName())
            .log(Level.INFO, "Dataline info ? ");
			supported = AudioSystem.isLineSupported(info);
		} catch (Exception ex) {
			supported = false;
		}
	}

	public boolean isSupported() {
		return supported;
	}
}
