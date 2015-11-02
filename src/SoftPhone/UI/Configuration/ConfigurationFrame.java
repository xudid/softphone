/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.UI.Configuration;

import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author didier
 */
public class ConfigurationFrame extends JFrame
{
    private JPanel configurationPanel=null;
    private JTabbedPane jTabbedPane =null;
    

    public ConfigurationFrame() throws HeadlessException
    {
       
             this.setTitle("Configuration");
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
             this.setSize(500, 500);


    }
    public void setPanel(JPanel panel)
    {
        if(configurationPanel==null)
        {
            configurationPanel  =panel;
            add(configurationPanel);
        }

        else
        {
            remove(configurationPanel);
            configurationPanel  =panel;
            add(configurationPanel);
        }

    }
    
    public void setTabbedPanel(JTabbedPane jTabbedPane)
    {
        if(configurationPanel==null)
        {
            this.jTabbedPane  =jTabbedPane;
            add(this.jTabbedPane);
        }

        else
        {
            remove(configurationPanel);
            this.jTabbedPane  =jTabbedPane;
            add(this.jTabbedPane);
        }

        
    }


}
