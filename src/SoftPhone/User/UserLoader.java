/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SoftPhone.User;

import SoftPhone.User.Account.Account;
import SoftPhone.User.Identity.Identity;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author didier
 */
public class UserLoader {
    private boolean configured=false;
    private  Account  account =null;
    private XStream xstream;
    private String accountPath;
    private User user;

    public UserLoader() {
    }

    public UserLoader(String accountPath) {
        this.accountPath = accountPath;
        this.accountPath=accountPath;
        xstream = new XStream(new DomDriver());
        xstream.alias("User", User.class);
        xstream.alias("Identity",Identity.class);
       
        load();
    }

    private void load() {
        String fromFile = accountPath+"\\User.xml";
        String xml ="";
        File f = new File(fromFile);
        byte[] bytes = new byte[(int) f.length()];
        FileInputStream fis;

        try {
                fis = new FileInputStream(f);
                fis.read(bytes);
                fis.close();
                xml = new String(bytes);
                user = (User) xstream.fromXML(xml);
                configured=verifyUser();
             }
        catch (FileNotFoundException ex)
        {
            try
                {
                    f.createNewFile();
                     user =new User();
                     xml =xstream.toXML(user);
                     FileOutputStream fos =new FileOutputStream(f);
                     xstream.toXML(user, fos);
                     fos.close();
                    load();
                }
            catch (IOException ex1)
            {
                Logger.getLogger(UserLoader.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        catch (IOException ex) {
            Logger.getLogger(UserLoader.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    private boolean verifyUser() {
        return true;
    }

    

}
