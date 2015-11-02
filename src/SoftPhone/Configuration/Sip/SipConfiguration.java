/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SoftPhone.Configuration.Sip;

/**
 *
 * @author didier
 */
public class SipConfiguration {

    private String sipListenningPort = "5060";

   
public SipConfiguration() {
    }
    

    /**
     * Get the value of sipListenningPort
     *
     * @return the value of sipListenningPort
     */
    public String getSipListenningPort() {
        return sipListenningPort;
    }

    /**
     * Set the value of sipListenningPort
     *
     * @param sipListenningPort new value of sipListenningPort
     */
    public void setSipListenningPort(String sipListenningPort) {
        this.sipListenningPort = sipListenningPort;
    }


    

   

    

}
