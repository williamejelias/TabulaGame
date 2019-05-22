package Main;

import Main.BoardInterface;
import Main.GameInterface;

import java.io.IOException;
import java.io.*;
import java.util.*;

/**
 * GameInterface represent the game state including the board, the dice and the players
 *
 * Requires a constructor with no parameters. 
 *
 * Also requires a main method which allows the user to choose the player types and start the game. The main method menu should allow users to: set the players (human or computer); load a game; continue a game; save the game; start a new game; exit the program.
 *
 * If providing a GUI then the same options need to be available through the GUI.
 **/

public class Game implements GameInterface {
    private BoardInterface board;
    private Map<Colour, PlayerInterface> playerMap = new HashMap<Colour, PlayerInterface>();
    private boolean player1HadTurn = false;
    private boolean player2HadTurn = true;

    public static void main(String args[]) throws IOException {
        Scanner in = new Scanner(System.in);
        //while loop
        System.out.println("Welcome to the main menu!");
        System.out.println("To start a new game, type '1'");
        System.out.println("To load a previously saved game, type '2'");
        System.out.println("To exit the program, type '3'");
        boolean selected = false;
        int selector = 0;
        while (!selected) {
            String input = in.next();
            try {
                selector = Integer.parseInt(input);
                selected = true;
            }
            catch (NumberFormatException e) {
                System.out.println("Please enter an integer");
                selected = false;
            }
        }
        boolean mainMenu = true;
        while (mainMenu) {
            if (selector == 1) {
                //New game
                Game game = new Game();
                game.consoleSetBothPlayers();
                try {
                    game.play();
                    mainMenu = true;
                    System.out.println();
                    System.out.println("Welcome to the main menu!");
                    System.out.println("To start a new game, type '1'");
                    System.out.println("To load a previously saved game, type '2'");
                    System.out.println("To exit the program, type '3'");
                    selector = in.nextInt();
                }
                catch (PlayerNotDefinedException e) {
                    System.out.println("An error has occcurred meaning that either/both of the players have not been defined!");
                    System.out.println("Returning to menu!");
                    System.out.println();
                    System.out.println("To start a new game, type '1'");
                    System.out.println("To load a previously saved game, type '2'");
                    System.out.println("To exit the program, type '3'");
                    selector = in.nextInt();
                    mainMenu = true;
                }
            }
            else if (selector == 2) {
                try
                {
                    //Load game
                    GameInterface game = new Game();
                    System.out.println("Type the name of the save file that you want to load - if it is not of the correct format, the game wont load");
                    String loadFile = in.next();
                    game.loadGame(loadFile);
                    game.play();
                    mainMenu = true;
                    System.out.println();
                    System.out.println("Welcome to the main menu!");
                    System.out.println("To start a new game, type '1'");
                    System.out.println("To load a previously saved game, type '2'");
                    System.out.println("To exit the program, type '3'");
                    selector = in.nextInt();
                }
                catch (IOException e) {
                    System.out.println("An error occurred whilst loading the save file!");
                    System.out.println("Returning to menu!");
                    System.out.println();
                    System.out.println("To start a new game, type '1'");
                    System.out.println("To load a previously saved game, type '2'");
                    System.out.println("To exit the program, type '3'");
                    selector = in.nextInt();
                    mainMenu = true;
                }
                catch (PlayerNotDefinedException e) {
                    System.out.println("Players weren't defined whilst loading the save file!");
                    System.out.println("Returning to menu!");
                    System.out.println();
                    System.out.println("To start a new game, type '1'");
                    System.out.println("To load a previously saved game, type '2'");
                    System.out.println("To exit the program, type '3'");
                    selector = in.nextInt();
                    mainMenu = true;
                }
            }
            else if (selector == 3) {
                mainMenu = false;
            }
            else {
                System.out.println("That is not an option!");
                System.out.println("To start a new game, type '1'");
                System.out.println("To load a previously saved game, type '2'");
                System.out.println("To exit the program, type '3'");
                selected = false;
                selector = 0;
                while (!selected) {
                    String input = in.next();
                    try {
                        selector = Integer.parseInt(input);
                        selected = true;
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Please enter an integer");
                        selected = false;
                    }
                }
                mainMenu = true;
            }
        }
    }

    public Game() {
        board = new Board();
        //depending on input, create two players and assign inputted paramaters to their constructors if required
        //the view/state of the board is the same for each player
        //clone the board
        //roll dice
        //view possible moves, no repetition (if no moves possible other player takes their turn)
        //make move  
        //check if clone board is valid - if it is, board gets this, else, ask for new moves
        //check if winner
        //(repeat above steps until no dice values left)
        //other player has their turn
    }

    /**
     * @param colour of the player to set
     *
     * @param player the player to use
     **/
    public void setPlayer(Colour colour, PlayerInterface player)
    {
        playerMap.put(colour, player);
    }

    public void consoleSetBothPlayers() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the PlayerType (HumanConsolePlayer, ComputerPlayer, or HumanGUIPlayer) of Player1 (Player 1 uses " + Colour.values()[0] + " pieces)");
        String playerType = in.nextLine();
        boolean player1Set = false;
        while (!player1Set)
        {
            switch (playerType) {
                case "HumanConsolePlayer": {
                    PlayerInterface firstPlayer = new HumanConsolePlayer();
                    player1Set = true;
                    setPlayer(Colour.values()[0], firstPlayer);
                    break;
                }
                case "ComputerPlayer": {
                    PlayerInterface firstPlayer = new ComputerPlayer();
                    player1Set = true;
                    setPlayer(Colour.values()[0], firstPlayer);
                    break;
                }
                case "HumanGUIPlayer": {
                    PlayerInterface firstPlayer = new HumanGUIPlayer();
                    player1Set = true;
                    setPlayer(Colour.values()[0], firstPlayer);
                    break;
                }
                default:
                    System.out.println("That is not a type of player! Please enter a valid player type.");
                    playerType = in.nextLine();
                    player1Set = false;
                    break;
            }
        }

        System.out.println("Enter the PlayerType of Player2 (Player 2 uses " + Colour.values()[1] + " pieces)");
        playerType = in.nextLine();
        boolean player2Set = false;
        while (!player2Set)
        {
            switch (playerType) {
                case "HumanConsolePlayer": {
                    PlayerInterface secondPlayer = new HumanConsolePlayer();
                    player2Set = true;
                    setPlayer(Colour.values()[1], secondPlayer);
                    break;
                }
                case "ComputerPlayer": {
                    PlayerInterface secondPlayer = new ComputerPlayer();
                    player2Set = true;
                    setPlayer(Colour.values()[1], secondPlayer);
                    break;
                }
                case "HumanGUIPlayer": {
                    PlayerInterface secondPlayer = new HumanGUIPlayer();
                    player2Set = true;
                    setPlayer(Colour.values()[1], secondPlayer);
                    break;
                }
                default:
                    System.out.println("That is not a type of player! Please enter a valid player type.");
                    playerType = in.nextLine();
                    player2Set = false;
                    break;
            }
        }
    }

    /**
     * @return the player who has the next turn. Green goes first.
     **/
    public Colour getCurrentPlayer() {
        if (player1HadTurn && !player2HadTurn) {
            return Colour.values()[1];
        }
        else if (!player1HadTurn && player2HadTurn) {
            return Colour.values()[0];
        }
        else {
            //SHOULD NOT HAPPEN
            return null;
        }
    }

    /**
     * Play the game until completion or pause. Should work either for a new game or the continuation of a paused game. This method should roll the dice and pass the dice values to the players. The players should be asked one after another for their choice of turn via their getTurn method. The board that is passed to the players should be a clone of the game board so that they can try out moves without affecting the state of the game.
     *
     * @return the colour of the winner if there is one, or null if not (the game has been paused by a player). If a player tries to take an illegal turn then they forfeit the game and the other player immediately wins.
     *
     * @throws PlayerNotDefinedException if one or both of the players is undefined
     **/
    public Colour play() throws PlayerNotDefinedException {
        //the view/state of the board is the same for each player
        //clone the board
        //roll dice
        //view possible moves, no repetition (if no moves possible other player takes their turn)
        //make move  
        //check if winner
        //(repeat above steps until no dice values left)
        //other player has their turn
        if (playerMap.size() != 2) {
            throw new PlayerNotDefinedException("One or both of the players are not defined!");
        }
        else {
            DiceInterface dice = new Dice();
            BoardInterface boardClone;
            //play game
            try {
                if (board.winner() == null) {
                    while (board.winner() == null) {
                        if (playerMap.get(getCurrentPlayer()) instanceof HumanConsolePlayer) {
                            //routine for a human console player
                            System.out.println(getCurrentPlayer() + " turn: \n");
                            System.out.println(board.toString());
                            dice.roll();
                            System.out.println("Dice values: " + dice.getValues());
                        }
                        if (playerMap.get(getCurrentPlayer()) instanceof ComputerPlayer) {
                            //routine for a computer player
                            System.out.println("COMPUTER TAKING TURN...");
                            dice.roll();
                        }
                        if (playerMap.get(getCurrentPlayer()) instanceof HumanGUIPlayer) {
                            //routine for a human GUI player
                            dice.roll();
                        }

                        boardClone = board.clone();
                        try
                        {
                            TurnInterface playerTurn = playerMap.get(getCurrentPlayer()).getTurn(getCurrentPlayer(), boardClone, dice.getValues());
                            board.takeTurn(getCurrentPlayer(), playerTurn, dice.getValues());
                            if (getCurrentPlayer() == Colour.values()[0])
                            {
                                player1HadTurn = true;
                                player2HadTurn = false;
                            }
                            else
                            {
                                player1HadTurn = false;
                                player2HadTurn = true;
                            }

                            if (board.isWinner(getCurrentPlayer()))
                            {
                                //automatically exits loop as a winner is now 'true'
                            }
                        }
                        catch (NotRolledYetException e)
                        {
                            //should not happen
                            System.out.println("Not rolled yet exception thrown!");
                            return null;
                        }
                        catch (IllegalTurnException e)
                        {
                            //should not happen
                            System.out.println("Illegal turn exception thrown !");
                            return null;
                        }
                    }
                }
                System.out.println(board.toString());
                System.out.println();
                System.out.println("Congratulations, the winner is " + board.winner() + "!");
                return board.winner();
            }
            catch (PauseException e)
            {
                try
                {
                    Scanner in = new Scanner(System.in);
                    System.out.println("Enter the name of the save file - the system will add '.txt' to your input");
                    String filename = in.next();
                    filename = filename + ".txt";
                    saveGame(filename);
                    return null;
                }
                catch (IOException f)
                {
                    return null;
                }
            }
            catch (NotRolledYetException e)
            {
                //should not happen
                return null;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Save the current state of the game (including the board, dice and player types) into a file so it can be re-loaded and game play continued. You choose what the format of the file is.
     *
     * @param filename the name of the file in which to save the game state
     *
     * @throws IOException when an I/O problem occurs while saving
     **/
    public void saveGame(String filename) throws IOException {
        String typeP1 = "error";
        if (playerMap.get(Colour.values()[0]) instanceof HumanConsolePlayer) {
            typeP1 = "HumanConsolePlayer";
        }
        else if (playerMap.get(Colour.values()[0]) instanceof ComputerPlayer) {
            typeP1 = "ComputerPlayer";
        }
        else if (playerMap.get(Colour.values()[0]) instanceof HumanGUIPlayer) {
            typeP1 = "HumanGUIPlayer";
        }

        String typeP2 = "error";
        if (playerMap.get(Colour.values()[1]) instanceof HumanConsolePlayer) {
            typeP2 = "HumanConsolePlayer";
        }
        else if (playerMap.get(Colour.values()[1]) instanceof ComputerPlayer) {
            typeP2 = "ComputerPlayer";
        }
        else if (playerMap.get(Colour.values()[1]) instanceof HumanGUIPlayer) {
            typeP2 = "HumanGUIPlayer";
        }
        //in the board object, write each location with the number of each colour to a new line in the save file
        //save each player and their colour, and the type of player
        try {
            PrintWriter pw = new PrintWriter(filename);
            //write each line of board.toString() to a new line in the file 
            //close the printwriter and save the file
            pw.println("Player1|" + Colour.values()[0] + "|" + typeP1);
            pw.println("Player2|" + Colour.values()[1] + "|" + typeP2);
            pw.println("CurrentPlayer|" + getCurrentPlayer());
            //for every location: representation = locationNumber: colour*numberOfPieces
            StringBuilder boardStateString = new StringBuilder();
            boardStateString.append("Knock|").append(Colour.values()[0]).append("*").append(board.getKnockedLocation().numberOfPieces(Colour.values()[0])).append("|").append(Colour.values()[1]).append("*").append(board.getKnockedLocation().numberOfPieces(Colour.values()[1])).append("\n");
            boardStateString.append("Start|").append(Colour.values()[0]).append("*").append(board.getStartLocation().numberOfPieces(Colour.values()[0])).append("|").append(Colour.values()[1]).append("*").append(board.getStartLocation().numberOfPieces(Colour.values()[1])).append("\n");
            int i = 1;
            for (LocationInterface location : ((Board)board).getLocationList()) {
                boardStateString.append("Location").append(i).append("|").append(Colour.values()[0]).append("*").append(location.numberOfPieces(Colour.values()[0])).append("|").append(Colour.values()[1]).append("*").append(location.numberOfPieces(Colour.values()[1])).append("\n");
                i += 1;
            }
            boardStateString.append("End|").append(Colour.values()[0]).append("*").append(board.getEndLocation().numberOfPieces(Colour.values()[0])).append("|").append(Colour.values()[1]).append("*").append(board.getEndLocation().numberOfPieces(Colour.values()[1]));

            pw.println(boardStateString);
            pw.close();
        }
        catch (IOException e) {
            System.out.println("An error occurred whilst writing the save file!");
        }
    }

    /**
     * Load the game state from the given file
     *
     * @param filename  the name of the file from which to load the game state
     *
     * @throws IOException when an I/O problem occurs or the file is not in the correct format (as used by saveGame())
     **/
    public void loadGame(String filename) throws IOException {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            //read each player, their colour and their type, and set accordingly
            //for each line in the saved game file, write the associated location, and number of each colour to the associated variables in the location class
            String player1Line = br.readLine();
            String[] player1Parts = player1Line.split("\\|");
            String player1Type = player1Parts[2];
            switch (player1Type) {
                case "HumanConsolePlayer": {
                    PlayerInterface firstPlayer = new HumanConsolePlayer();
                    setPlayer(Colour.values()[0], firstPlayer);
                    break;
                }
                case "ComputerPlayer": {
                    PlayerInterface firstPlayer = new ComputerPlayer();
                    setPlayer(Colour.values()[0], firstPlayer);
                    break;
                }
                case "HumanGUIPlayer": {
                    PlayerInterface firstPlayer = new HumanGUIPlayer();
                    setPlayer(Colour.values()[0], firstPlayer);
                    break;
                }
            }

            String player2Line = br.readLine();
            String[] player2Parts = player2Line.split("\\|");
            String player2Type = player2Parts[2];
            switch (player2Type) {
                case "HumanConsolePlayer": {
                    PlayerInterface secondPlayer = new HumanConsolePlayer();
                    setPlayer(Colour.values()[1], secondPlayer);
                    break;
                }
                case "ComputerPlayer": {
                    PlayerInterface secondPlayer = new ComputerPlayer();
                    setPlayer(Colour.values()[1], secondPlayer);
                    break;
                }
                case "HumanGUIPlayer": {
                    PlayerInterface secondPlayer = new HumanGUIPlayer();
                    setPlayer(Colour.values()[1], secondPlayer);
                    break;
                }
            }

            String currentPlayerLine = br.readLine();
            String[] currentPlayerParts = currentPlayerLine.split("\\|");
            String currentPlayer = currentPlayerParts[1];
            String colourPlayer1 = "" + Colour.values()[0];
            String colourPlayer2 = "" + Colour.values()[1];
            if (currentPlayer.equals(colourPlayer1)) {
                player1HadTurn = false;
                player2HadTurn = true;
            }
            else if (currentPlayer.equals(colourPlayer2)) {
                player1HadTurn = true;
                player2HadTurn = false;
            }
            else {
                //should not happen
                //assume that colour of current player is either one of the colours in the player colours field in Game Interface
            }

            try {
                //remove pieces from start location, as a new board needs to be initialised with saved values
                for (int i = 1; i <= BoardInterface.PIECES_PER_PLAYER; i++) {
                    board.getStartLocation().removePiece(Colour.values()[0]); }
                for (int i = 1; i <= BoardInterface.PIECES_PER_PLAYER; i++) {
                    board.getStartLocation().removePiece(Colour.values()[1]);
                }

                //set knock location up
                String knockLine = br.readLine();
                String[] knockLineParts = knockLine.split("\\|");
                String[] knockCol1 = knockLineParts[1].split("\\*");
                String[] knockCol2 = knockLineParts[2].split("\\*");
                int knockNumOfCol1 = Integer.parseInt(knockCol1[1]);
                int knockNumOfCol2 = Integer.parseInt(knockCol2[1]);
                for (int i = 1; i <= knockNumOfCol1; i ++) {
                    board.getKnockedLocation().addPieceGetKnocked(Colour.values()[0]);
                }
                for (int i = 1; i <= knockNumOfCol2; i ++) {
                    board.getKnockedLocation().addPieceGetKnocked(Colour.values()[1]);
                }

                //set start location up
                String startLine = br.readLine();
                String[] startLineParts = startLine.split("\\|");
                String[] startCol1 = startLineParts[1].split("\\*");
                String[] startCol2 = startLineParts[2].split("\\*");
                int startNumOfCol1 = Integer.parseInt(startCol1[1]);
                int startNumOfCol2 = Integer.parseInt(startCol2[1]);
                for (int i = 1; i <= startNumOfCol1; i ++) {
                    board.getStartLocation().addPieceGetKnocked(Colour.values()[0]);
                }
                for (int i = 1; i <= startNumOfCol2; i ++) {
                    board.getStartLocation().addPieceGetKnocked(Colour.values()[1]);
                }

                //set up on board locations
                int n = 1;
                String line;
                while (n <= BoardInterface.NUMBER_OF_LOCATIONS) {
                    String locationLine = br.readLine();
                    String[] locationLineParts = locationLine.split("\\|");
                    String[] locationCol1 = locationLineParts[1].split("\\*");
                    String[] locationCol2 = locationLineParts[2].split("\\*");
                    int locationNumOfCol1 = Integer.parseInt(locationCol1[1]);
                    int locationNumOfCol2 = Integer.parseInt(locationCol2[1]);
                    for (LocationInterface location : ((Board)board).getLocationList()) {
                        if (location.getName().equals(locationLineParts[0])) {
                            for (int i = 1; i <= locationNumOfCol1; i ++) {
                                location.addPieceGetKnocked(Colour.values()[0]);
                            }
                            for (int i = 1; i <= locationNumOfCol2; i ++) {
                                location.addPieceGetKnocked(Colour.values()[1]);
                            }
                            n += 1;
                        }
                    }
                }

                String endLine = br.readLine();
                String[] endLineParts = endLine.split("\\|");
                String[] endCol1 = endLineParts[1].split("\\*");
                String[] endCol2 = endLineParts[2].split("\\*");
                int endNumOfCol1 = Integer.parseInt(endCol1[1]);
                int endNumOfCol2 = Integer.parseInt(endCol2[1]);
                for (int i = 1; i <= endNumOfCol1; i ++) {
                    board.getEndLocation().addPieceGetKnocked(Colour.values()[0]);
                }
                for (int i = 1; i <= endNumOfCol2; i ++) {
                    board.getEndLocation().addPieceGetKnocked(Colour.values()[1]);
                }
            }
            catch (IllegalMoveException e) {
                System.out.println("Error in setting up board from save file!");
            }
        }
        catch (IOException e) {
            System.out.println("Unable to find/open the file!");
            throw new IOException();
        }
        catch (NullPointerException e) {
            System.out.println("Saved file is not in the correct format to be loaded!");
            //does nothing
        }
    }
}
