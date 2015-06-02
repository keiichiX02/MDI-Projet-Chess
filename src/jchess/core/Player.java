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
 * Authors:
 * Mateusz Sławomir Lach ( matlak, msl )
 * Damian Marciniak
 */
package jchess.core;

import java.io.Serializable;


/**
 * Class representing the player in the game
 */
public class Player implements Serializable
{

    protected String name;

    protected Colors color;

    public enum playerTypes
    {
        localUser, networkUser, computer
    }
    
    protected playerTypes playerType;
    
    protected boolean goDown;

    /**
     * Empty constructor.
     */
    public Player(){}

    /**
     * Constructor for Player class
     * @param name
     * @param color 
     */
    public Player(String name, String color)
    {
        this.name = name;
        this.color = Colors.valueOf(color.toUpperCase());
        this.goDown = false;
    }

    /** Method setting the players name
     *  @param name name of player
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /** Method getting the players name
     *  @return name of player
     */
    public String getName()
    {
        return this.name;
    }

    /** Method setting the players type
     *  @param type type of player - enumerate
     */
    public void setType(playerTypes type)
    {
        this.playerType = type;
    }

    /**
     * @return the color
     */
    public Colors getColor()
    {
        return color;
    }

    /**
     * @return the playerType
     */
    public playerTypes getPlayerType()
    {
        return playerType;
    }

    /**
     * @return the goDown
     */
    public boolean isGoDown()
    {
        return goDown;
    }    
}
