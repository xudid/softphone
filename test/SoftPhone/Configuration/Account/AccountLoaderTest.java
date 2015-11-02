/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Configuration.Account;

import junit.framework.TestCase;
import SoftPhone.Protocol.Sip.Account.SipAccount;




/**
 *
 * @author didier
 */
public class AccountLoaderTest extends TestCase{

    public AccountLoaderTest() {
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
     * Test of getConfiguredAccount method, of class AccountLoader.
     */
   
    public void testGetConfiguredAccount() {
        System.out.println("getConfiguredAccount");
        AccountLoader instance = new AccountLoader(null);
        SipAccount expResult = null;
        SipAccount result = instance.getConfiguredAccount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}