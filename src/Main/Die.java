package Main;

import java.util.Random;

/**
 * DieInterface represents a single die.
 *
 * Requires a constructor with no parameters. Initially the die has no value until it is rolled.
 *
 * A single static java.util.Random object should be the source of all randomisation.
 *
 **/

public class Die implements DieInterface
{
    private boolean hasBeenRolled = false;
    private int value = 0;
    
    public Die() {
    }
    
    /**
     * @return false when first constructed or cleared, then true once rolled (unless it is then cleared)
     **/
    public boolean hasRolled()
    {
        return hasBeenRolled;
    }

    /**
     * rolls the die to give a it a value in the range 1-6
     */
    public void roll() {
        value = DieInterface.r.nextInt(DieInterface.NUMBER_OF_SIDES_ON_DIE) + 1;
        hasBeenRolled = true;
    }

    /**
     *
     * @return the visible face of the die, a value in the range 1-6
     *
     * @throws NotRolledYetException if the die has not been rolled or has been cleared since the last roll
     **/
    public int getValue() throws NotRolledYetException {
        if (!hasBeenRolled) {
            throw new NotRolledYetException("The die has not been rolled yet!");
        }
        else {
            return value;
        }
    }

    /**
     * set the face value of the die: only needed when recreating a game state
     *
     * @param value the new value of the die. If it is not in an acceptable range then afterwards hasRolled() should return false.
     **/
    public void setValue(int value) {
        if (value >= 0 && value <= DieInterface.NUMBER_OF_SIDES_ON_DIE) {
            this.value = value;
        }
        else {
            System.out.println("Die value is not in an acceptable range");
        }
    }
    
    /**
     * clears the die so it has no value until it is rolled again
     **/
    public void clear() {
        hasBeenRolled = false;
        value = 0;
    }

    /**
     * sets the seed for the random number generator used by all dice
     *
     * @param seed the seed value to use for randomisation
     **/
    public void setSeed(long seed)
    {
        DieInterface.r.setSeed(seed);
    }
    
}