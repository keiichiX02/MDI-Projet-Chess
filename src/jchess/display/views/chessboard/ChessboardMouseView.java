/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jchess.display.views.chessboard;

import java.awt.event.MouseListener;
import jchess.core.Chessboard;
import jchess.core.Square;

/*
 * Authors:
 * Mateusz Sławomir Lach ( matlak, msl )
 */
public interface ChessboardMouseView extends MouseListener
{
    public Square getSquare(int clickedX, int clickedY);
    
    public void draw(Chessboard chessboard);
}
