package Main;

/**
 * LocationInterface represents a single location on the board, but not its position
 * Locations on the main part of the board may only contain a single colour of piece.
 *
 * Off-board locations, i.e. the starting location, the finishing location and the knocked-off location
 * can contain pieces of both colours and are referred to as mixed.
 *
 * Requires a constructor with one parameter (the name of the location), which creates a non-mixed location with no pieces.
 *
 */

public class Location implements LocationInterface 
{
    private String name;
    private int numOfCol1 = 0;
    private int numOfCol2 = 0;
    private boolean isMixed = false;
    private Colour colour1 = Colour.values()[0];
    private Colour colour2 = Colour.values()[1];

    public Location(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return true if and only if the location allows pieces of both colours
     */
    public boolean isMixed()
    {
        return isMixed;
    }

    /**
     * @param isMixed true if and only if the location allows pieces of both colours
     */
    public void setMixed(boolean isMixed)
    {
        this.isMixed = isMixed;
    }

    /**
     * @return true if and only if the location has no pieces in it
     */
    public boolean isEmpty() {
        return numOfCol1 == 0 && numOfCol2 == 0;
    }

    /**
     * @param colour the colour of pieces to count
     * @return the number of pieces of that colour
     **/
    public int numberOfPieces(Colour colour) {
        if (colour == colour1) {
            return numOfCol1;
        }
        else if (colour == colour2) {
            return numOfCol2;
        }
        else {
            return 0;
        }
    }

    /**
     * @param colour the colour of the piece to add
     * @return true if and only if a piece of that colour can be added (i.e. no IllegalMoveException)
     **/
    public boolean canAddPiece(Colour colour) {
        if (isMixed) {
            return true;
        }
        else if (colour == colour1) {
            return numOfCol2 <= 1;
        }
        else if (colour == colour2) {
            return numOfCol1 <= 1;
        }
        else {
            return false;
        }
    }

    /**
     * @param colour the colour of the piece to add
     *
     * @throws IllegalMoveException if the location is not mixed and already contains two or more pieces 
     * of the other colour
     *
     * @return null if nothing has been knocked off, otherwise the colour of the piece that has been knocked off
     **/
    public Colour addPieceGetKnocked(Colour colour) throws IllegalMoveException {
        if (colour == colour1) {
            if (isMixed || numOfCol2 == 0) {
                numOfCol1 += 1;
                return null;
            } else if (numOfCol2 == 1) {
                numOfCol1 = 1;
                numOfCol2 = 0;
                return colour2;
            } else {
                throw new IllegalMoveException("Location contains 2 or more pieces of the other colour!");
            }
        }
        else if (colour == colour2) {
            if (isMixed || numOfCol1 == 0) {
                numOfCol2 += 1;
                return null;
            } else if (numOfCol1 == 1) {
                numOfCol2 = 1;
                numOfCol1 = 0;
                return colour1;
            } else {
                throw new IllegalMoveException("Location contains 2 or more pieces of the other colour!");
            }
        } else {
            throw new IllegalMoveException("Inputted colour is not used by either player!");
        }
    }

    /**
     * @param colour the colour of the piece to remove
     * @return true if and only if a piece of that colour can be removed (i.e. no IllegalMoveException)
     **/
    public boolean canRemovePiece(Colour colour)
    {
        if (colour == colour1)
        {
            if (numOfCol1 > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if (colour == colour2)
        {
            if (numOfCol2 > 0)
            {
                return true;
            }
            else 
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * @param colour the colour of the piece to remove
     *
     * @throws IllegalMoveException if there are no pieces of that colour in the location
     *
     **/
    public void removePiece(Colour colour) throws IllegalMoveException
    {
        if (canRemovePiece(colour))
        {
            if (colour == colour1)
            {
                numOfCol1 -= 1;
            }
            else
            {
                numOfCol2 -= 1;
            }
        }
        else 
        {
            throw new IllegalMoveException("There are no pieces of that colour to remove from that location!");
        }
    }

    /**
     * @return true if and only if the Location is in a valid state depending on the number of each colour and whether or not it is a mixed location
     */
    public boolean isValid()
    {
        //either lots of one colour and none of the other, or one of a colour and zero of the other, or zero of both colours
        if (isMixed() || (numOfCol1 > 0 && numOfCol2 == 0) || (numOfCol1 == 0 && numOfCol2 > 0) || (numOfCol1 == 0 && numOfCol2 == 0))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
