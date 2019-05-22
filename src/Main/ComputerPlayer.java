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

public class ComputerPlayer implements PlayerInterface {
    private boolean noMovesPossible = false;

    public ComputerPlayer() {
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
        //FIX

        //TACTICS
        //Knock unprotected pieces of the opposite colour
        //Try to protect pieces where possible by stacking them - this prevents a player from making a move to that location
        //move pieces as close to final position as possible - if a move get to finish location, pick the smallest dice value move that can do this
        try {
            noMovesPossible = false;
            int numberOfDiceValues = diceValues.size();
            int numberOfMovesAdded = 0;
            int source = 0;
            int moveTo = 0;
            TurnInterface turn = new Turn();

            MoveInterface maxMoveKnock = null;
            int maxKnock = 0;
            MoveInterface maxMoveStack = null;
            int maxStack = 0;
            MoveInterface minMoveFinish = null;
            int minFinish = BoardInterface.NUMBER_OF_LOCATIONS + Die.NUMBER_OF_SIDES_ON_DIE;
            MoveInterface maxMoveOther = null;
            int maxOther = 0;
            MoveInterface randomPossibleMove = null;
            while (numberOfDiceValues != numberOfMovesAdded) {
                Set<MoveInterface> possMovesList = board.possibleMoves(colour, diceValues);
                if (possMovesList.isEmpty()) {
                    System.out.println("No moves are possible, further moves are forfeited");
                    noMovesPossible = true;
                    return turn;
                }
                randomPossibleMove = (MoveInterface)possMovesList.toArray()[0];
                
                for (MoveInterface move : possMovesList) {
                    //if the moveTo of the move is greater than NUMBER_OF_LOCATIONS then add the smallest move that gets to the final location
                    if ((move.getSourceLocation() + move.getDiceValue()) > Board.NUMBER_OF_LOCATIONS) {
                        if ((move.getSourceLocation() + move.getDiceValue()) < minFinish) {
                            minFinish = move.getSourceLocation() + move.getDiceValue();
                            minMoveFinish = move;
                            source = move.getSourceLocation();
                            moveTo = move.getSourceLocation() + move.getDiceValue();
                        }
                    }
                    else {
                        //prioritise knocking the furthest away piece possible
                        if (board.getBoardLocation(move.getSourceLocation() + move.getDiceValue()).numberOfPieces(colour.otherColour()) == 1) {
                            if ((move.getSourceLocation() + move.getDiceValue()) > maxKnock) {
                                maxKnock = move.getSourceLocation() + move.getDiceValue();
                                maxMoveKnock = move;
                                source = move.getSourceLocation();
                                moveTo = move.getSourceLocation() + move.getDiceValue();
                            }
                        }
                        //otherwise stack pieces where possible
                        else if (board.getBoardLocation(move.getSourceLocation() + move.getDiceValue()).numberOfPieces(colour) == 1) {
                            if ((move.getSourceLocation() + move.getDiceValue()) > maxStack) {
                                maxStack = move.getSourceLocation() + move.getDiceValue();
                                maxMoveStack = move;
                                source = move.getSourceLocation();
                                moveTo = move.getSourceLocation() + move.getDiceValue();
                            }
                        }
                        //otherwise move the piece that gets closest to the final location
                        else {
                            if ((move.getSourceLocation() + move.getDiceValue()) > maxOther) {
                                maxOther = move.getSourceLocation() + move.getDiceValue();
                                maxMoveOther = move;
                                source = move.getSourceLocation();
                                moveTo = move.getSourceLocation() + move.getDiceValue();
                            }
                        }
                    }
                }

                //prioritise knocking the furthest away piece possible
                if (maxMoveKnock != null) {
                    turn.addMove(maxMoveKnock);
                    board.makeMove(colour, maxMoveKnock);
                    diceValues.remove((Integer)maxMoveKnock.getDiceValue());
                    numberOfMovesAdded += 1;
                    System.out.println("Computer moved a piece from " + maxMoveKnock.getSourceLocation() + " to " + (maxMoveKnock.getSourceLocation() + maxMoveKnock.getDiceValue()));
                    maxKnock = 0;
                    maxStack = 0;
                    minFinish = BoardInterface.NUMBER_OF_LOCATIONS + Die.NUMBER_OF_SIDES_ON_DIE;
                    maxOther = 0;
                    maxMoveKnock = null;
                    maxMoveStack = null;
                    minMoveFinish = null;
                    maxMoveOther = null;
                }

                //otherwise stack pieces where possible
                else if (maxMoveStack != null) {
                    turn.addMove(maxMoveStack);
                    board.makeMove(colour, maxMoveStack);
                    diceValues.remove((Integer)maxMoveStack.getDiceValue());
                    numberOfMovesAdded += 1;
                    System.out.println("Computer moved a piece from " + maxMoveStack.getSourceLocation() + " to " + (maxMoveStack.getSourceLocation() + maxMoveStack.getDiceValue()));
                    maxKnock = 0;
                    maxStack = 0;
                    minFinish = BoardInterface.NUMBER_OF_LOCATIONS + Die.NUMBER_OF_SIDES_ON_DIE;
                    maxOther = 0;
                    maxMoveKnock = null;
                    maxMoveStack = null;
                    minMoveFinish = null;
                    maxMoveOther = null;
                }

                //if the moveTo of the move is greater than NUMBER_OF_LOCATIONS then add the smallest move that gets to the final location
                else if (minMoveFinish != null) {
                    turn.addMove(minMoveFinish);
                    board.makeMove(colour, minMoveFinish);
                    diceValues.remove((Integer)minMoveFinish.getDiceValue());
                    numberOfMovesAdded += 1;
                    System.out.println("Computer moved a piece from " + minMoveFinish.getSourceLocation() + " to " + (minMoveFinish.getSourceLocation() + minMoveFinish.getDiceValue()));
                    maxKnock = 0;
                    maxStack = 0;
                    minFinish = BoardInterface.NUMBER_OF_LOCATIONS + Die.NUMBER_OF_SIDES_ON_DIE;
                    maxOther = 0;
                    maxMoveKnock = null;
                    maxMoveStack = null;
                    minMoveFinish = null;
                    maxMoveOther = null;
                }
                //otherwise move the piece that gets closest to the final location
                else if (maxMoveOther != null) {
                    turn.addMove(maxMoveOther);
                    board.makeMove(colour, maxMoveOther);
                    diceValues.remove((Integer)maxMoveOther.getDiceValue());
                    numberOfMovesAdded += 1;
                    System.out.println("Computer moved a piece from " + maxMoveOther.getSourceLocation() + " to " + (maxMoveOther.getSourceLocation() + maxMoveOther.getDiceValue()));
                    maxKnock = 0;
                    maxStack = 0;
                    minFinish = BoardInterface.NUMBER_OF_LOCATIONS + Die.NUMBER_OF_SIDES_ON_DIE;
                    maxOther = 0;
                    maxMoveKnock = null;
                    maxMoveStack = null;
                    minMoveFinish = null;
                    maxMoveOther = null;
                }
                //otherwise pick fist move in list
                //shouldnt happen really
                else {
                    turn.addMove(randomPossibleMove);
                    board.makeMove(colour, randomPossibleMove);
                    diceValues.remove((Integer)randomPossibleMove.getDiceValue());
                    numberOfMovesAdded += 1;
                    System.out.println("Computer moved a piece from " + randomPossibleMove.getSourceLocation() + " to " + (randomPossibleMove.getSourceLocation() + randomPossibleMove.getDiceValue()));
                }
            }
            return turn;
        }

        catch (IllegalTurnException e) {
            System.out.println("ERROR - tried to add more than 4 moves in getTurn() comp player!!!");
            return null;
        }
        catch (IllegalMoveException e) {
            System.out.println("ERROR - illegal move exception in getTurn() comp player!!!");
            return null;
        }
        catch (NoSuchLocationException e) {
            //should not happen
            System.out.println("ERROR - no such location exception thrown in getTurn() comp player");
            return null;
        }
    }
}
