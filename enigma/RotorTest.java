package enigma;

import org.junit.Test;
import static org.junit.Assert.*;

public class RotorTest {

    private Rotor rotor1 = new Rotor("rotor1",
            new Permutation("(ab) (cd)", new Alphabet("abcde")));
    private Rotor rotor2 = new Rotor("rotor2",
            new Permutation("(ab) (cd) (hi)", new Alphabet("abcdefghi")));

    @Test
    public void setTest() {
        assertEquals(0, rotor1.setting());
        rotor1.set(1);
        assertEquals(1, rotor1.setting());
        rotor1.set('e');
        assertEquals(4, rotor1.setting());
        rotor1.set(rotor1.setting() + 1);
        assertEquals(0, rotor1.setting());
    }

    @Test
    public void convertTest() {
        assertEquals(1, rotor2.convertForward(0));
        assertEquals(1, rotor2.convertBackward(0));
        assertEquals(5, rotor2.convertBackward(5));
        assertEquals(5, rotor2.convertForward(5));
        rotor2.set(1);
        assertEquals(2, rotor2.convertForward(1));
        assertEquals(4, rotor2.convertBackward(4));
        rotor2.set(0);
        assertEquals(0, rotor2.convertForward(1));
        assertEquals(0, rotor2.convertBackward(1));
    }

    @Test
    public void rotorTest() {
        String cycles = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
        Rotor rotor3 = new Rotor("rotor",
                new Permutation(cycles, new Alphabet()));
        rotor3.set('F');
        assertEquals(8, rotor3.convertForward(5));
    }
}
