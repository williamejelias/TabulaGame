package Main;

/**
 * MoveInterface represents the use of a single Die to move a single piece.
 *
 * Requires a constructor with no parameters.
 *
 **/

public class Move implements MoveInterface {
    private int sourceLocation;
    private int valueOfDice;
    
    public Move() {
    }
    
    /**
     * @param locationNumber represents the board position to move a piece from
     * in the range 0-24. 0 reresents off the board (the knocked location if there are pieces there, otherwise the off-board start location). A locationNumber of 1-24 refers to locations on the board with 1 being the first and 24 being the last.
     * @throws NoSuchLocationException if locationNumber is not in the range 0-24
     **/
    public void setSourceLocation(int locationNumber) throws NoSuchLocationException {
        if (locationNumber >= 0 && locationNumber <= BoardInterface.NUMBER_OF_LOCATIONS) {
            sourceLocation = locationNumber;
        }
        else {
            throw new NoSuchLocationException("The input is not a Location!");
        }
    }

    public int getSourceLocation()
    {
        return sourceLocation;
    }

    /**
     *
     * @param diceValue represents the value of the dice to be used in the move 
     *
     * @throws IllegalMoveException if diceValue is not in the range 0-6 
     **/
    public void setDiceValue(int diceValue) throws IllegalMoveException {
        if (diceValue >= 0 && diceValue <= DieInterface.NUMBER_OF_SIDES_ON_DIE) {
            valueOfDice = diceValue;
        }
        else {
            throw new IllegalMoveException("Dice value is not in the range 0-6!");
        }
    }

    public int getDiceValue()
    {
        return valueOfDice;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + sourceLocation;
        result = prime * result + valueOfDice;
        return result;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        
        MoveInterface m = (MoveInterface) o;
        return ((this.getDiceValue() == m.getDiceValue()) && (this.getSourceLocation() == m.getSourceLocation()));
    }
}
