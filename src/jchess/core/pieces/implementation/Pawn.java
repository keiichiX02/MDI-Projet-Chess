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

package jchess.core.pieces.implementation;

import jchess.core.pieces.Piece;
import jchess.core.Chessboard;
import jchess.core.Player;
import jchess.core.pieces.traits.behaviors.implementation.PawnBehavior;

/**
 * @author : Mateusz Sławomir Lach ( matlak, msl )
 * @author : Damian Marciniak
 * Class to represent a pawn piece
 * Pawn can move only forvard and can beat only across
 * In first move pawn can move 2 sqares
 * pawn can be upgreade to rook, knight, bishop, Queen if it's in the
 * squers nearest the side where opponent is lockated
 * Firat move of pawn:
 *       |_|_|_|_|_|_|_|_|7
         |_|_|_|_|_|_|_|_|6
         |_|_|_|X|_|_|_|_|5
         |_|_|_|X|_|_|_|_|4
         |_|_|_|P|_|_|_|_|3
         |_|_|_|_|_|_|_|_|2
         |_|_|_|_|_|_|_|_|1
         |_|_|_|_|_|_|_|_|0
         0 1 2 3 4 5 6 7
 *
 * Move of a pawn:
 *       |_|_|_|_|_|_|_|_|7
         |_|_|_|_|_|_|_|_|6
         |_|_|_|_|_|_|_|_|5
         |_|_|_|X|_|_|_|_|4
         |_|_|_|P|_|_|_|_|3
         |_|_|_|_|_|_|_|_|2
         |_|_|_|_|_|_|_|_|1
         |_|_|_|_|_|_|_|_|0
         0 1 2 3 4 5 6 7
 * Beats with can take pawn:
 *       |_|_|_|_|_|_|_|_|7
         |_|_|_|_|_|_|_|_|6
         |_|_|_|_|_|_|_|_|5
         |_|_|X|_|X|_|_|_|4
         |_|_|_|P|_|_|_|_|3
         |_|_|_|_|_|_|_|_|2
         |_|_|_|_|_|_|_|_|1
         |_|_|_|_|_|_|_|_|0
         0 1 2 3 4 5 6 7
 */
public class Pawn extends Piece
{
    protected boolean down;
    
    protected static short value = 1;
    
    public Pawn(Chessboard chessboard, Player player)
    {
        super(chessboard, player);
        this.symbol = "";
        this.behaviors.add(new PawnBehavior(this));
    }

    void promote(Piece newPiece)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the down
     */
    public boolean isDown()
    {
        return down;
    }
}
