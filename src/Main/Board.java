package Main;

import java.util.*;
import java.util.Set;

/**
 * BoardInterface represents the board state in the game of tabula (not including dice and players).
 *
 * Requires a constructor with no parameters which creates and initialises all of the locations for the start of the game.
 *
 **/
public class Board implements BoardInterface 
{
    private Location knock;
    private Location start;
    private Location end;

    private List<LocationInterface> startList;
    private List<LocationInterface> knockList;
    private List<LocationInterface> endList;

    private List<LocationInterface> locations;
    private boolean thereIsAWinner = false;
    private Colour winningColour = null;

    private Colour colour1 = Colour.values()[0];
    private Colour colour2 = Colour.values()[1];

    public Board() {
        LocationInterface knock = new Location("Knock");
        LocationInterface start = new Location("Start");
        LocationInterface end = new Location("end");
        start.setMixed(true);                                   //knock location
        knock.setMixed(true);                                   //start location
        end.setMixed(true);                                     //end location

        try {
            for (int i = 1; i <= BoardInterface.PIECES_PER_PLAYER; i ++) {
                //initialise the start locations
                start.addPieceGetKnocked(colour1);
                start.addPieceGetKnocked(colour2);  
            }
        }
        catch (IllegalMoveException ignored) {
        }
        startList = new ArrayList<>();
        startList.add(start);
        knockList = new ArrayList<>();
        knockList.add(knock);
        endList = new ArrayList<>();
        endList.add(end);

        locations = new ArrayList<>();
        for (int i = 1; i <= BoardInterface.NUMBER_OF_LOCATIONS; i++) {
            String name = "Location" + i;
            LocationInterface location = new Location(name);
            locations.add(location);
        }
        // ^ does not include knock/start/end location
    }

    public void setName(String name) {
    }

    /**
     * @return the list of locations (excludes knock/start location)
     */
    public List<LocationInterface> getLocationList()
    {
        return locations;
    }

    /**
     * @return the Location off the board where all pieces start the game. This will be a mixed location.
     **/
    public LocationInterface getStartLocation() {
        //just returning the location seemingly doesnt work?
        return startList.get(0);
    }

    /**
     * @return the Location off the board where pieces get to when they have gone all the way round the board. This will be a mixed location.
     **/
    public LocationInterface getEndLocation() {
        //just returning the location seemingly doesnt work?
        return endList.get(0);
    }

    /**
     * @return the Location where pieces go to when they are knocked off the board by an opposing piece. This will be a mixed location.
     **/
    public LocationInterface getKnockedLocation() {
        //just returning the location seemingly doesnt work?
        return knockList.get(0);
    }

    /**
     * @return the Location corresponding to a numbered position on the board. This will not be a mixed location.
     *
     * @param locationNumber the number of the location going from 1-24
     *
     * @throws NoSuchLocationException when position is not in the range 1-24
     **/
    public LocationInterface getBoardLocation(int locationNumber) throws NoSuchLocationException {
        if (locationNumber >= 1 && locationNumber <= BoardInterface.NUMBER_OF_LOCATIONS) {
            return locations.get(locationNumber - 1);
        }
        else {
            throw new NoSuchLocationException("No such location!");
        }
    }

    /**
     * @param colour the colour to move
     *
     * @param move the move to make
     *
     * @return true if and only if, from the current board state it would be legal for the given colour to make the given move.
     **/
    public boolean canMakeMove(Colour colour, MoveInterface move) {
        try {
            //checks whether a colour can be added at the location of (move.getSourceLocation + move.getDiceValue) - allowed even if this value is greater than 24 (the piece goes to the end location)
            //needs see whether the proposed location.canAddPieceGetKnocked() 
            int source = move.getSourceLocation();

            LocationInterface sourceLocation = movecheck(colour, source);

            int moveTo = move.getSourceLocation() + move.getDiceValue();
            LocationInterface moveToLocation = null;

            if (moveTo > BoardInterface.NUMBER_OF_LOCATIONS) {
                moveToLocation = getEndLocation();
            }
            else {
                moveToLocation = getBoardLocation(moveTo);
            }

            //can make a move if can remove a piece from current location and add a piece to the new position
            //pieces of both colours can be added to knock and l24
            boolean can = false;

            if (moveToLocation.canAddPiece(colour) && (sourceLocation.numberOfPieces(colour) > 0)) {
                can = true;
            }
            return can;
        }
        catch (NoSuchLocationException e) {
            System.out.println("no location found");
            return false;
        }
        catch (NullPointerException e) {
            //inputted move object was null
            //should not happen
            return false;
        }
    }

    /**
     * Update the Board state by making the given move for the given colour, including any knocking off.
     *
     * @param colour the colour to move
     *
     * @param move the move to make
     *
     * @throws IllegalMoveException if and only if the move is not legal.
     **/
    public void makeMove(Colour colour, MoveInterface move) throws IllegalMoveException {
        //add a piece at the location of (move.getSourceLocation + move.getDiceValue) and remove a piece of the input colour at (move.getSourceLocation)
        //throws IllegalMoveException in the case of the location of (move.getSourceLocation + move.getDiceValue) being invalid
        if (canMakeMove(colour, move)) {
            int source = move.getSourceLocation();
            int moveTo = move.getSourceLocation() + move.getDiceValue();
            LocationInterface moveToLocation = null;
            if (moveTo > BoardInterface.NUMBER_OF_LOCATIONS) {
                moveToLocation = getEndLocation();
            }
            else {
                try {
                    moveToLocation = getBoardLocation(moveTo);
                }
                catch (NoSuchLocationException e) {
                    //should not happen
                    System.out.println("unable to find end location in canMakeMove method");
                }
            }
            try {

                LocationInterface sourceLocation = movecheck(colour, source);

                //function is called in the if statement, no need to add pieces at moveTo 
                if (moveToLocation.addPieceGetKnocked(colour) == null) {
                    //piece added at moveToLocation (allowed if moveToLocation is mixed or only 1 or 0 pieces of other colour)
                    //remove a piece of that colour from move.getSourceLocation()
                    sourceLocation.removePiece(colour);
                }
                else {
                    //location is not mixed - added a piece of that colour, remove a piece of that colour from sourceLocation, and add a piece of the other colour to knock
                    sourceLocation.removePiece(colour);
                    getKnockedLocation().addPieceGetKnocked(colour.otherColour());
                }
            }
            catch (NoSuchLocationException e) {
                System.out.println("No such location!");
            }
        }
        else {
            System.out.println("Illegal move exception thrown - move is illegal in canMakeMove()");
            throw new IllegalMoveException("Move is Illegal!");
        }
    }

    private LocationInterface movecheck(Colour colour, int source) throws NoSuchLocationException {
        LocationInterface sourceLocation = null;    //needs to have initial value, will be guaranteed to update later
        if (source == 0) {
            if (getKnockedLocation().numberOfPieces(colour) > 0) {
                sourceLocation = getKnockedLocation();
            }
            else if (getStartLocation().numberOfPieces(colour) > 0) {
                sourceLocation = getStartLocation();
            }
        }
        else {
            sourceLocation = getBoardLocation(source);
        }
        return sourceLocation;
    }

    /**
     * Update the Board state by making the all of the moves in the given turn in order, including any knocking off, based on the given diceValues.
     *
     * @param colour the colour to move
     *
     * @param turn the turn to take
     *
     * @param diceValues the values of the dice available in no particular order. There will be repeated values in the list if a double is thrown
     *
     * @throws IllegalTurnException if and only if the turns in the move are not legal for the diceValues give. Each of the moves has to be legal, and the diceValues in the moves of the turn must match the diceValues parameter. The number of moves in the turn must be no less than the maximum possible number of legal moves: all available dice must be used. If IllegalTurnException is thrown then the board state remains unchanged.
     **/
    public void takeTurn(Colour colour, TurnInterface turn, List<Integer> diceValues) throws IllegalTurnException {
        //for each move in 'turn', makeMove(turn) for the specified colour                                           |
        //use the output of dice.getValues() for the list of diceValues check the field diceValue in the move object v
        //throws IllegalMoveException if values in the list of diceValues do not match the diceValue field in the move object -- if thrown board state remains unchanged
        List<Integer> diceValuesCopy = new ArrayList<>(diceValues);
        boolean valid = false;
        if (!turn.getMoves().isEmpty()) {
            List<MoveInterface> moveList = turn.getMoves();
            for (MoveInterface move : moveList) {
                //even if there are not as many moves as dice values, will work
                if (diceValuesCopy.contains(move.getDiceValue())) {
                    diceValuesCopy.remove(((Integer)move.getDiceValue()));
                    valid = true;
                }
                else {
                    valid = false;
                }
            }
            if (valid) {
                for (MoveInterface move : turn.getMoves()) {
                    try {
                        makeMove(colour, move);
                    }
                    catch (IllegalMoveException e) {
                        System.out.println("Illegal turn exception, move in list of turn.getMoves is not legal");
                        throw new IllegalTurnException("Illegal turn exception, move in list of turn.getMoves is not legal");
                    }
                }
            }
            else {
                System.out.println("Illegal turn exception, valid == false in takeTurn()");
                throw new IllegalTurnException("Illegal turn exception, valid == false in takeTurn()");
            }
        }
    }

    /**
     * @param colour the colour to check
     *
     * @return true if and only if the given colour has won
     **/
    public boolean isWinner(Colour colour) {
        //return true if all pieces per player of a colour are in the final position (and set the winnerColour variable to be the colour in the paramater), else return null
        if (getEndLocation().numberOfPieces(colour) == BoardInterface.PIECES_PER_PLAYER)  {
            winningColour = colour;
            thereIsAWinner = true;
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * @return the colour of the winner if there is one, otherwise null
     **/
    public Colour winner() {
        // check that the boolean thereIsAWinner is true, return the colour (field winnerColour) associated with the variable
        if (thereIsAWinner) {
            return winningColour;
        }
        else {
            return null;
        }
    }

    /**
     * @return true if and only if the Board is in a valid state (do not need to check whether or not it could be reached by a valid sequence of moves)
     **/
    public boolean isValid() {
        //check that the total number of pieces on the board of each colour adds up to PIECES_PER_PLAYER
        int sumColour1 = 0;
        int sumColour2 = 0;
        for (LocationInterface location : locations) {
            sumColour1 += location.numberOfPieces(colour1);
            sumColour2 += location.numberOfPieces(colour2);
        }
        sumColour1 += getKnockedLocation().numberOfPieces(colour1);
        sumColour2 += getKnockedLocation().numberOfPieces(colour2);
        sumColour1 += getStartLocation().numberOfPieces(colour1);
        sumColour2 += getStartLocation().numberOfPieces(colour2);
        sumColour1 += getEndLocation().numberOfPieces(colour1);
        sumColour2 += getEndLocation().numberOfPieces(colour2);
        boolean allPieces;
        if (sumColour1 == BoardInterface.PIECES_PER_PLAYER && sumColour2 == BoardInterface.PIECES_PER_PLAYER) {
            allPieces = true;
        }
        else {
            allPieces = false;
            System.out.println("initial pieces on start not what they should be");
        }

        //check that the only positions that are mixed are the knock, start, and end locations
        boolean allLocationsCorrectMixed;
        for (LocationInterface location : locations) {
            allLocationsCorrectMixed = !location.isMixed();
        }
        allLocationsCorrectMixed = getKnockedLocation().isMixed() && getStartLocation().isMixed() && getEndLocation().isMixed();

        //check both conditions are satisfied
        return allPieces && allLocationsCorrectMixed;
    }

    /**
     * @param colour the colour to move next
     *
     * @param diceValues the dice values available to use
     *
     * @return a list of moves that the given colour can make from the current board state with (any one of) the given diceValues
     **/
    public Set<MoveInterface> possibleMoves(Colour colour, List<Integer> diceValues) {
        //for each diceValue in the list, need to check whether (location of a piece + the diceValue) can be reached/ is a valid move from the pieces' location

        Set<MoveInterface> possibleMoves = new LinkedHashSet<MoveInterface>();
        //knocked location takes priority (must move knocked pieces from the start location before other moves can be made) and uses a move
        try {
            if (getKnockedLocation().numberOfPieces(colour) > 0) {
                for (Integer diceValue : diceValues) {
                    MoveInterface possibleMove = new Move();
                    possibleMove.setSourceLocation(0);
                    possibleMove.setDiceValue(diceValue);
                    if (canMakeMove(colour, possibleMove)) {
                        possibleMoves.add(possibleMove);
                    }
                }
            }
            else {
                for (int i = 1; i <= BoardInterface.NUMBER_OF_LOCATIONS; i ++) {
                    //all locations that have pieces of that colour
                    if (getBoardLocation(i).numberOfPieces(colour) > 0) {
                        for (Integer diceValue : diceValues) {
                            MoveInterface possibleMove = new Move();
                            possibleMove.setSourceLocation(i);
                            possibleMove.setDiceValue(diceValue);
                            if (canMakeMove(colour, possibleMove)) {
                                possibleMoves.add(possibleMove);
                            }
                        }
                    }
                }
                if (getStartLocation().numberOfPieces(colour) > 0) {
                    for (Integer diceValue : diceValues) {
                        MoveInterface possibleMove = new Move();
                        possibleMove.setSourceLocation(0);
                        possibleMove.setDiceValue(diceValue);
                        if (canMakeMove(colour, possibleMove)) {
                            possibleMoves.add(possibleMove);
                        }
                    }
                }
            }
            return possibleMoves;
        }
        catch (NoSuchLocationException | IllegalMoveException e)  {
            //fails to set the source location as the integer in range of 0 to number of locations || dice value is not in range of 0 - number of sides on die
            //should not happen
            return null;
        }
    }

    /**
     * @return a copy of the board that can be passed to players to work with
     */
    public BoardInterface clone() throws CloneNotSupportedException {
        BoardInterface clone = (BoardInterface) super.clone();
        try {
            BoardInterface boardClone = new Board();

            //remove starting pieces from start location, as creating a new board initialises with number of pieces per player of each colour at the start position(1)
            for (int i = 1; i <= BoardInterface.PIECES_PER_PLAYER; i++) {
                boardClone.getStartLocation().removePiece(colour1);
            }
            for (int i = 1; i <= BoardInterface.PIECES_PER_PLAYER; i++) {
                boardClone.getStartLocation().removePiece(colour2);
            }

            //for every location of the board store the number of pieces of each colour on it as with the actual board locations
            try {
                for (int i = 1; i <= BoardInterface.NUMBER_OF_LOCATIONS; i ++) {
                    for (int j = 1; j <= getBoardLocation(i).numberOfPieces(colour1); j++) {
                        boardClone.getBoardLocation(i).addPieceGetKnocked(colour1);
                    }
                    for (int j = 1; j <= getBoardLocation(i).numberOfPieces(colour2); j++) {
                        boardClone.getBoardLocation(i).addPieceGetKnocked(colour2);
                    }
                }
            }
            catch (NoSuchLocationException e)
            {
                System.out.println("illegal move in set up of locations in location list of clone");
                return null;
            }

            for (int i = 1; i<= getKnockedLocation().numberOfPieces(colour1); i++) {
                boardClone.getKnockedLocation().addPieceGetKnocked(colour1);
            }
            for (int i = 1; i<= getKnockedLocation().numberOfPieces(colour2); i++) {
                boardClone.getKnockedLocation().addPieceGetKnocked(colour2);
            }
            for (int i = 1; i<= getStartLocation().numberOfPieces(colour1); i++) {
                boardClone.getStartLocation().addPieceGetKnocked(colour1);
            }
            for (int i = 1; i<= getStartLocation().numberOfPieces(colour2); i++) {
                boardClone.getStartLocation().addPieceGetKnocked(colour2);
            }
            for (int i = 1; i<= getEndLocation().numberOfPieces(colour1); i++) {
                boardClone.getEndLocation().addPieceGetKnocked(colour1);
            }
            for (int i = 1; i<= getEndLocation().numberOfPieces(colour2); i++) {
                boardClone.getEndLocation().addPieceGetKnocked(colour2);
            }
            return boardClone;
        }
        catch (IllegalMoveException e)
        {
            System.out.println("illegal move in set up of knock/start/end locations in clone");
            return null;
        }
    }

    /**
     * Overrides toString() from Object with a suitable String representation of the board state for displaying via the console to a human
     **/
    public String toString()
    {
        //for every location: representation = locationNumber: colour*numberOfPieces
        StringBuilder boardStateString = new StringBuilder();
        if ((getKnockedLocation().numberOfPieces(colour1) > 0) || (getKnockedLocation().numberOfPieces(colour2) > 0)) {
            if ((getKnockedLocation().numberOfPieces(colour1) > 0) && (getKnockedLocation().numberOfPieces(colour2) > 0)) {
                boardStateString.append("Knock: ").append(colour1).append("*").append(getKnockedLocation().numberOfPieces(colour1)).append(" | ").append(colour2).append("*").append(getKnockedLocation().numberOfPieces(colour2)).append("\n");
            }
            else if (getKnockedLocation().numberOfPieces(colour1) == 0) {
                boardStateString.append("Knock: ").append(colour2).append("*").append(getKnockedLocation().numberOfPieces(colour2)).append("\n");
            }
            else if (getKnockedLocation().numberOfPieces(colour2) == 0) {
                boardStateString.append("Knock: ").append(colour1).append("*").append(getKnockedLocation().numberOfPieces(colour1)).append("\n");
            }
        }
        else {
            boardStateString.append("Knock: \n");
        }
        if ((getStartLocation().numberOfPieces(colour1) > 0) || (getStartLocation().numberOfPieces(colour2) > 0)) {
            if ((getStartLocation().numberOfPieces(colour1) > 0) && (getStartLocation().numberOfPieces(colour2) > 0)) {
                boardStateString.append("Start: ").append(colour1).append("*").append(getStartLocation().numberOfPieces(colour1)).append(" | ").append(colour2).append("*").append(getStartLocation().numberOfPieces(colour2)).append("\n");
            }
            else if (getStartLocation().numberOfPieces(colour1) == 0) {
                boardStateString.append("Start: ").append(colour2).append("*").append(getStartLocation().numberOfPieces(colour2)).append("\n");
            }
            else if (getStartLocation().numberOfPieces(colour2) == 0) {
                boardStateString.append("Start: ").append(colour1).append("*").append(getStartLocation().numberOfPieces(colour1)).append("\n");
            }
        }
        else {
            boardStateString.append("Start: \n");
        }

        int i = 1;
        for (LocationInterface location : locations) {
            if ((location.numberOfPieces(colour1) > 0) || (location.numberOfPieces(colour2) > 0)) {
                if ((location.numberOfPieces(colour1) > 0) && (location.numberOfPieces(colour2) > 0)) {
                    boardStateString.append("Location").append(i).append(": ").append(colour1).append("*").append(location.numberOfPieces(colour1)).append(" | ").append(colour2).append("*").append(location.numberOfPieces(colour2)).append("\n");
                    i += 1;
                }
                else if (location.numberOfPieces(colour1) == 0) {
                    boardStateString.append("Location").append(i).append(": ").append(colour2).append("*").append(location.numberOfPieces(colour2)).append("\n");
                    i += 1;
                }
                else if (location.numberOfPieces(colour2) == 0) {
                    boardStateString.append("Location").append(i).append(": ").append(colour1).append("*").append(location.numberOfPieces(colour1)).append("\n");
                    i += 1;
                }
            }
            else {
                boardStateString.append("Location").append(i).append(": \n");
                i += 1;
            }
        }

        if ((getEndLocation().numberOfPieces(colour1) > 0) || (getEndLocation().numberOfPieces(colour2) > 0)) {
            if ((getEndLocation().numberOfPieces(colour1) > 0) && (getEndLocation().numberOfPieces(colour2) > 0)) {
                boardStateString.append("End: ").append(colour1).append("*").append(getEndLocation().numberOfPieces(colour1)).append(" | ").append(colour2).append("*").append(getEndLocation().numberOfPieces(colour2)).append("\n");
            }
            else if (getEndLocation().numberOfPieces(colour1) == 0) {
                boardStateString.append("End: ").append(colour2).append("*").append(getEndLocation().numberOfPieces(colour2)).append("\n");
            }
            else if (getEndLocation().numberOfPieces(colour2) == 0) {
                boardStateString.append("End: ").append(colour1).append("*").append(getEndLocation().numberOfPieces(colour1)).append("\n");
            }
        }
        else {
            boardStateString.append("End: \n");
        }
        return boardStateString.toString();
    }
}
