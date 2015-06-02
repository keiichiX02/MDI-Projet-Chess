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

 */
package jchess.core;

import jchess.core.pieces.implementation.King;
import jchess.core.pieces.implementation.Knight;
import jchess.core.pieces.implementation.Queen;
import jchess.core.pieces.implementation.Rook;
import jchess.core.pieces.implementation.Pawn;
import jchess.core.pieces.implementation.Bishop;
import jchess.core.pieces.Piece;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Set;
import jchess.JChessApp;
import jchess.core.moves.Castling;
import jchess.core.moves.Move;
import jchess.core.moves.Moves;
import jchess.display.views.chessboard.implementation.graphic2D.Chessboard2D;
import jchess.display.views.chessboard.ChessboardView;
import jchess.utils.Settings;
import org.apache.log4j.*;

/** 
 * @author: Mateusz Sławomir Lach ( matlak, msl )
 * @author: Damian Marciniak
 * Class to represent chessboard. Chessboard is made from squares.
 * It is setting the squers of chessboard and sets the pieces(pawns)
 * witch the owner is current player on it.
 */
public class Chessboard 
{
    private static final Logger LOG = Logger.getLogger(Chessboard.class);
    
    protected static final int TOP = 0;
    
    protected static final int BOTTOM = 7;
    
    /*
     * squares of chessboard
     */
    protected Square squares[][];

    private Set<Square> moves;
    
    private Settings settings;
    
    protected King kingWhite;
    
    protected King kingBlack;
    
    //For En passant:
    //|-> Pawn whose in last turn moved two square
    protected Pawn twoSquareMovedPawn = null;
    
    private Moves Moves;
    
    protected Square activeSquare;
    
    protected int activeSquareX;
    
    protected int activeSquareY;      
    
    /**
     * chessboard view data object
     */  
    private ChessboardView chessboardView;

    /** 
     * Chessboard class constructor
     * @param settings reference to Settings class object for this chessboard
     * @param moves_history reference to Moves class object for this chessboard 
     */
    public Chessboard(Settings settings, Moves Moves)
    {
        this.settings = settings;
        this.chessboardView = new Chessboard2D(this);

        this.activeSquareX = 0;
        this.activeSquareY = 0;
        
        this.squares = new Square[8][8];//initalization of 8x8 chessboard

        for (int i = 0; i < 8; i++) //create object for each square
        {
            for (int y = 0; y < 8; y++)
            {
                this.squares[i][y] = new Square(i, y, null);
            }
        }//--endOf--create object for each square
        this.Moves = Moves;

    }/*--endOf-Chessboard--*/

    /**
     * @return the top
     */
    public static int getTop() 
    {
        return TOP;
    }

    /**
     * @return the bottom
     */
    public static int getBottom() 
    {
        return BOTTOM;
    }
    /** Method setPieces on begin of new game or loaded game
     * @param places string with pieces to set on chessboard
     * @param plWhite reference to white player
     * @param plBlack reference to black player
     */
    public void setPieces(String places, Player plWhite, Player plBlack)
    {

        if (places.equals("")) //if newGame
        {
            this.setPieces4NewGame(plWhite, plBlack);
        } 
        else //if loadedGame
        {
            return;
        }
    }/*--endOf-setPieces--*/


    /**
     *
     */
    private void setPieces4NewGame(Player plWhite, Player plBlack)
    {
        /* WHITE PIECES */
        Player player = plBlack;
        Player player1 = plWhite;
        this.setFigures4NewGame(0, player);
        this.setPawns4NewGame(1, player);
        this.setFigures4NewGame(7, player1);
        this.setPawns4NewGame(6, player1);
    }/*--endOf-setPieces(boolean upsideDown)--*/


    /**  
     *  Method to set Figures in row (and set Queen and King to right position)
     *  @param i row where to set figures (Rook, Knight etc.)
     *  @param player which is owner of pawns
     *  @param upsideDown if true white pieces will be on top of chessboard
     * */
    private void setFigures4NewGame(int i, Player player)
    {
        if (i != 0 && i != 7)
        {
            LOG.error("error setting figures like rook etc.");
            return;
        }
        else if (i == 0)
        {
            player.goDown = true;
        }

        this.getSquare(0, i).setPiece(new Rook(this, player));
        this.getSquare(7, i).setPiece(new Rook(this, player));
        this.getSquare(1, i).setPiece(new Knight(this, player));
        this.getSquare(6, i).setPiece(new Knight(this, player));
        this.getSquare(2, i).setPiece(new Bishop(this, player));
        this.getSquare(5, i).setPiece(new Bishop(this, player));
        

        this.getSquare(3, i).setPiece(new Queen(this, player));
        if (player.getColor() == Colors.WHITE)
        {
            kingWhite = new King(this, player);
            this.getSquare(4, i).setPiece(kingWhite);
        }
        else
        {
            kingBlack = new King(this, player);
            this.getSquare(4, i).setPiece(kingBlack);
        }
    }

    /**  method set Pawns in row
     *  @param i row where to set pawns
     *  @param player player which is owner of pawns
     * */
    private void setPawns4NewGame(int i, Player player)
    {
        if (i != 1 && i != 6)
        {
            LOG.error("error setting pawns etc.");
            return;
        }
        for (int x = 0; x < 8; x++)
        {
            this.getSquare(x, i).setPiece(new Pawn(this, player));
        }
    }
    
    /** Method selecting piece in chessboard
     * @param  sq square to select (when clicked))
     */
    public void select(Square sq)
    {
        this.setActiveSquare(sq);
        this.setActiveSquareX(sq.getPozX() + 1);
        this.setActiveSquareY(sq.getPozY() + 1);

        LOG.debug("active_x: " + this.getActiveSquareX() + " active_y: " + this.getActiveSquareY());//4tests
        this.getChessboardView().repaint();
    }/*--endOf-select--*/

    public void unselect()
    {
        this.setActiveSquareX(0);
        this.setActiveSquareY(0);
        this.setActiveSquare(null);

        this.getChessboardView().unselect();
    }/*--endOf-unselect--*/
        
    public void resetActiveSquare() 
    {
        this.setActiveSquare(null);
    }
 
    public void move(Square begin, Square end)
    {
        move(begin, end, true);
    }

    /** Method to move piece over chessboard
     * @param xFrom from which x move piece
     * @param yFrom from which y move piece
     * @param xTo to which x move piece
     * @param yTo to which y move piece
     */
    public void move(int xFrom, int yFrom, int xTo, int yTo)
    {
        Square fromSQ = null;
        Square toSQ = null;
        try
        {
            fromSQ = this.getSquares()[xFrom][yFrom];
            toSQ = this.getSquares()[xTo][yTo];
        }
        catch (java.lang.IndexOutOfBoundsException exc)
        {
            LOG.error("error moving piece: " + exc.getMessage());
            return;
        }
        this.move(fromSQ, toSQ, true);
    }

    public void move(Square begin, Square end, boolean refresh)
    {
        this.move(begin, end, refresh, true);
    }

    /** Method move piece from square to square
     * @param begin square from which move piece
     * @param end square where we want to move piece         *
     * @param refresh chessboard, default: true
     * */
    public void move(Square begin, Square end, boolean refresh, boolean clearForwardHistory)
    {
        Castling wasCastling = Castling.NONE;
        Piece promotedPiece = null;
        boolean wasEnPassant = false;
        if (end.piece != null)
        {
            end.getPiece().setSquare(null);
        }

        Square tempBegin = new Square(begin);//4 moves history
        Square tempEnd = new Square(end);  //4 moves history

        begin.getPiece().setSquare(end);//set square of piece to ending
        end.piece = begin.piece;//for ending square set piece from beginin square
        begin.piece = null;//make null piece for begining square

        if (end.getPiece().getName().equals("King"))
        {
            if (!((King) end.piece).getWasMotioned())
            {
                ((King) end.piece).setWasMotioned(true);
            }

            //Castling
            if (begin.getPozX() + 2 == end.getPozX())
            {
                move(getSquare(7, begin.getPozY()), getSquare(end.getPozX() - 1, begin.getPozY()), false, false);
                wasCastling = Castling.SHORT_CASTLING;
            }
            else if (begin.getPozX() - 2 == end.getPozX())
            {
                move(getSquare(0, begin.getPozY()), getSquare(end.getPozX() + 1, begin.getPozY()), false, false);
                wasCastling = Castling.LONG_CASTLING;
            }
            //endOf Castling
        }
        else if (end.getPiece().getName().equals("Rook"))
        {
            if (!((Rook) end.piece).getWasMotioned())
            {
                ((Rook) end.piece).setWasMotioned(true);
            }
        }
        else if (end.getPiece().getName().equals("Pawn"))
        {
            if (getTwoSquareMovedPawn() != null && getSquares()[end.getPozX()][begin.getPozY()] == getTwoSquareMovedPawn().getSquare()) //en passant
            {
                tempEnd.piece = getSquares()[end.getPozX()][begin.getPozY()].piece; //ugly hack - put taken pawn in en passant plasty do end square

                squares[end.pozX][begin.pozY].piece = null;
                wasEnPassant = true;
            }

            if (begin.getPozY() - end.getPozY() == 2 || end.getPozY() - begin.getPozY() == 2) //moved two square
            {
                twoSquareMovedPawn = (Pawn) end.piece;
            }
            else
            {
                twoSquareMovedPawn = null; //erase last saved move (for En passant)
            }

            if (end.getPiece().getSquare().getPozY() == 0 || end.getPiece().getSquare().getPozY() == 7) //promote Pawn
            {
                if (clearForwardHistory)
                {
                    String color = String.valueOf(end.getPiece().getPlayer().getColor().getSymbolAsString().toUpperCase());
                    String newPiece = JChessApp.getJavaChessView().showPawnPromotionBox(color); //return name of new piece
                    
                    Piece piece;
                    switch (newPiece)
                    {
                        case "Queen":
                            piece = new Queen(this, end.getPiece().getPlayer());
                            break;
                        case "Rook":
                            piece = new Rook(this, end.getPiece().getPlayer());
                            break;
                        case "Bishop":
                            piece = new Bishop(this, end.getPiece().getPlayer());
                            break;
                        default:
                            piece = new Knight(this, end.getPiece().getPlayer());
                            break;
                    }
                    piece.setChessboard(end.getPiece().getChessboard());
                    piece.setPlayer(end.getPiece().getPlayer());
                    piece.setSquare(end.getPiece().getSquare());
                    end.piece = piece;                    
                    promotedPiece = end.piece;
                }
            }
        }
        else if (!end.getPiece().getName().equals("Pawn"))
        {
            twoSquareMovedPawn = null; //erase last saved move (for En passant)
        }

        if (refresh)
        {
            this.unselect();//unselect square
            repaint();
        }

        if (clearForwardHistory)
        {
            this.Moves.clearMoveForwardStack();
            this.Moves.addMove(tempBegin, tempEnd, true, wasCastling, wasEnPassant, promotedPiece);
        }
        else
        {
            this.Moves.addMove(tempBegin, tempEnd, false, wasCastling, wasEnPassant, promotedPiece);
        }
    }/*endOf-move()-*/


    public boolean redo()
    {
        return redo(true);
    }

    public boolean redo(boolean refresh)
    {
        if ( this.getSettings().getGameType() == Settings.gameTypes.local ) //redo only for local game
        {
            Move first = this.Moves.redo();

            Square from = null;
            Square to = null;

            if (first != null)
            {
                from = first.getFrom();
                to = first.getTo();

                this.move(this.getSquares()[from.getPozX()][from.getPozY()], this.getSquares()[to.getPozX()][to.getPozY()], true, false);
                if (first.getPromotedPiece() != null)
                {
                    Pawn pawn = (Pawn) this.getSquares()[to.getPozX()][to.getPozY()].piece;
                    pawn.setSquare(null);

                    this.squares[to.pozX][to.pozY].piece = first.getPromotedPiece();
                    Piece promoted = this.getSquares()[to.getPozX()][to.getPozY()].piece;
                    promoted.setSquare(this.getSquares()[to.getPozX()][to.getPozY()]);
                }
                return true;
            }
            
        }
        return false;
    }

    public boolean undo()
    {
        return undo(true);
    }

    public synchronized boolean undo(boolean refresh) //undo last move
    {
        Move last = this.Moves.undo();

        if (last != null && last.getFrom() != null)
        {
            Square begin = last.getFrom();
            Square end = last.getTo();
            try
            {
                Piece moved = last.getMovedPiece();
                this.squares[begin.pozX][begin.pozY].piece = moved;

                moved.setSquare(this.getSquares()[begin.getPozX()][begin.getPozY()]);

                Piece taken = last.getTakenPiece();
                if (last.getCastlingMove() != Castling.NONE)
                {
                    Piece rook = null;
                    if (last.getCastlingMove() == Castling.SHORT_CASTLING)
                    {
                        rook = this.getSquares()[end.getPozX() - 1][end.getPozY()].piece;
                        this.squares[7][begin.pozY].piece = rook;
                        rook.setSquare(this.getSquares()[7][begin.getPozY()]);
                        this.squares[end.pozX - 1][end.pozY].piece = null;
                    }
                    else
                    {
                        rook = this.getSquares()[end.getPozX() + 1][end.getPozY()].piece;
                        this.squares[0][begin.pozY].piece = rook;
                        rook.setSquare(this.getSquares()[0][begin.getPozY()]);
                        this.squares[end.pozX + 1][end.pozY].piece = null;
                    }
                    ((King) moved).setWasMotioned(false);
                    ((Rook) rook).setWasMotioned(false);
                }
                else if (moved.getName().equals("Rook"))
                {
                    ((Rook) moved).setWasMotioned(false);
                }
                else if (moved.getName().equals("Pawn") && last.wasEnPassant())
                {
                    Pawn pawn = (Pawn) last.getTakenPiece();
                    this.squares[end.pozX][begin.pozY].piece = pawn;
                    pawn.setSquare(this.getSquares()[end.getPozX()][begin.getPozY()]);

                }
                else if (moved.getName().equals("Pawn") && last.getPromotedPiece() != null)
                {
                    Piece promoted = this.getSquares()[end.getPozX()][end.getPozY()].piece;
                    promoted.setSquare(null);
                    this.squares[end.pozX][end.pozY].piece = null;
                }

                //check one more move back for en passant
                Move oneMoveEarlier = this.Moves.getLastMoveFromHistory();
                if (oneMoveEarlier != null && oneMoveEarlier.wasPawnTwoFieldsMove())
                {
                    Piece canBeTakenEnPassant = this.getSquare(oneMoveEarlier.getTo().getPozX(), oneMoveEarlier.getTo().getPozY()).getPiece();
                    if (canBeTakenEnPassant.getName().equals("Pawn"))
                    {
                        this.twoSquareMovedPawn = (Pawn) canBeTakenEnPassant;
                    }
                }

                if (taken != null && !last.wasEnPassant())
                {
                    this.squares[end.pozX][end.pozY].piece = taken;
                    taken.setSquare(this.getSquares()[end.getPozX()][end.getPozY()]);
                }
                else
                {
                    this.squares[end.pozX][end.pozY].piece = null;
                }

                if (refresh)
                {
                    this.unselect();//unselect square
                    repaint();
                }

            }
            catch (ArrayIndexOutOfBoundsException | NullPointerException exc)
            {
                LOG.error("error: " + exc.getClass()+ " exc object: " + exc);
                return false;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public void componentMoved(ComponentEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void componentShown(ComponentEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void componentHidden(ComponentEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the squares
     */
    public Square[][] getSquares() 
    {
        return squares;
    }
    
    public Square getSquare(int x, int y) 
    {
        try 
        {
            return squares[x][y];
        } 
        catch(ArrayIndexOutOfBoundsException exc) 
        {
            return null;
        }
    }

    /**
     * @return the activeSquare
     */
    public Square getActiveSquare() 
    {
        return activeSquare;
    }

    public ArrayList<Piece> getAllPieces(Colors color)
    {
        ArrayList<Piece> result = new ArrayList<>();
        for(int i=0; i<squares.length; i++)
        {
            for(int j=0; j<squares[i].length; j++)
            {
                Square sq = squares[i][j];
                if(null != sq.getPiece() && ( sq.getPiece().getPlayer().color == color || color == null) )
                {
                    result.add(sq.getPiece());
                }
            }
        }       
        return result;
    }
    
    public static boolean wasEnPassant(Square sq)
    {
        return sq.getPiece() != null
                    && sq.getPiece().getChessboard().getTwoSquareMovedPawn() != null
                    && sq == sq.getPiece().getChessboard().getTwoSquareMovedPawn().getSquare();
    }    

    /**
     * @return the kingWhite
     */
    public King getKingWhite()
    {
        return kingWhite;
    }

    /**
     * @return the kingBlack
     */
    public King getKingBlack()
    {
        return kingBlack;
    }

    /**
     * @return the twoSquareMovedPawn
     */
    public Pawn getTwoSquareMovedPawn()
    {
        return twoSquareMovedPawn;
    }

    /**
     * @return the chessboardView
     */
    public ChessboardView getChessboardView()
    {
        return chessboardView;
    }

    /**
     * @param chessboardView the chessboardView to set
     */
    public void setChessboardView(ChessboardView chessboardView)
    {
        this.chessboardView = chessboardView;
    }
    
    public void repaint()
    {
        getChessboardView().repaint();
    }

    /**
     * @return the settings
     */
    public Settings getSettings()
    {
        return settings;
    }

    /**
     * @param settings the settings to set
     */
    public void setSettings(Settings settings)
    {
        this.settings = settings;
    }

    /**
     * @return the moves
     */
    public Set<Square> getMoves()
    {
        return moves;
    }

    /**
     * @param moves the moves to set
     */
    public void setMoves(Set<Square> moves)
    {
        this.moves = moves;
    }

    /**
     * @param activeSquare the activeSquare to set
     */
    public void setActiveSquare(Square activeSquare)
    {
        this.activeSquare = activeSquare;
    }

    /**
     * @return the activeSquareX
     */
    public int getActiveSquareX()
    {
        return activeSquareX;
    }

    /**
     * @param activeSquareX the activeSquareX to set
     */
    public void setActiveSquareX(int activeSquareX)
    {
        this.activeSquareX = activeSquareX;
    }

    /**
     * @return the activeSquareY
     */
    public int getActiveSquareY()
    {
        return activeSquareY;
    }

    /**
     * @param activeSquareY the activeSquareY to set
     */
    public void setActiveSquareY(int activeSquareY)
    {
        this.activeSquareY = activeSquareY;
    }
}
