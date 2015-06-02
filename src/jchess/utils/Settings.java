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

/*
 * Authors:
 * Mateusz Sławomir Lach ( matlak, msl )
 * Damian Marciniak
 */
package jchess.utils;

import jchess.core.Player;
import java.io.Serializable;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import jchess.JChessApp;
import jchess.core.Colors;

/** Class representings game settings available for the current player
 */
public class Settings implements Serializable
{
    private static final Logger LOG = Logger.getLogger(Settings.class);
    
    private static ResourceBundle loc = null;
    
    protected int timeForGame;
    
    protected boolean runningChat;
    
    protected boolean runningGameClock;
    
    /**
     * tel us if player choose time 4 game or it's infinity
     */
    protected boolean timeLimitSet = false;
    
    protected boolean upsideDown;
    
    protected boolean displayLegalMovesEnabled = true;

    /**
     * @return the runningChat
     */
    public boolean isRunningChat()
    {
        return runningChat;
    }

    /**
     * @return the runningGameClock
     */
    public boolean isRunningGameClock()
    {
        return runningGameClock;
    }

    /**
     * @return the timeLimitSet
     */
    public boolean isTimeLimitSet()
    {
        return timeLimitSet;
    }
   
    public boolean isUpsideDown()
    {
        return upsideDown;
    }

    /**
     * @return the gameMode
     */
    public gameModes getGameMode()
    {
        return gameMode;
    }

    /**
     * @return the playerWhite
     */
    public Player getPlayerWhite()
    {
        return playerWhite;
    }

    /**
     * @return the playerBlack
     */
    public Player getPlayerBlack()
    {
        return playerBlack;
    }

    /**
     * @return the gameType
     */
    public gameTypes getGameType()
    {
        return gameType;
    }

    /**
     * @return the renderLabels
     */
    public boolean isRenderLabels()
    {
        return renderLabels;
    }
     
    public void setRenderLabels(boolean renderLabels)
    {
        this.renderLabels = renderLabels;
    }

    /**
     * @param upsideDown the upsideDown to set
     */
    public void setUpsideDown(boolean upsideDown)
    {
        this.upsideDown = upsideDown;
    }

    /**
     * @param gameMode the gameMode to set
     */
    public void setGameMode(gameModes gameMode)
    {
        this.gameMode = gameMode;
    }

    /**
     * @param gameType the gameType to set
     */
    public void setGameType(gameTypes gameType)
    {
        this.gameType = gameType;
    }

    /**
     * @param timeForGame the timeForGame to set
     */
    public void setTimeForGame(int timeForGame)
    {
        this.timeLimitSet = true;
        this.timeForGame = timeForGame;
    }

    /**
     * @return the isDisplayLegalMovesEnabled
     */
    public boolean isDisplayLegalMovesEnabled()
    {
        return displayLegalMovesEnabled;
    }

    /**
     * @param isDisplayLegalMovesEnabled the isDisplayLegalMovesEnabled to set
     */
    public void setDisplayLegalMovesEnabled(boolean displayLegalMovesEnabled)
    {
        this.displayLegalMovesEnabled = displayLegalMovesEnabled;
    }

    public enum gameModes
    {
        newGame, loadGame
    }
    protected gameModes gameMode;
    protected Player playerWhite;
    protected Player playerBlack;

    public enum gameTypes
    {

        local, network
    }
    protected gameTypes gameType;
    protected boolean renderLabels = true;

    public Settings()
    {
        //temporally
        this.playerWhite = new Player("", Colors.WHITE.getColorName());
        this.playerBlack = new Player("", Colors.BLACK.getColorName());
        this.timeLimitSet = false;

        gameMode = gameModes.newGame;
    }

    /** Method to get game time set by player
     *  @return timeFofGame int with how long the game will leasts
     */
    public int getTimeForGame()
    {
        return this.timeForGame;
    }

    public static String lang(String key)
    {
        if (Settings.loc == null)
        {
            Settings.loc = PropertyResourceBundle.getBundle(JChessApp.MAIN_PACKAGE_NAME + ".resources.i18n.main");
            Locale.setDefault(Locale.ENGLISH);
        }
        String result = "";
        try
        {
            result = Settings.loc.getString(key);
        }
        catch (java.util.MissingResourceException exc)
        {
            result = key;
        }
        LOG.debug("Locale: " + Settings.loc.getLocale().toString());
        return result;
    }
}
