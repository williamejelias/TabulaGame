package Main;

import static org.junit.Assert.*;

import Main.BoardInterface;
import Main.LocationInterface;
import Main.MoveInterface;
import Main.TurnInterface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.util.*;

public class BoardTestBasic 
{

    private BoardInterface b;
    private LocationInterface s, e, k, l1, l2;
    private MoveInterface m, n, a, f, c, d;
    private TurnInterface t;
    private Colour G,B;

    public BoardTestBasic()
    {

    }

    //@Rule
    //public Timeout globalTimeout = Timeout.seconds(1); // 1 seconds max per method tested

    @Before
    public void setUp()
    {
        b = new Board();
        s = b.getStartLocation();
        e = b.getEndLocation();
        k = b.getKnockedLocation();
        G = Colour.GREEN;
        B = Colour.BLUE;
        m = new Move();
        n = new Move();
        t = new Turn();
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void constructor_initialises_start()
    {
        assertEquals(15, s.numberOfPieces(B));
        assertEquals(15, s.numberOfPieces(G));
    }

    @Test
    public void constructor_initialises_empty_elsewhere() throws Exception
    {
        assertEquals(0, e.numberOfPieces(B));
        assertEquals(0, e.numberOfPieces(G));
        assertEquals(0, k.numberOfPieces(B));
        assertEquals(0, k.numberOfPieces(G));
        for(int i=1; i<=24; i++)
        {
            assertEquals(0, b.getBoardLocation(i).numberOfPieces(G));
            assertEquals(0, b.getBoardLocation(i).numberOfPieces(B));
        }
    }

    @Test
    public void cloned_board_is_different() throws Exception
    {
        BoardInterface clone = b.clone();
        assertEquals(clone.getBoardLocation(1).numberOfPieces(G), 0);
        assertEquals(b.getBoardLocation(1).numberOfPieces(G), 0);
        clone.getBoardLocation(1).addPieceGetKnocked(G);
        assertEquals(clone.getBoardLocation(1).numberOfPieces(G), 1);
        assertEquals(b.getBoardLocation(1).numberOfPieces(G), 0);
    }

    @Test
    public void initial_move_from_start() throws Exception 
    {
        m.setSourceLocation(0);
        m.setDiceValue(5);
        assert(b.canMakeMove(G, m));
    }

    @Test(expected=IllegalMoveException.class)
    public void no_initial_move_elsewhere() throws Exception 
    {
        m.setSourceLocation(1);
        m.setDiceValue(5);
        b.makeMove(B, m);
    }

    @Test
    public void turn_initial() throws Exception 
    {
        m.setSourceLocation(0);
        m.setDiceValue(5);
        n.setSourceLocation(5);
        n.setDiceValue(4);
        t.addMove(m);
        t.addMove(n);
        List<Integer> dice = new ArrayList<>();
        dice.add(4);
        dice.add(5);
        b.takeTurn(G, t, dice);
        assertEquals(b.getBoardLocation(9).numberOfPieces(G), 1);
    }

    @Test
    public void knock_after_initial() throws Exception 
    {
        m.setSourceLocation(0);
        m.setDiceValue(5);
        b.makeMove(G,m);
        b.makeMove(B,m);
        assertEquals(k.numberOfPieces(G), 1);
        assertEquals(b.getBoardLocation(5).numberOfPieces(G), 0);
        assertEquals(b.getBoardLocation(5).numberOfPieces(B), 1);
    }

    @Test
    public void piece_can_finish() throws Exception 
    {
        l1 = b.getBoardLocation(23);
        l1.addPieceGetKnocked(G);
        m.setSourceLocation(23);
        m.setDiceValue(4);
        b.makeMove(G,m);
        assertEquals(e.numberOfPieces(G), 1);
    }

    @Test
    public void can_win() throws Exception 
    {
        for(int i = 0; i<15; i++)
        {
            e.addPieceGetKnocked(G);
        }
        assert(b.isWinner(G));
        assertEquals(b.winner(), G);
    }

    @Test
    public void test_initial_is_valid()
    {
        assertEquals(b.isValid(), true);
    }

    /*
    @Test
    public void test_to_string()
    {
        String string = "Knock: GREEN*0 | BLUE*0\nStart: GREEN*15 | BLUE*15\n";
        string += "Location1: GREEN*0 | BLUE*0\n";
        string += "Location2: GREEN*0 | BLUE*0\n";
        string += "Location3: GREEN*0 | BLUE*0\n";
        string += "Location4: GREEN*0 | BLUE*0\n";
        string += "Location5: GREEN*0 | BLUE*0\n";
        string += "Location6: GREEN*0 | BLUE*0\n";
        string += "Location7: GREEN*0 | BLUE*0\n";
        string += "Location8: GREEN*0 | BLUE*0\n";
        string += "Location9: GREEN*0 | BLUE*0\n";
        string += "Location10: GREEN*0 | BLUE*0\n";
        string += "Location11: GREEN*0 | BLUE*0\n";
        string += "Location12: GREEN*0 | BLUE*0\n";
        string += "Location13: GREEN*0 | BLUE*0\n";
        string += "Location14: GREEN*0 | BLUE*0\n";
        string += "Location15: GREEN*0 | BLUE*0\n";
        string += "Location16: GREEN*0 | BLUE*0\n";
        string += "Location17: GREEN*0 | BLUE*0\n";
        string += "Location18: GREEN*0 | BLUE*0\n";
        string += "Location19: GREEN*0 | BLUE*0\n";
        string += "Location20: GREEN*0 | BLUE*0\n";
        string += "Location21: GREEN*0 | BLUE*0\n";
        string += "Location22: GREEN*0 | BLUE*0\n";
        string += "Location23: GREEN*0 | BLUE*0\n";
        string += "Location24: GREEN*0 | BLUE*0\n";

        System.out.println(b.toString());
        System.out.println();
        System.out.println(string);

        assertEquals(b.toString(), string);
    }
    */

    @Test
    public void test_number_of_locations_produced()
    {
        assertEquals(((Board)b).getLocationList().size(), BoardInterface.NUMBER_OF_LOCATIONS);
    }
    
    @Test
    public void test_possible_moves() throws Exception
    {
        m.setSourceLocation(0);
        m.setDiceValue(5);
        n.setSourceLocation(5);
        n.setDiceValue(4);
        t.addMove(m);
        t.addMove(n);
        List<Integer> dice = new ArrayList<>();
        dice.add(4);
        dice.add(5);
        b.takeTurn(G, t, dice);
        dice.remove((Integer)4);
        dice.remove((Integer)5);
        dice.add(3);
        dice.add(2);
        Move a = new Move();
        Move f = new Move();
        Move c = new Move();
        Move d = new Move();
        a.setSourceLocation(0);
        a.setDiceValue(3);
        f.setSourceLocation(0);
        f.setDiceValue(2);
        c.setSourceLocation(9);
        c.setDiceValue(3);
        d.setSourceLocation(9);
        d.setDiceValue(2);
        Set<MoveInterface> testSet = new HashSet<MoveInterface>();
        testSet.add(a);
        testSet.add(f);
        testSet.add(c);
        testSet.add(d);
        assertEquals(b.possibleMoves(G, dice), testSet); 
    }
}
