/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SoftPhone.Protocol.Sip.Call;

import SoftPhone.Media.RTPSessionConfiguration;
import javax.sip.message.Request;

/**
 *
 * @author didier
 */
public class CallSession
{
    private String sdpOffer="";
    private String sdpResponse="";
    private RTPSessionConfiguration rtpConf=null;
    private String callSessionID="";
    private boolean established=false;
    private boolean needToAuthenticate=false;
    private CallParticipantInterface localParticipant;
    private CallParticipantInterface distantParticipant;
    private Request currentRequest;
    private Request incommingCallRequest;

    public CallSession(CallParticipantInterface localParticipant, CallParticipantInterface distantParticipant,String offer)
    {
        this.localParticipant = localParticipant;
        this.distantParticipant = distantParticipant;
        this.sdpOffer=offer;
        
    }

    public boolean IsEstablished()
    {
        return established;
    }

    public void setEstablished(boolean established) {
        this.established = established;
    }



    public String getCallSessionID() {
        return callSessionID;
    }

    public void setCallSessionID(String callSessionID) {
        this.callSessionID = callSessionID;
    }

    public CallParticipantInterface getDistantParticipant() {
        return distantParticipant;
    }

    public void setDistantParticipant(CallParticipantInterface distantParticipant) {
        this.distantParticipant = distantParticipant;
    }

    public CallParticipantInterface getLocalParticipant() {
        return localParticipant;
    }

    public void setLocalParticipant(CallParticipantInterface localParticipant) {
        this.localParticipant = localParticipant;
    }

    public String getSdpOffer() {
        return sdpOffer;
    }

    public String getSdpResponse() {
        return sdpResponse;
    }

    public void setSdpResponse(String sdpResponse) {
        this.sdpResponse = sdpResponse;
    }

    public Request getCurrentRequest() {
        return currentRequest;
    }

    public Request getIncommingCallRequest() {
        return incommingCallRequest;
    }

    public void setIncommingCallRequest(Request incommingCallRequest) {
        this.incommingCallRequest = incommingCallRequest;
    }



    public void setCurrentRequest(Request request) {
        this.currentRequest = request;
    }

    public RTPSessionConfiguration getRtpConf() {
        return rtpConf;
    }

    public void setRtpConf(RTPSessionConfiguration rtpConf) {
        this.rtpConf = rtpConf;
    }

    

    










}
