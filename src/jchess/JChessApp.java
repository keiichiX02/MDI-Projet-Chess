/*
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jchess;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import java.awt.Window;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JDialog;

import jchess.core.Game;
import jchess.display.windows.DrawLocalSettings;
import jchess.utils.Settings;

import org.apache.log4j.PropertyConfigurator;

/**
 * The main class of the application.
 */
public class JChessApp extends SingleFrameApplication {
    
    protected static JChessView javaChessView; 
    
    /**
     * htd
     */
    public final static String LOG_FILE = "log4j.properties"; //original one
    //public final static String LOG_FILE = "C:\\workspace_for_all_projects\\MDI_TP67projet\\ProjetChess\\log4j.properties"; 
    //
    
    public final static String MAIN_PACKAGE_NAME = "jchess";

    /**
     * @return the jcv
     */
    public static JChessView getJavaChessView()
    {
        return javaChessView;
    }
     
    /**
     * At startup create and show the main frame of the application.
     */
    @Override 
    protected void startup() 
    {
    	/**
    	 * original code
        javaChessView = new JChessView(this);
        show(getJavaChessView());
    	*/
    	
    	/**
    	 * new code htd
    	 */
    	javaChessView = new JChessView(this); //created main window
    	show(getJavaChessView()); //show main window
    	
    	String p1 = Biblio.generateUUID() + "_White";
    	String p2 = Biblio.generateUUID() + "_Black";
    	Game newGUI = JChessApp.getJavaChessView().addNewTab(p1 + " vs " + p2); //added new tab in main window
    	
    	Settings newSettings = newGUI.getSettings();
    	newSettings.getPlayerWhite().setName(p1);
    	newSettings.getPlayerBlack().setName(p2); //prepared a new setting where players' name is predefined
    	
        newGUI.newGame();//start new Game
        //this.parent.setVisible(false);//hide parent
        JChessApp.getJavaChessView().getActiveTabGame().repaint();
        JChessApp.getJavaChessView().setActiveTabGame(JChessApp.getJavaChessView().getNumberOfOpenedTabs()-1); //started new game, show it as main tab for user
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override 
    protected void configureWindow(Window root) {}

    /**
     * A convenient static getter for the application instance.
     * @return the instance of JChessApp
     */
    public static JChessApp getApplication() 
    {
        return Application.getInstance(JChessApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) 
    {
        launch(JChessApp.class, args);
        Properties logProp = new Properties();
        try
        {   
            logProp.load(JChessApp.class.getClassLoader().getResourceAsStream(LOG_FILE)); 
            PropertyConfigurator.configure(logProp);
        }
        catch (NullPointerException | IOException e)
        {
            System.err.println("Logging not enabled : "+e.getMessage());
        } 
    }
}
