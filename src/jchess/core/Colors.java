/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jchess.core;

/**
 * @author Mateusz Sławomir Lach
 */
public enum Colors
{
    
    WHITE("white", 'w'),
    BLACK("black", 'b');
    
    protected String colorName;
    
    protected char symbol;
    
    Colors(String colorName, char symbol)
    {
        this.colorName = colorName;
        this.symbol = symbol;
    }
    
    public String getColorName()
    {
        return colorName;
    }
    
    public char getSymbol()
    {
        return symbol;
    }
    
    public String getSymbolAsString()
    {
        return String.valueOf(symbol);
    }
    
}
