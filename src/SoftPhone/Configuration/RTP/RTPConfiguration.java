/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SoftPhone.Configuration.RTP;

/**
 *
 * @author Administrateur
 */
public class RTPConfiguration {
private int firstPort=8000;
private int lastPort=8050;

    public RTPConfiguration(int first ,int last)
    {
        this.firstPort=first;
        this.lastPort=last;
    }

    public int getFirstPort() {
        return firstPort;
    }

    public int getLastPort() {
        return lastPort;
    }
    
    

}
