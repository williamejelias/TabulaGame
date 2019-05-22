package Main;

import java.util.*;
import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.BorderLayout;

/**
 * PlayerInterface represents a player in the game of tabula
 *
 * Up to three different implementations of this interface can be provided: HumanConsolePlayer; ; ComputerPlayer; HumanGUIPlayer
 *
 * Each implementation requires a constructor with no parameters. 
 **/

public class HumanGUIPlayer implements PlayerInterface {
    private boolean noMovesPossible = false;

    public HumanGUIPlayer() {
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
        int numberOfDiceValues = diceValues.size();
        int numberOfMovesAdded = 0;
        TurnInterface turn = new Turn();
        try {
            String turnString = "" + colour + " Turn \nSelect from the drop down menu whether you'd like to pause or continue with the game. \nSelect Option: ";
            List<String> goOrNoStrings = new ArrayList<>();
            goOrNoStrings.add("PAUSE");
            goOrNoStrings.add("CONTINUE WITH TURN");
            int continueOrPause = selector(turnString, goOrNoStrings, board, colour);
            if(goOrNoStrings.get(continueOrPause).equals("PAUSE")) {
                throw new PauseException("Game Paused!");
            }
            else {
                try {
                    int numberString = 1;
                    while (numberOfMovesAdded != numberOfDiceValues) {
                        Set<MoveInterface> possMovesList = board.possibleMoves(colour, diceValues);
                        List<String> strings = new ArrayList<>();
                        for (MoveInterface move : possMovesList) {
                            strings.add("Move piece from " + move.getSourceLocation() + " to " + (move.getSourceLocation() + move.getDiceValue()));
                        }
                        
                        if (possMovesList.isEmpty()) {
                            turnString = "No moves are possible, further moves are forfeited";
                        }
                        else {
                            turnString = ("Select move no." + numberString + " to add to your turn. \nAvailable dice values: " + diceValues + " \nSelect Option: " );
                            numberString += 1;
                        }

                        int selected = selector(turnString, strings, board, colour);
                        String selectedMove = strings.get(selected);
                        String[] selectedMoveParts = selectedMove.split(" ");
                        int source = Integer.parseInt(selectedMoveParts[3]);
                        int moveTo = Integer.parseInt(selectedMoveParts[5]);
                        for (MoveInterface move : possMovesList) {
                            if (source == move.getSourceLocation() && moveTo == (move.getSourceLocation() + move.getDiceValue())) {
                                turn.addMove(move);
                                numberOfMovesAdded += 1;
                                diceValues.remove((Integer)(moveTo - source));
                                board.makeMove(colour, move);
                            }
                        }
                    }
                }
                catch (IllegalTurnException | IllegalMoveException | ArrayIndexOutOfBoundsException e) {
                    //should not happen
                    System.out.println("ERROR - move in possible moves list is illegal");
                    return turn;
                }
            }
        }
        catch (PauseException e) {
            throw new PauseException("GamePaused");
        }
        return turn;
    }

    public int selector(String text, List<String> choices, BoardInterface board, Colour colour) {
        JPanel content = new JPanel();
        content.setPreferredSize(new Dimension(850, 300));
        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
        
        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        JTextArea contextText = new JTextArea(text);
        JComboBox<Object> combox = new JComboBox<>(choices.toArray());
        inputPanel.add(contextText);
        inputPanel.add(combox);
        
        JPanel boardPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        JLabel boardLabel = new JLabel("Current board state:        ");
        JTextArea boardTextArea = new JTextArea(board.toString());
        JScrollPane scrollPane = new JScrollPane(boardTextArea);
        boardPanel.add(boardLabel);
        boardPanel.add(scrollPane);
        
        content.add(boardPanel);
        content.add(inputPanel);
        String title = "" + colour + " Turn";
        JOptionPane.showMessageDialog(null, content, title, JOptionPane.PLAIN_MESSAGE);
        return combox.getSelectedIndex();
    }
}
