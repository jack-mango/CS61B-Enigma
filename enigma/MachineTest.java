package enigma;

import org.junit.Test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MachineTest {
    private Alphabet alphabet = new Alphabet("ABCDEFG");

    private MovingRotor rotor1 = new MovingRotor("rotor1",
            new Permutation("(ABC) (DEF)", alphabet), "C");
    private MovingRotor rotor2 = new MovingRotor("rotor2",
            new Permutation("(AC) (BDEF)", alphabet), "A");
    private FixedRotor rotor3 = new FixedRotor("rotor3",
            new Permutation("(ACDF) (BG)", alphabet));
    private Reflector rotor4 = new Reflector("rotor4",
            new Permutation("(ABFGECD)", alphabet));
    private ArrayList<Rotor> rotors = new ArrayList<Rotor>(
            Arrays.asList(new Rotor[]{rotor1, rotor2, rotor3, rotor4}));

    private Machine mach1 = new Machine(alphabet, 1, 0, rotors);
    private Machine mach2 = new Machine(alphabet, 2, 1, rotors);
    private Machine mach3 = new Machine(alphabet, 3, 1, rotors);
    private Machine mach4 = new Machine(alphabet, 4, 2, rotors);

    private String[] rlst;

    @Test
    public void numRotorsTest() {
        assertEquals(1, mach1.numRotors());
        assertEquals(2, mach2.numRotors());
        assertEquals(3, mach3.numRotors());
    }

    @Test
    public void numPawlsTest() {
        assertEquals(0, mach1.numPawls());
        assertEquals(1, mach2.numPawls());
        assertEquals(1, mach3.numPawls());
    }

    @Test
    public void simpleConvertTest() {
        mach1.insertRotors(new String[] {"rotor4"});
        mach1.setRotors("");
        assertEquals("B", mach1.convert("A"));
        assertEquals("AC", mach1.convert("DE"));
    }

    @Test
    public void intermediateConvertTest() {
        mach2.insertRotors(new String[]{"rotor4", "rotor2"});
        mach2.setRotors("A");
        assertEquals("CDDCGDCD", mach2.convert("AABBAABB"));
        mach2.setRotors("E");
        assertEquals("DDAEDAB", mach2.convert("BADCAFE"));
    }

    @Test
    public void advancedConvertTest() {
        mach3.insertRotors(new String[] {"rotor4", "rotor3", "rotor2"});
        mach3.setRotors("AA");
        assertEquals("CDADBBG", mach3.convert("BADCAFE"));
        mach3.setRotors("GB");
        assertEquals("EDDCEDGA", mach3.convert("AGEDFEED"));
        mach3.setRotors("DC");
        assertEquals("FAAFDFGCFFBGEACFFFAGBDE",
                mach3.convert("BEBEBEDFADDEAFFADEDBEAD"));
    }

    @Test
    public void supermegaConvertTest() {
        rlst[0] = "rotor4";
        rlst[1] = "rotor3";
        rlst[2] = "rotor2";
        rlst[3] = "rotor1";
        mach4.insertRotors(rlst);
        mach4.setRotors("AAC");
        assertEquals("FCGGBAG", mach4.convert("BADCAFE"));
        mach4.setRotors("GBC");
        assertEquals("DDACCFBE", mach4.convert("AGEDFEED"));
        mach4.setRotors("DCC");
        assertEquals("GCABDDADCGBAECDGFDGCFBE",
                mach4.convert("BEBEBEDFADDEAFFADEDBEAD"));
    }
}
