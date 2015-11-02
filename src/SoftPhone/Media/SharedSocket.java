/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SoftPhone.Media;

/**
 *
 * @author PROTELCO
 */

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PushSourceStream;
import javax.media.protocol.SourceTransferHandler;
import javax.media.rtp.OutputDataStream;
import javax.media.rtp.RTPConnector;
public class SharedSocket implements RTPConnector
{
    DatagramSocket rtpSocket =null;
    DatagramSocket rtcpSocket=null;
    InetAddress addr;
    int port;

    SockInputStream dataInStrm = null, ctrlInStrm = null;
    SockOutputStream dataOutStrm = null, ctrlOutStrm = null;


    public PushSourceStream getDataInputStream() throws IOException {
        if (dataInStrm == null) {
	    dataInStrm = new SockInputStream(rtpSocket, addr, port);
	    dataInStrm.start();
	}
	return dataInStrm;

    }

    public OutputDataStream getDataOutputStream() throws IOException {
        if (dataOutStrm == null)
	    dataOutStrm = new SockOutputStream(rtpSocket, addr, port);
	return dataOutStrm;

    }

    public PushSourceStream getControlInputStream() throws IOException {
       if (ctrlInStrm == null) {
	    ctrlInStrm = new SockInputStream(rtcpSocket, addr, port+1);
	    ctrlInStrm.start();
	}
	return ctrlInStrm;

    }

    public OutputDataStream getControlOutputStream() throws IOException {
        if (ctrlOutStrm == null)
	    ctrlOutStrm = new SockOutputStream(rtcpSocket, addr, port+1);
	return ctrlOutStrm;
    }

    public void close() {
        if (dataInStrm != null)
	    dataInStrm.kill();
	if (ctrlInStrm != null)
	    ctrlInStrm.kill();
	rtpSocket.close();
	rtcpSocket.close();

    }

    public void setReceiveBufferSize(int size) throws IOException {
        rtpSocket.setReceiveBufferSize(size);

    }

    public int getReceiveBufferSize() {
      int size=0;
        try {
            size = rtpSocket.getReceiveBufferSize();
        } catch (SocketException ex) {
            Logger.getLogger(SharedSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
      return size;
    }

    public void setSendBufferSize(int size) throws IOException {
         rtpSocket.setSendBufferSize(size);
    }

    public int getSendBufferSize() {
        int size =0;
        try {
           size = rtpSocket.getSendBufferSize();
        } catch (SocketException ex) {
            Logger.getLogger(SharedSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return size;
    }

    public double getRTCPBandwidthFraction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getRTCPSenderBandwidthFraction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sendStunPacket(byte[]data , int port ,int offset ,int len, InetAddress   addr)
    {
        dataOutStrm.write(data ,offset,len,addr ,port);
    }

       /**
     * An inner class to implement an OutputDataStream based on UDP sockets.
     */
    class SockOutputStream implements OutputDataStream {

	DatagramSocket sock;
	InetAddress addr;
	int port;

	public SockOutputStream(DatagramSocket sock, InetAddress addr, int port) {
	    this.sock = sock;
	    this.addr = addr;
	    this.port = port;
	}

	public int write(byte data[], int offset, int len) {
	    try {
		sock.send(new DatagramPacket(data, offset, len, addr, port));
	    } catch (Exception e) {
		return -1;
	    }
	    return len;
	}
        public int write(byte data[], int offset, int len,InetAddress addr , int port)
        {
             try {
		sock.send(new DatagramPacket(data, offset, len, addr, port));
	    } catch (Exception e) {
		return -1;
	    }
	    return len;
        }
    }

 /**
     * An inner class to implement an PushSourceStream based on UDP sockets.
     */
    class SockInputStream extends Thread implements PushSourceStream {

	DatagramSocket sock;
	InetAddress addr;
	int port;
	boolean done = false;
	boolean dataRead = false;

	SourceTransferHandler sth = null;

	public SockInputStream(DatagramSocket sock, InetAddress addr, int port) {
	    this.sock = sock;
	    this.addr = addr;
	    this.port = port;
	}
//mofify here to redirect stun packets
	public int read(byte buffer[], int offset, int length) {
	    DatagramPacket p = new DatagramPacket(buffer, offset, length, addr, port);
	    try {
		sock.receive(p);
	    } catch (IOException e) {
		return -1;
	    }
	    synchronized (this) {
		dataRead = true;
		notify();
	    }
	    return p.getLength();
	}

	public synchronized void start() {
	    super.start();
	    if (sth != null) {
		dataRead = true;
		notify();
	    }
	}

	public synchronized void kill() {
	    done = true;
	    notify();
	}

	public int getMinimumTransferSize() {
	    return 2 * 1024;	// twice the MTU size, just to be safe.
	}

	public synchronized void setTransferHandler(SourceTransferHandler sth) {
	    this.sth = sth;
	    dataRead = true;
	    notify();
	}

	// Not applicable.
	public ContentDescriptor getContentDescriptor() {
	    return null;
	}

	// Not applicable.
	public long getContentLength() {
	    return LENGTH_UNKNOWN;
	}

	// Not applicable.
	public boolean endOfStream() {
	    return false;
	}

	// Not applicable.
	public Object[] getControls() {
	    return new Object[0];
	}

	// Not applicable.
	public Object getControl(String type) {
	    return null;
	}

	/**
	 * Loop and notify the transfer handler of new data.
	 */
	public void run() {
	    while (!done) {

		synchronized (this) {
		    while (!dataRead && !done) {
			try {
			    wait();
			} catch (InterruptedException e) { }
		    }
		    dataRead = false;
		}

		if (sth != null && !done) {
		    sth.transferData(this);
		}
	    }
	}
    }




}
