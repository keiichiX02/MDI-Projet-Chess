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

package jchess.core;

import jchess.core.pieces.implementation.King;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.Calendar;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import org.apache.log4j.Logger;
import java.io.IOException;
import jchess.JChessApp;
import jchess.core.moves.Moves;
import jchess.display.panels.LocalSettingsView;
import jchess.display.views.chessboard.ChessboardView;
import jchess.utils.Settings;



/** 
 * @author: Mateusz Sławomir Lach ( matlak, msl )
 * @author: Damian Marciniak
 * Class responsible for the starts of new games, loading games,
 * saving it, and for ending it.
 * This class is also responsible for appoing player with have
 * a move at the moment
 */
public class Game extends JPanel implements ComponentListener, MouseListener
{
    
    private static final Logger LOG = org.apache.log4j.Logger.getLogger(Game.class);
    
    /**
     * Settings object of the current game
     */
    protected Settings settings;
 
    /**
     * if chessboard is blocked - true, false otherwise
     */
    protected boolean blockedChessboard;
    
    /**
     * chessboard data object
     */    
    protected Chessboard chessboard;

    
    /**
     * Currently active player object
     */
    protected Player activePlayer;
    
    /**
     * Game clock object
     */
    protected GameClock gameClock;
    


    /**
     * History of moves object
     */       
    protected Moves moves;
    

    
    protected JTabbedPane tabPane;
   
    protected LocalSettingsView localSettingsView;
    

    public Game()
    {
        this.setLayout(null);
        this.moves = new Moves(this);
        settings = new Settings();
        chessboard = new Chessboard(this.getSettings(), this.moves);
        
        
        ChessboardView chessboardView = chessboard.getChessboardView();
        int chessboardWidth = chessboardView.getChessboardWidht(true);
        this.add(chessboardView);
        
        //this.chessboard.
        gameClock = new GameClock(this);
        gameClock.setSize(new Dimension(200, 100));
        gameClock.setLocation(new Point(500, 0));
        this.add(gameClock);

        JScrollPane Moves = this.moves.getScrollPane();
        Moves.setSize(new Dimension(180, 350));
        Moves.setLocation(new Point(500, 121));
        this.add(Moves);


        this.tabPane = new JTabbedPane();
        this.localSettingsView = new LocalSettingsView(this);
        //this.tabPane.addTab(Settings.lang("game_chat"), this.chat);
        this.tabPane.addTab(Settings.lang("game_settings"), this.localSettingsView);
        
        /**
         * htd
         */
//        this.tabPane.setSize(new Dimension(380, 100));
//        this.tabPane.setLocation(new Point(chessboardWidth, chessboardWidth/2));
//        this.tabPane.setMinimumSize(new Dimension(400, 100));        
        this.tabPane.setSize(new Dimension(380, 300));
        this.tabPane.setLocation(new Point(chessboardWidth, chessboardWidth/2));
        //this.tabPane.setMinimumSize(new Dimension(400, 500));    
        
        this.add(tabPane);

        
        this.blockedChessboard = false;
        this.setLayout(null);
        this.setDoubleBuffered(true);
        chessboardView.addMouseListener(this);
        this.addComponentListener(this);
        
        /**
    	 * htd
    	 */
    	System.out.println("str " + this.getMoves().getMovesInString()); //ok, str empty at start
    	System.out.println("first move done? " + this.getMoves().isFirstMoveSpotted());
    	//
    }

    /** Method to save actual state of game
     * @param path address of place where game will be saved
     */
    public void saveGame(File path)
    {
        File file = path;
        FileWriter fileW = null;
        try
        {
            fileW = new FileWriter(file);
        }
        catch (IOException exc)
        {
            LOG.error("error creating fileWriter: " + exc);
            JOptionPane.showMessageDialog(this, Settings.lang("error_writing_to_file")+": " + exc);
            return;
        }
        Calendar cal = Calendar.getInstance();
        String str = "";
        String info = "[Event \"Game\"]\n[Date \"" + cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.DAY_OF_MONTH) + "\"]\n"
                    + "[White \"" + this.getSettings().getPlayerWhite().getName() + "\"]\n[Black \"" + this.getSettings().getPlayerBlack().getName() + "\"]\n\n";
        str += info;
        str += this.getMoves().getMovesInString();
        try
        {
            fileW.write(str);
            fileW.flush();
            fileW.close();
        }
        catch (IOException exc)
        {
            LOG.error("error writing to file: " + exc);
            JOptionPane.showMessageDialog(this, Settings.lang("error_writing_to_file")+": " + exc);
            return;
        }
        JOptionPane.showMessageDialog(this, Settings.lang("game_saved_properly"));
    }

    /** Loading game method(loading game state from the earlier saved file)
     *  @param file File where is saved game
     */
    static public void loadGame(File file)
    {
        FileReader fileR = null;
        try
        {
            fileR = new FileReader(file);
        }
        catch (IOException exc)
        {
            LOG.error("Something wrong reading file: " + exc);
            return;
        }
        BufferedReader br = new BufferedReader(fileR);
        String tempStr = new String();
        String blackName, whiteName;
        try
        {
            tempStr = getLineWithVar(br, "[White");
            whiteName = getValue(tempStr);
            tempStr = getLineWithVar(br, "[Black");
            blackName = getValue(tempStr);
            tempStr = getLineWithVar(br, "1.");
        }
        catch (ReadGameError err)
        {
            LOG.error("Error reading file: " + err);
            return;
        }
        Game newGUI = JChessApp.getJavaChessView().addNewTab(whiteName + " vs. " + blackName);
        Settings locSetts = newGUI.getSettings();
        
        Player playerBlack = locSetts.getPlayerBlack();
        Player playerWhite = locSetts.getPlayerWhite();
        
        playerBlack.setName(blackName);
        playerWhite.setName(whiteName);
        
        playerBlack.setType(Player.playerTypes.localUser);
        playerWhite.setType(Player.playerTypes.localUser);
        
        locSetts.setGameMode(Settings.gameModes.loadGame);
        locSetts.setGameType(Settings.gameTypes.local);

        newGUI.newGame();
        newGUI.blockedChessboard = true;
        JChessApp.getJavaChessView().setLastTabAsActive();
        newGUI.getMoves().setMoves(tempStr);
        newGUI.blockedChessboard = false;
        newGUI.getChessboard().repaint();
        //newGUI.getChat().setEnabled(false);
    }

    /** Method checking in with of line there is an error
     *  @param  br BufferedReader class object to operate on
     *  @param  srcStr String class object with text which variable you want to get in file
     *  @return String with searched variable in file (whole line)
     *  @throws ReadGameError class object when something goes wrong when reading file
     */
    static public String getLineWithVar(BufferedReader br, String srcStr) throws ReadGameError
    {
        String str = new String();
        while (true)
        {
            try
            {
                str = br.readLine();
            }
            catch (IOException exc)
            {
                LOG.error("Something wrong reading file: " + exc);
            }
            if (str == null)
            {
                throw new ReadGameError();
            }
            if (str.startsWith(srcStr))
            {
                return str;
            }
        }
    }

    /** Method to get value from loaded txt line
     *  @param line Line which is readed
     *  @return result String with loaded value
     *  @throws ReadGameError object class when something goes wrong
     */
    static public String getValue(String line) throws ReadGameError
    {
        int from = line.indexOf("\"");
        int to = line.lastIndexOf("\"");
        int size = line.length() - 1;
        String result = new String();
        if (to < from || from > size || to > size || to < 0 || from < 0)
        {
            throw new ReadGameError();
        }
        try
        {
            result = line.substring(from + 1, to);
        }
        catch (StringIndexOutOfBoundsException exc)
        {
            LOG.error("error getting value: " + exc);
            return "none";
        }
        return result;
    }

    /** Method to Start new game
     *
     */
    public void newGame()
    {
        getChessboard().setPieces("", getSettings().getPlayerWhite(), getSettings().getPlayerBlack());

        activePlayer = getSettings().getPlayerWhite();
        if (activePlayer.getPlayerType() != Player.playerTypes.localUser)
        {
            this.blockedChessboard = true;
        }
        //dirty hacks starts over here :) 
        //to fix rendering artefacts on first run
        Game activeGame = JChessApp.getJavaChessView().getActiveTabGame();
        if (null != activeGame) {
            Chessboard chessboard = activeGame.getChessboard();
            ChessboardView chessboardView = chessboard.getChessboardView();
            if (JChessApp.getJavaChessView().getNumberOfOpenedTabs() == 0)
            {
                chessboardView.resizeChessboard(chessboardView.getChessboardHeight(false));
                chessboard.repaint();
                activeGame.repaint();
            }            
        }
        chessboard.repaint();
        this.repaint();
        //dirty hacks ends over here :)
    }

    /** Method to end game
     *  @param message what to show player(s) at end of the game (for example "draw", "black wins" etc.)
     */
    public void endGame(String message)
    {
        this.blockedChessboard = true;
        LOG.debug(message);
        JOptionPane.showMessageDialog(null, message);
    }

    /** Method to swich active players after move
     */
    public void switchActive()
    {
        if (activePlayer == getSettings().getPlayerWhite())
        {
            activePlayer = getSettings().getPlayerBlack();
        }
        else
        {
            activePlayer = getSettings().getPlayerWhite();
        }

        this.getGameClock().switchClocks();
    }

    /** Method of getting accualy active player
     *  @return  player The player which have a move
     */
    public Player getActivePlayer()
    {
        return this.activePlayer;
    }

    /** Method to go to next move (checks if game is local/network etc.)
     */
    public void nextMove()
    {
        switchActive();

        LOG.debug("next move, active player: " + activePlayer.getName() + 
                  ", color: " + activePlayer.getColor().name() + 
                  ", type: " + activePlayer.getPlayerType().name()
        );
        if (activePlayer.getPlayerType() == Player.playerTypes.localUser)
        {
            this.blockedChessboard = false;
        }
        else if (activePlayer.getPlayerType() == Player.playerTypes.networkUser)
        {
            this.blockedChessboard = true;
        }
        else if (activePlayer.getPlayerType() == Player.playerTypes.computer)
        {
        }
    }

    /** Method to simulate Move to check if it's correct etc. (usable for network game).
     * @param beginX from which X (on chessboard) move starts
     * @param beginY from which Y (on chessboard) move starts
     * @param endX   to   which X (on chessboard) move go
     * @param endY   to   which Y (on chessboard) move go
     * */
    public boolean simulateMove(int beginX, int beginY, int endX, int endY)
    {
        try 
        {
            Square begin = getChessboard().getSquare(beginX, beginY);
            Square end   = getChessboard().getSquare(endX, endY);
            getChessboard().select(begin);
            if (getChessboard().getActiveSquare().getPiece().getAllMoves().contains(end)) //move
            {
                getChessboard().move(begin, end);
                
                /**
                 * htd
                 */
                this.localSettingsView.disableTimeConfirmButton();
                //
            }
            else
            {
                LOG.debug("Bad move: beginX: " + beginX + " beginY: " + beginY + " endX: " + endX + " endY: " + endY);
                return false;
            }
            getChessboard().unselect();
            nextMove();

            return true;
            
        } 
        catch(StringIndexOutOfBoundsException exc) 
        {
            LOG.error("StringIndexOutOfBoundsException: " + exc);
            return false;
        }    
        catch(ArrayIndexOutOfBoundsException exc) 
        {
            LOG.error("ArrayIndexOutOfBoundsException: " + exc);
            return false;
        }
        catch(NullPointerException exc)
        {
            LOG.error("NullPointerException: " + exc + " stack: " + exc.getStackTrace());
            return false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent arg0)
    {
    }
    
    public boolean undo()
    {
        boolean status = false;
        
        if( this.getSettings().getGameType() == Settings.gameTypes.local )
        {
            status = getChessboard().undo();
            if( status )
            {
                this.switchActive();
            }
            else
            {
                getChessboard().repaint();//repaint for sure
            }
        }

        return status;
    }
    
    public boolean rewindToBegin()
    {
        boolean result = false;
        
        if( this.getSettings().getGameType() == Settings.gameTypes.local )
        {
            while( getChessboard().undo() )
            {
                result = true;
            }
        }
        else
        {
            throw new UnsupportedOperationException( Settings.lang("operation_supported_only_in_local_game") );
        }
        
        return result;
    }
    
    public boolean rewindToEnd() throws UnsupportedOperationException
    {
        boolean result = false;
        
        if( this.getSettings().getGameType() == Settings.gameTypes.local )
        {
            while( getChessboard().redo() )
            {
                result = true;
            }
        }
        else
        {
            throw new UnsupportedOperationException( Settings.lang("operation_supported_only_in_local_game") );
        }
        
        return result;
    }
    
    public boolean redo()
    {
        boolean status = getChessboard().redo();
        if( this.getSettings().getGameType() == Settings.gameTypes.local )
        {
            if ( status )
            {
                this.nextMove();
            }
            else
            {
                getChessboard().repaint();//repaint for sure
            }
        }
        else
        {
            throw new UnsupportedOperationException( Settings.lang("operation_supported_only_in_local_game") );
        }
        return status;
    }
    
    
    @Override
    public void mousePressed(MouseEvent event)
    {
        if (event.getButton() == MouseEvent.BUTTON3) //right button
        {
            this.undo();
        }
        else if (event.getButton() == MouseEvent.BUTTON2 && getSettings().getGameType() == Settings.gameTypes.local)
        {
            this.redo();
        }
        else if (event.getButton() == MouseEvent.BUTTON1) //left button
        {

            if (!isChessboardBlocked())
            {
                try 
                {
                    int x = event.getX();//get X position of mouse
                    int y = event.getY();//get Y position of mouse

                    Square sq = getChessboard().getChessboardView().getSquare(x, y);
                    if ((sq == null && sq.piece == null && getChessboard().getActiveSquare() == null)
                            || (this.getChessboard().getActiveSquare() == null && sq.piece != null && sq.getPiece().getPlayer() != this.activePlayer))
                    {
                        return;
                    }

                    if (sq.piece != null && sq.getPiece().getPlayer() == this.activePlayer && sq != getChessboard().getActiveSquare())
                    {
                        getChessboard().unselect();
                        getChessboard().select(sq);
                    }
                    else if (getChessboard().getActiveSquare() == sq) //unselect
                    {
                        getChessboard().unselect();
                    }
                    else if (getChessboard().getActiveSquare() != null && getChessboard().getActiveSquare().piece != null
                            && getChessboard().getActiveSquare().getPiece().getAllMoves().contains(sq)) //move
                    {
                        if (getSettings().getGameType() == Settings.gameTypes.local)
                        {
                            getChessboard().move(getChessboard().getActiveSquare(), sq);
                        }


                        getChessboard().unselect();

                        //switch player
                        this.nextMove();

                        //checkmate or stalemate
                        King king;
                        if (this.activePlayer == getSettings().getPlayerWhite())
                        {
                            king = getChessboard().getKingWhite();
                        }
                        else
                        {
                            king = getChessboard().getKingBlack();
                        }

                        switch (king.isCheckmatedOrStalemated())
                        {
                            case 1:
                                this.endGame("Checkmate! " + king.getPlayer().getColor().toString() + " player lose!");
                                break;
                            case 2:
                                this.endGame("Stalemate! Draw!");
                                break;
                        }
                    }
                    
                } 
                catch(NullPointerException exc)
                {
                    LOG.error("NullPointerException: " + exc.getMessage() + " stack: " + exc.getStackTrace());
                    getChessboard().repaint();
                    return;
                }
            }
            else if (isChessboardBlocked())
            {
                LOG.debug("Chessboard is blocked");
            }
        }
        //chessboard.repaint();
    }
    
    @Override
    public void mouseReleased(MouseEvent arg0)
    {
    }
    
    @Override
    public void mouseEntered(MouseEvent arg0)
    {
    }
    
    @Override
    public void mouseExited(MouseEvent arg0)
    {
    }

    @Override
    public void componentResized(ComponentEvent e)
    {
        resizeGame();
    }

    @Override
    public void componentMoved(ComponentEvent e)
    {
        componentResized(e);
        repaint();
    }

    @Override
    public void componentShown(ComponentEvent e)
    {
        componentResized(e);
    }

    @Override
    public void componentHidden(ComponentEvent e)
    {
    }

    /**
     * @return the chessboard
     */
    public Chessboard getChessboard()
    {
        return chessboard;
    }

    /**
     * @return the settings
     */
    public Settings getSettings() 
    {
        return settings;
    }

    /**
     * @return the blockedChessboard
     */
    public boolean isChessboardBlocked() 
    {
        return blockedChessboard;
    }

    /**
     * @return the gameClock
     */
    public GameClock getGameClock() 
    {
        return gameClock;
    }



    /**
     * @return the moves
     */
    public Moves getMoves() 
    {
        return moves;
    }



    /**
     * @param settings the settings to set
     */
    public void setSettings(Settings settings)
    {
        this.settings = settings;
    }
    



    @Override
    public void repaint()
    {
        super.repaint();
        if (null != this.tabPane)
        {
            this.tabPane.repaint();
        }
        if (null != this.localSettingsView)
        {
            this.localSettingsView.repaint();
        }
        if (null != chessboard)
        {
            this.getChessboard().repaint();
        }
    }

    public void resizeGame()
    {
        int height = this.getHeight() >= this.getWidth() ? this.getWidth() : this.getHeight();
        int chessHeight = (int)Math.round((height * 0.88)/8 ) * 8;
        int chessWidthWithLabels;
        JScrollPane movesScrollPane = this.getMoves().getScrollPane();
        ChessboardView chessboardView = getChessboard().getChessboardView();
        chessboardView.resizeChessboard((int)chessHeight);
        chessHeight = chessboardView.getHeight();
        chessWidthWithLabels  = chessboardView.getChessboardWidht(true);
        movesScrollPane.setLocation(new Point(chessHeight + 5, 100));
        movesScrollPane.setSize(movesScrollPane.getWidth(), chessHeight - 100 - (chessWidthWithLabels/4));
        getGameClock().setLocation(new Point(chessHeight + 5, 0));
        if (null !=  tabPane)
        {
        	/**
        	 * htd
        	 */ /*
            tabPane.setLocation(new Point(chessWidthWithLabels + 5, ((int)chessWidthWithLabels/4)*3));
            tabPane.setSize(new Dimension(movesScrollPane.getWidth(), chessWidthWithLabels/4)); 
            tabPane.repaint(); */
            
        	tabPane.setLocation(new Point(chessWidthWithLabels + 205, 30));
            tabPane.setSize(new Dimension(movesScrollPane.getWidth() + 30, chessWidthWithLabels/2)); 
            tabPane.repaint();
            //
        }
    }
}
class ReadGameError extends Exception
{
}