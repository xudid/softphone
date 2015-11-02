/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.Agent;

/**
 *
 * @author didier
 */
public class SipProxy
{
    private int port=5060;
    private String uri="";
    
    public SipProxy(){
    	
    }

    public SipProxy(String uri,int port)
    {
        this.port=port;
        this.uri=uri;
    }

    public int getPort() {
        return port;
    }

    public void setPort(String port)
    {
        this.port = Integer.parseInt(port);
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    


}
