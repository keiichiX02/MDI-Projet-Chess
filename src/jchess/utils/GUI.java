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

package jchess.utils;

import jchess.JChessApp;
import jchess.core.Game;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.Properties;
import java.io.FileOutputStream;
import org.apache.log4j.Logger;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import jchess.core.Colors;

/** 
 * @author: Mateusz Sławomir Lach ( matlak, msl )
 * @author: Damian Marciniak
 * Class representing the game interface which is seen by a player and
 * where are lockated available for player opptions, current games and where
 * can he start a new game (load it or save it)
 */
public class GUI
{

    private static final Logger LOG = Logger.getLogger(GUI.class);
    
    private static final String CONFIG_FILENAME = "config.txt";
    
    private Game game;
    
    private static final Properties CONFIG_FILE = GUI.getConfigFile();
    
    private static final String IMAGE_PATH = "theme/%s/images/%s";

    /**
     * Default constructor
     */
    public GUI()
    {
        this.game = new Game();
//        //htd
//        this.game.addNewTab("bolo" + " vs " + "bala");
        
    }/*--endOf-GUI--*/

    public static Image loadPieceImage(String pieceName, Colors color, int size, String fileExt)
    {
        String colorSymbol = String.valueOf(color.getSymbol()).toUpperCase();
        return loadImage(pieceName + "-" + colorSymbol + size + fileExt);
    }
    
    /**
     * Method load image by a given name with extension
     * @param name : string of image to load for ex. "chessboard.jpg"
     * @returns  : image or null if cannot loadng
     */
    public static Image loadImage(String name)
    {
        if (getConfigFile() == null)
        {
            return null;
        }
        Image img = null;
        try
        {
            String imageLink = String.format(IMAGE_PATH, getConfigFile().getProperty("THEME", "default"), name);
            LOG.debug("THEME: " + getConfigFile().getProperty("THEME"));
            img = ImageIO.read(JChessApp.class.getResourceAsStream(imageLink)); 
        }
        catch (Exception e)
        {
            LOG.error("some error loading image!, message: " + e.getMessage() + 
                      " stackTrace: " + e.getStackTrace().toString()
            );
        }
        return img;
    }/*--endOf-loadImage--*/


    public static boolean themeIsValid(String name)
    {
        //TODO: implement me
        return true;
    }

    /**
     * Static function to get current location of JAR file.
     * @return 
     */
    public static String getJarPath()
    {
        String path = GUI.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        path = path.replaceAll("[a-zA-Z0-9%!@#$%^&*\\(\\)\\[\\]\\{\\}\\.\\,\\s]+\\.jar", "");
        int lastSlash = path.lastIndexOf(File.separator); 
        if(path.length()-1 == lastSlash)
        {
            path = path.substring(0, lastSlash);
        }
        path = path.replace("%20", " ");
        return path;
    }

    public static Properties getConfigFile()
    {
        Properties defConfFile = new Properties();
        Properties confFile = new Properties();
        File outFile = new File(GUI.getJarPath() + File.separator + CONFIG_FILENAME);
        try
        {
            defConfFile.load(GUI.class.getResourceAsStream(CONFIG_FILENAME));
        }
        catch (IOException e)
        {
            LOG.error("IOException, some error during getting config file!, message: " + e.getMessage() +
                      " stackTrace: " + e.getStackTrace().toString()
            );
        }
        if (!outFile.exists())
        {
            try
            {
                defConfFile.store(new FileOutputStream(outFile), null);
            }
            catch (IOException e)
            {
                LOG.error("IOException, some error during getting config file!,, message: " + e.getMessage() + 
                          " stackTrace: " + e.getStackTrace().toString()
                );
            }
        }
        try
        {   
            confFile.load(new FileInputStream(CONFIG_FILENAME));
        }
        catch (IOException e)
        {
            LOG.error("IOException, some error during getting config file!,, message: " + e.getMessage() + 
                      " stackTrace: " + e.getStackTrace().toString()
            );
        }
        return confFile;
    }

    /**
     * @return the game
     */
    public Game getGame()
    {
        return game;
    }
}
