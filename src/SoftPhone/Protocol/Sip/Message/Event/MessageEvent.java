/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.Message.Event;

/**
 *
 * @author didier
 */
import java.util.EventObject;


//import testjain.SipLayer2;

public class MessageEvent extends EventObject {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	protected long cseqNum;
	protected String method;


    public MessageEvent(Object arg0) {
        super(arg0);
    }
	public long getCseqNum() {
		return cseqNum;
	}
	public String getMethod() {
		return method;
	}





}
