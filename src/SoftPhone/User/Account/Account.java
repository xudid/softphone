/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SoftPhone.User.Account;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.*;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author didier
 */
public class Account {
    private String name ="";
    private String protocolName="";
    private Credentials credentials=new Credentials();

    public Account() {
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }



    

}
