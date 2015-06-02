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
import jchess.core.pieces.traits.behaviors.implementation.KnightBehavior;

/**
 * @author: Mateusz Sławomir Lach ( matlak, msl )
 * @author: Damian Marciniak
 * Class to represent a chess pawn knight
 */
public class Knight extends Piece
{
    protected static final short value = 3;

    public Knight(Chessboard chessboard, Player player)
    {
        super(chessboard, player);//call initializer of super type: Piece
        this.symbol = "N";
        this.addBehavior(new KnightBehavior(this));
    }
    
}
