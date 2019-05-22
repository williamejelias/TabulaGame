package Main;

import java.util.*;

/**
 * TurnInterface represents a series of moves which constitute a turn by a player.
 *
 * Requires a constructor with no parameters.
 *
 **/

public class Turn implements TurnInterface {
    private List<MoveInterface> moves = new ArrayList<>();

    public Turn() {
    } 

    /**
     * @param move to be added after the moves already defined in the current turn
     *
     * @throws IllegalTurnException if there are already four or more moves in the turn
     */
    public void addMove(MoveInterface move) throws IllegalTurnException {
        if (move != null) {
            if (moves.size() < 4) {
                moves.add(move);
            } else {
                throw new IllegalTurnException("The maximum number of moves (4) have been added in this turn!");
            }
        }
    }

    public List<MoveInterface> getMoves()
    {
        return moves;
    }
}