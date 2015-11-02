/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package testjainsipinvite;

import SoftPhone.Network.NetworkUtils;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;





/**
 *
 * @author didier
 */
public class NetworkUtilsTest extends TestCase{

    public NetworkUtilsTest() {
    }

   
    public static void setUpClass() throws Exception {
    }

  
    public static void tearDownClass() throws Exception {
    }

    
    public void setUp() {
    }

  
    public void tearDown() {
    }

    /**
     * Test of StringToInet4Address method, of class NetworkUtils.
     */
   
    public void testStringToInet4Address() throws UnknownHostException {
        System.out.println("StringToInet4Address");
        String IPV4 ="192.168.1.20";
        InetAddress expResult = InetAddress.getByName(IPV4);
        InetAddress result = NetworkUtils.StringToInet4Address(IPV4);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }

    /**
     * Test of isIPV4 method, of class NetworkUtils.
     */
  
    public void testIsIPV4() {
        System.out.println("isIPV4");
        String string = "192.168.1.1";
        boolean expResult = true;
        boolean result = NetworkUtils.isIPV4(string);
        assertEquals(expResult, result);
       
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isLoopback method, of class NetworkUtils.
     */
    
    public void testIsLoopback() {
        System.out.println("isLoopback");
        String string = "127.0.0.1";
        boolean expResult =true;
        boolean result = NetworkUtils.isLoopback(string);
        assertEquals(expResult, result);
        string = "137.0.0.1";
        expResult =false;
        result = NetworkUtils.isLoopback(string);
        assertEquals(expResult, result);
        string = "0.0.0.0";
        expResult =false;
        result = NetworkUtils.isLoopback(string);
        assertEquals(expResult, result);
        string = "255.255.255.255";
        expResult =false;
        result = NetworkUtils.isLoopback(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }

    /**
     * Test of ValidatePortNumber method, of class NetworkUtils.
     */
 
    public void testValidatePortNumber() {
        System.out.println("ValidatePortNumber");
        int port = 1024;
        boolean expResult = true;
        boolean result = NetworkUtils.ValidatePortNumber(port);
        assertEquals(expResult, result);

         port = 65536;
        expResult = false;
        result = NetworkUtils.ValidatePortNumber(port);
        assertEquals(expResult, result);
       //  TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getFreePort method, of class NetworkUtils.
     */
    
   public void testGetFreePort() {
        System.out.println("getFreePort");
        int minexpResult =1024;
        int maxresult=65535;
        int tempresult = NetworkUtils.getFreePort();
        System.out.println("getFreePort returned :"+Integer.toString(tempresult));
        assertTrue(tempresult>=minexpResult && tempresult<=maxresult);
        
       
       Socket   bindingsocket = new Socket();
        try {
            bindingsocket.bind(new InetSocketAddress(tempresult));
            // TODO review the generated test code and remove the default call to fail.
            //fail("The test case is a prototype.");

        int result =NetworkUtils.getFreePort();
        System.out.println("getFreePort returned :"+Integer.toString(result));
        assertTrue((tempresult>=minexpResult && tempresult<=maxresult)&&(tempresult!=result));
        bindingsocket.close();
        } catch (IOException ex) {
            Logger.getLogger(NetworkUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}