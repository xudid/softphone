/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Media;

import javax.media.format.AudioFormat;
import javax.media.rtp.SessionAddress;

/**
 *
 * @author didier
 */
public class RTPSessionConfiguration {

    private SessionAddress localSessionAddress=null;
    private SessionAddress remoteSessionAddress=null;
    private String audioFormat=" ";
    private int packetizationTime=20;

    /*RTPSessionConfiguration
     * @param lsa adresse de session  media local
     * @param rsa adresse de session media distant
     * @param aft format audio de la session
     */
    public RTPSessionConfiguration(SessionAddress lsa, SessionAddress rsa, String aft )
    {
       this.audioFormat=aft;
        this.localSessionAddress=lsa;
        this.remoteSessionAddress=rsa;
    }
/*return retourne une chaîne de caractère représentant le format audio de la session
 */
    public String getAudioFormat()
    {
        return audioFormat;
    }

   
/*@return retourne l'adresse media local de la session */
    public SessionAddress getLocalSessionAddress()
    {
        return localSessionAddress;
    }
/*@return retourne l'adresse media distant de la session */
    public SessionAddress getRemoteSessionAddress()
    {
        return remoteSessionAddress;
    }
/*@return retourne la duree de donnee audio par paquet de la session */
    public int getPacketizationTime()
    {
        return packetizationTime;
    }
/*@param packetizationTime  fixe la duree de donnee audio par paquet de la session */
    public void setPacketizationTime(int packetizationTime)
    {
        this.packetizationTime = packetizationTime;
    }









}
