package jchess.core.moves;

/**
 *
 * @author matis
 */
public enum Castling
{
    NONE(""), 
    SHORT_CASTLING("0-0"), 
    LONG_CASTLING("0-0-0");
    
    protected String symbol;
    
    Castling(String symbol)
    {
        this.symbol = symbol;
    }
    
    public String getSymbol()
    {
        return symbol;
    }
    
    public static boolean isCastling(String moveInPGN)
    {
        return moveInPGN.equals(Castling.SHORT_CASTLING.getSymbol()) 
                || moveInPGN.equals(Castling.LONG_CASTLING.getSymbol());
    }
}
