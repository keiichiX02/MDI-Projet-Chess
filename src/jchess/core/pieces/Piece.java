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

package jchess.core.pieces;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jchess.core.Chessboard;
import jchess.core.Colors;
import jchess.core.Player;
import jchess.core.Square;
import jchess.core.pieces.traits.behaviors.Behavior;
import org.apache.log4j.Logger;

/**
 * @author : Mateusz Sławomir Lach ( matlak, msl )
 * @author : Damian Marciniak
   Class to represent a piece (any kind) - this class should be extended to represent pawn, bishop etc.
 */
public abstract class Piece
{

    private static final Logger LOG = Logger.getLogger(Piece.class);
    
    protected Chessboard chessboard; // <-- this relations isn't in class diagram, but it's necessary :/
    
    protected Square square;
    
    protected Player player;
    
    protected String name;
    
    protected String symbol;
    
    protected static short value = 0;
    
    protected Set<Behavior> behaviors = new HashSet<>();

    public Piece(Chessboard chessboard, Player player)
    {
        this.chessboard = chessboard;
        this.player = player;
        this.name = this.getClass().getSimpleName();

    }
    /* Method to draw piece on chessboard
     * @graph : where to draw
     */
    
    /**
     * @return the value
     */
    public static short getValue()
    {
        return value;
    }        
    
    public void addBehavior(Behavior behavior)
    {
        this.behaviors.add(behavior);
    }
    
    public Set<Behavior> getBehaviors()
    {
        return Collections.unmodifiableSet(this.behaviors);
    }

    void clean() {}

    /** method check if Piece can move to given square
     * @param square square where piece want to move (Square object)
     * @param allmoves  all moves which can piece do
     * */
    boolean canMove(Square square, ArrayList allmoves)
    {
        ArrayList moves = allmoves;
        for (Iterator it = moves.iterator(); it.hasNext();)
        {
            Square sq = (Square) it.next();//get next from iterator
            if (sq == square) //if adress is the same
            {
                return true; //piece canMove
            }
        }
        return false;//if not, piece cannot move
    }
    
    /**
     * Helper method to check if Piece can move to given field
     * @param newX new position X coordinate
     * @param newY new position Y coordinate
     * @return true if can move, false otherwise
     */
    public boolean canMove(int newX, int newY)
    {
        boolean result = false;
        
        Square[][] squares    = chessboard.getSquares();
        if (!isOut(newX, newY) && checkPiece(newX, newY))
        {
            if (this.getPlayer().getColor() == Colors.WHITE) //white
            {
                if (chessboard.getKingWhite().willBeSafeAfterMove(square, squares[newX][newY]))
                {
                    result = true;
                }
            }
            else //or black
            {
                if (chessboard.getKingBlack().willBeSafeAfterMove(square, squares[newX][newY]))
                {
                    result = true;
                }
            }
        }
        return result;
    }    

    /**
     * Annotation to superclass Piece changing pawns location
     * @return  ArrayList with new possition of piece
     */
    public Set<Square> getAllMoves()
    {
        Set<Square> moves = new HashSet<>();
        for(Behavior behavior: behaviors)
        {
            moves.addAll(behavior.getLegalMoves());
        }
        return moves;
    }

    public Set<Square> getSquaresInRange()
    {
        Set<Square> moves = new HashSet<>();
        for (Behavior behavior : behaviors)
        {
            moves.addAll(behavior.getSquaresInRange());
        }
        return moves;
    }

    /** Method is useful for out of bounds protection
     * @param x  x position on chessboard
     * @param y y position on chessboard
     * @return true if parameters are out of bounds (array)
     * */
    public boolean isOut(int x, int y)
    {
        if (x < 0 || x > 7 || y < 0 || y > 7)
        {
            return true;
        }
        return false;
    }

    /** 
     * Method to check if piece can move to given field. 
     * Checks if player tries to move on his own piece, or opponents King - if so, returns boolean False.
     * @param x y position on chessboard
     * @param y  y position on chessboard
     * @return true if can move, false otherwise
     * */
    public boolean checkPiece(int x, int y)
    {
        if (getChessboard().getSquares()[x][y].piece != null
                && getChessboard().getSquares()[x][y].getPiece().getName().equals("King"))
        {
            return false;
        }
        Piece piece = getChessboard().getSquares()[x][y].piece;
        if (piece == null || //if this sqhuare is empty
                piece.getPlayer() != this.getPlayer()) //or piece is another player
        {
            return true;
        }
        return false;
    }

    /** Method check if piece has other owner than calling piece
     * @param x x position on chessboard
     * @param y y position on chessboard
     * @return true if owner(player) is different
     * */
    public boolean otherOwner(int x, int y)
    {
        Square sq = getChessboard().getSquares()[x][y];
        if (sq.piece == null)
        {
            return false;
        }
        if (this.getPlayer() != sq.getPiece().getPlayer())
        {
            return true;
        }
        return false;
    }

    public String getSymbol()
    {
        return this.symbol;
    }

    /**
     * @return the chessboard
     */
    public Chessboard getChessboard()
    {
        return chessboard;
    }

    /**
     * @param chessboard the chessboard to set
     */
    public void setChessboard(Chessboard chessboard)
    {
        this.chessboard = chessboard;
    }

    /**
     * @return the square
     */
    public Square getSquare()
    {
        return square;
    }

    /**
     * @param square the square to set
     */
    public void setSquare(Square square)
    {
        this.square = square;
    }

    /**
     * @return the player
     */
    public Player getPlayer()
    {
        return player;
    }

    /**
     * @param player the player to set
     */
    public void setPlayer(Player player)
    {
        this.player = player;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }
}
