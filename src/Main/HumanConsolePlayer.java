package Main;

import java.util.List;
import java.util.*;

/**
 * PlayerInterface represents a player in the game of tabula
 *
 * Up to three different implementations of this interface can be provided: HumanConsolePlayer; ; ComputerPlayer; HumanGUIPlayer
 *
 * Each implementation requires a constructor with no parameters. 
 **/

public class HumanConsolePlayer implements PlayerInterface {
    private boolean noMovesPossible = false;

    public HumanConsolePlayer() {
    }

    public boolean getBooleanMovesPossible()
    {
        return noMovesPossible;
    }

    /**
     * Get from the player the turn that they wish to take
     *
     * @param colour the Colour they are playing as
     * 
     * @param board a clone of the current board state, so that the player can try different moves
     *
     * @param diceValues a list of the dice values the player can use.
     *
     * @return the turn the player wishes to take. It is the Player's responsibility to ensure that the turn is legal, matches the provided diceValues and uses as may of the diceValues as possible.
     *
     * @throws PauseException is only used by human players if they are in the middle of a game and wish to pause the game instead of taking a turn. 
     **/
    public TurnInterface getTurn(Colour colour, BoardInterface board, List<Integer> diceValues) throws PauseException {
        try {
            noMovesPossible = false;
            int numberOfDiceValues = diceValues.size();
            int numberOfMovesAdded = 0;
            Scanner in = new Scanner(System.in);
            System.out.println("To pause the game, type 'PAUSE' (This will save and quit the game such that it can be resumed at another time)");
            System.out.println("To carry on with your turn, press the 'Enter' key"); 
            if (in.nextLine().equals("PAUSE")) {
                throw new PauseException("Game Paused!");
            }
            else {
                TurnInterface turn = new Turn();
                System.out.println("Possible moves for the player: ");
                Set<MoveInterface> possMovesList = board.possibleMoves(colour, diceValues);
                if (possMovesList.isEmpty()) {
                    System.out.println("No moves are possible, turn is forfeited");
                    noMovesPossible = true;
                    return turn;
                }
                for (MoveInterface move : possMovesList) {
                    //prints each move to a new line
                    System.out.println("Move piece from " + move.getSourceLocation() + " to " + (move.getSourceLocation() + move.getDiceValue()));
                }

                //sets are unordered, hence need to do ask for the sourceLocation() and diceValue() of a move when referring to a possible move
                System.out.println("Out of the above possible moves, select your first move by first entering the source location of the move, and then its moveTo location");
                int source = 0;
                int moveTo = 0;
                boolean inputsAreIntegers = false;
                while(!inputsAreIntegers) {
                    String inputSource = in.next();
                    String inputMoveTo = in.next();
                    try {
                        source = Integer.parseInt(inputSource);
                        moveTo = Integer.parseInt(inputMoveTo);
                        inputsAreIntegers = true;
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Enter only integers when referring to 'source' and 'move to' locations - Please re-enter both inputs");
                        inputsAreIntegers = false;
                    }
                }
                boolean addedMove = false;
                boolean moveInList = false;
                while (!addedMove) {
                    for (MoveInterface move : possMovesList) {
                        if (!moveInList) {
                            //check if source and moveTo is in list of moves
                            if ((source == move.getSourceLocation()) && (moveTo == (move.getDiceValue() + move.getSourceLocation()))) {
                                turn.addMove(move);
                                board.makeMove(colour, move);
                                diceValues.remove(((Integer)(moveTo - source)));
                                addedMove = true;
                                moveInList = true;
                                numberOfMovesAdded += 1;
                            }
                        }
                    }
                    //after going through each move checking source and moveTo, if not found, do following and ask for move again
                    if (!moveInList)
                    {
                        System.out.println("That move is not possible, please pick one of the following moves by entering the source, and moveTo location");
                        for (MoveInterface move : possMovesList)
                        {
                            //prints each move to a new line
                            System.out.println("Move piece from " + move.getSourceLocation() + " to " + (move.getSourceLocation() + move.getDiceValue()));
                        }
                        source = 0;
                        moveTo = 0;
                        inputsAreIntegers = false;
                        while(!inputsAreIntegers) {
                            String inputSource = in.next();
                            String inputMoveTo = in.next();
                            try {
                                source = Integer.parseInt(inputSource);
                                moveTo = Integer.parseInt(inputMoveTo);
                                inputsAreIntegers = true;
                            }
                            catch (NumberFormatException e) {
                                System.out.println("Enter only integers when referring to 'source' and 'move to' locations - Please re-enter both inputs");
                                inputsAreIntegers = false;
                            }
                        }
                        addedMove = false;
                        moveInList = false;
                    }
                }
                //need to perform the move on a clone board, then provide new possible moves, such that a full turn can be made
                //some moves only become available after other moves are performed

                while (numberOfDiceValues != numberOfMovesAdded) {
                    possMovesList = board.possibleMoves(colour, diceValues);
                    System.out.println("Possible moves for the player: ");
                    if (possMovesList.isEmpty()) {
                        System.out.println("No further moves are possible");
                        return turn;
                    }
                    for (MoveInterface move : possMovesList) {
                        //prints each move to a new line
                        System.out.println("Move piece from " + move.getSourceLocation() + " to " + (move.getSourceLocation() + move.getDiceValue()));
                    }

                    System.out.println("Select your next move by the same method");
                    source = 0;
                    moveTo = 0;
                    inputsAreIntegers = false;
                    while(!inputsAreIntegers) {
                        String inputSource = in.next();
                        String inputMoveTo = in.next();
                        try {
                            source = Integer.parseInt(inputSource);
                            moveTo = Integer.parseInt(inputMoveTo);
                            inputsAreIntegers = true;
                        }
                        catch (NumberFormatException e) {
                            System.out.println("Enter only integers when referring to 'source' and 'move to' locations - Please re-enter both inputs");
                            inputsAreIntegers = false;
                        }
                    }
                    addedMove = false;
                    moveInList = false;
                    while (!addedMove) {
                        for (MoveInterface move : possMovesList) {
                            if (!moveInList) {
                                //check if source and moveTo is in list of moves
                                if ((source == move.getSourceLocation()) && (moveTo == (move.getDiceValue() + move.getSourceLocation()))) {
                                    turn.addMove(move);
                                    board.makeMove(colour, move);
                                    diceValues.remove(((Integer)(moveTo - source)));
                                    addedMove = true;
                                    moveInList = true;
                                    numberOfMovesAdded += 1;
                                }
                            }
                        }
                        //after going through each move checking source and moveTo, if not found, do following and ask for move again
                        if (!moveInList) {
                            System.out.println("That move is not possible, please pick one of the following moves by entering the source, and moveTo location");
                            for (MoveInterface move : board.possibleMoves(colour, diceValues)) {
                                //prints each move to a new line
                                System.out.println("Move piece from " + move.getSourceLocation() + " to " + (move.getSourceLocation() + move.getDiceValue()));
                            }
                            source = 0;
                            moveTo = 0;
                            inputsAreIntegers = false;
                            while(!inputsAreIntegers) {
                                String inputSource = in.next();
                                String inputMoveTo = in.next();
                                try {
                                    source = Integer.parseInt(inputSource);
                                    moveTo = Integer.parseInt(inputMoveTo);
                                    inputsAreIntegers = true;
                                }
                                catch (NumberFormatException e) {
                                    System.out.println("Enter only integers when referring to 'source' and 'move to' locations - Please re-enter both inputs");
                                    inputsAreIntegers = false;
                                }
                            }
                            addedMove = false;
                            moveInList = false;
                        }
                    }
                }
                System.out.println();
                return turn;
            }
        }
        catch (PauseException e) {
            throw new PauseException("Game Paused!");
        }
        catch (IllegalTurnException | IllegalMoveException e) {
            System.out.println("ERROR - move in possible moves list is illegal!!!");
            return null;
        }
    }
}

