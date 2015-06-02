/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jchess.display.views.chessboard.implementation.graphic2D;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import jchess.core.Colors;
import jchess.core.pieces.Piece;
import jchess.core.pieces.implementation.Bishop;
import jchess.core.pieces.implementation.King;
import jchess.core.pieces.implementation.Knight;
import jchess.core.pieces.implementation.Pawn;
import jchess.core.pieces.implementation.Queen;
import jchess.core.pieces.implementation.Rook;
import jchess.display.views.chessboard.ChessboardView;
import jchess.utils.GUI;
import org.apache.log4j.Logger;

/**
 *
 * @author Mateusz Sławomir Lach ( matlak, msl )
 */
public class Pieces2D
{
    private static final Logger LOG = Logger.getLogger(Pieces2D.class);
    
    private static final String FILE_EXT = ".png"; 
    
    protected Map<Colors, Map<String, Image>> currentImageSet = new HashMap<>();
    
    protected static Pieces2D instance = null;
    
    protected static int currentSize = 55;
    
    protected static TreeSet<Integer> setsSizes = new TreeSet<>(Arrays.asList(25, 55, 70, 100));
    
    protected static Map<Integer, Map<Colors, Map<String, Image>>> imageSets = initImagesSets();
    
    /**
     * @return the images
     */
    public Map<Colors, Map<String, Image>> getCurrentImageSet()
    {
        return currentImageSet;
    }

    /**
     * @param aImages the images to set
     */
    public void setCurrentImageSet(Map<Colors, Map<String, Image>> aImages)
    {
        currentImageSet = aImages;
    }

    /**
     * @return the currentSize
     */
    public static int getCurrentSize()
    {
        return currentSize;
    }

    /**
     * @param aCurrentSize the currentSize to set
     */
    public static void setCurrentSize(int aCurrentSize)
    {
        currentSize = aCurrentSize;
    }
    
    protected Pieces2D()
    {
        currentImageSet = imageSets.get(currentSize);
    }
    
    public  final void resize(int squareSize)
    {
        if (null != Pieces2D.setsSizes)
        {
            int closest = getSizeToLoad(squareSize);
            if (currentSize != closest)
            {
                currentSize = closest;
                currentImageSet = imageSets.get(currentSize);
            }
        }
    }
    
    private static Map<Integer, Map<Colors, Map<String, Image>>> initImagesSets()
    {
        Map<Integer, Map<Colors, Map<String, Image>>> result = new HashMap<>();
        for (int size : Pieces2D.setsSizes)
        {
            Map<Colors, Map<String, Image>> localImages = new HashMap<>();
            localImages.put(Colors.BLACK, getPieceMap(Colors.BLACK, size));
            localImages.put(Colors.WHITE, getPieceMap(Colors.WHITE, size));   
            result.put(size, localImages);
        }
        return result;
    }
    
    private static Map<String, Image> getPieceMap(Colors color, int size)
    {
        Map<String, Image> result = new HashMap<>();  
        
        result.put(Pawn.class.getName(), GUI.loadPieceImage(Pawn.class.getSimpleName(), color, size, FILE_EXT));
        result.put(Knight.class.getName(), GUI.loadPieceImage(Knight.class.getSimpleName(), color, size, FILE_EXT));
        result.put(Queen.class.getName(), GUI.loadPieceImage(Queen.class.getSimpleName(), color, size, FILE_EXT));
        result.put(Rook.class.getName(), GUI.loadPieceImage(Rook.class.getSimpleName(), color, size, FILE_EXT));
        result.put(King.class.getName(), GUI.loadPieceImage(King.class.getSimpleName(), color, size, FILE_EXT));
        result.put(Bishop.class.getName(), GUI.loadPieceImage(Bishop.class.getSimpleName(), color, size, FILE_EXT));    
        
        return result;
    }
    
    private int getSizeToLoad(int squareHeight)
    {
        Integer closest = Pieces2D.setsSizes.ceiling(squareHeight);
        if (null == closest) 
        {
            closest = Pieces2D.setsSizes.last();
        }        
        return closest;
    }   
    
    public static synchronized Pieces2D getInstance()
    {
        if (null == instance)
        {
            instance = new Pieces2D();
        }
        return instance;
    }
    
    public Image getImage(Colors color, Piece piece)
    {
        return getCurrentImageSet().get(color).get(piece.getClass().getName());
    }
    
    public static void draw(ChessboardView chessboardView, Piece piece, Graphics g, Image image)
    {
        draw(chessboardView, piece, piece.getSquare().getPozX(), piece.getSquare().getPozY(), g, image);
    }    
    
    public static void draw(ChessboardView chessboardView, Piece piece, int pozX, int pozY, Graphics g, Image image)
    {
        try
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Point topLeft = chessboardView.getTopLeftPoint();
            int height = chessboardView.getSquareHeight();
            int x = (pozX * height) + topLeft.x;
            int y = (pozY * height) + topLeft.y;
            if (image != null && g != null)
            {
                Image tempImage = image;
                BufferedImage resized = new BufferedImage(height, height, BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D imageGr = (Graphics2D) resized.createGraphics();
                imageGr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                imageGr.drawImage(tempImage, 0, 0, height, height, null);
                imageGr.dispose();
                image = resized.getScaledInstance(height, height, 0);
                g2d.drawImage(image, x, y, null);
            }
            else
            {
                LOG.error("Piece/draw: image is null!");
            }

        }
        catch (NullPointerException exc)
        {
            LOG.error("Something wrong when painting piece: " + exc.getMessage());
        }
    }      
}
