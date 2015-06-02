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
 * Author: Mateusz Sławomir Lach ( matlak, msl )
 */
package jchess.core.moves;

import jchess.core.Chessboard;
import jchess.core.pieces.Piece;
import jchess.core.moves.Castling;
import jchess.core.Square;

public class Move
{

    protected Square from = null;
    protected Square to = null;
    protected Piece movedPiece = null;
    protected Piece takenPiece = null;
    protected Piece promotedTo = null;
    protected boolean wasEnPassant = false;
    protected Castling castlingMove = Castling.NONE;
    protected boolean wasPawnTwoFieldsMove = false;

    Move(Square from, Square to, Piece movedPiece, Piece takenPiece, Castling castlingMove, boolean wasEnPassant, Piece promotedPiece)
    {
        this.from = from;
        this.to = to;

        this.movedPiece = movedPiece;
        this.takenPiece = takenPiece;

        this.castlingMove = castlingMove;
        this.wasEnPassant = wasEnPassant;

        if (movedPiece.getName().equals("Pawn") && Math.abs(to.getPozY() - from.getPozY()) == 2)
        {
            this.wasPawnTwoFieldsMove = true;
        }
        else if (movedPiece.getName().equals("Pawn") && to.getPozY() == Chessboard.getBottom() || to.getPozY() == Chessboard.getTop() && promotedPiece != null)
        {
            this.promotedTo = promotedPiece;
        }
    }

    public Square getFrom()
    {
        return this.from;
    }

    public Square getTo()
    {
        return this.to;
    }

    public Piece getMovedPiece()
    {
        return this.movedPiece;
    }

    public Piece getTakenPiece()
    {
        return this.takenPiece;
    }

    public boolean wasEnPassant()
    {
        return this.wasEnPassant;
    }

    public boolean wasPawnTwoFieldsMove()
    {
        return this.wasPawnTwoFieldsMove;
    }

    public Castling getCastlingMove()
    {
        return this.castlingMove;
    }

    public Piece getPromotedPiece()
    {
        return this.promotedTo;
    }
}
