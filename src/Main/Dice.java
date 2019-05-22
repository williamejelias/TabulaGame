package Main;

import java.util.*;

/**
 * DiceInterface represents a pair of dice as used in tabula.
 *
 * It should use the Die class so that all randomness comes from there
 *
 * Requires a constructor with no parameters. Initially the dice have no value until they are rolled
 **/

public class Dice implements DiceInterface {
    private List<DieInterface> dice;
    
    public Dice()
    {
        dice = new ArrayList<DieInterface>();
    }
    
    /**
     * @return true if and only if both of the dice have been rolled
     **/
    public boolean haveRolled() {
        return dice.size() == 2;
    }

    /**
     * Roll both of the dice
     */
    public void roll() {
        clear();
        DieInterface die1 = new Die();
        DieInterface die2 = new Die();
        die1.roll();
        dice.add(die1);
        die2.roll();
        dice.add(die2);
    }

    /**
     * @return four numbers if there is a double, otherwise two
     *
     * @throws NotRolledYetException if either of the dice have not been rolled yet
     **/
    public List<Integer> getValues() throws NotRolledYetException
    {
        if (!haveRolled())
        {
            throw new NotRolledYetException("The dice haven't been rolled yet!");
        }
        List<Integer> list = new ArrayList<Integer>();
        if (dice.get(0).getValue() == dice.get(1).getValue()) {
            list.add(dice.get(0).getValue());
            list.add(dice.get(0).getValue());
            list.add(dice.get(1).getValue());
            list.add(dice.get(1).getValue());
        }
        else {
            list.add(dice.get(0).getValue());
            list.add(dice.get(1).getValue());
        }
        return list;
    }

    /**
     * clear both of the dice so they have no value until they are rolled again
     **/
    public void clear()
    {
        dice.clear();
    }

    /**
     * Get the individual dice in a list.
     *
     * @return the Die objects in a list, which will have length 2
     */
    public List<DieInterface> getDice()
    {
        return dice;
    }
}
