package enigma;

import org.junit.Test;
import static org.junit.Assert.*;

public class AlphabetTest {

    private Alphabet alphabet1 = new Alphabet();
    private Alphabet alphabet2 = new Alphabet("abcd");
    private Alphabet alphabet3 = new Alphabet("xyabwvcd");
    private Alphabet alphabet4 = new Alphabet("");

    @Test
    public void sizeTest() {
        assertEquals(26, alphabet1.size());
        assertEquals(4, alphabet2.size());
        assertEquals(8, alphabet3.size());
        assertEquals(0, alphabet4.size());
    }

    @Test
    public void containsTest() {
        assertTrue(alphabet1.contains('A'));
        assertTrue(alphabet2.contains('b'));
        assertFalse(alphabet3.contains('m'));
        assertFalse(alphabet4.contains('a'));
    }

    @Test
    public void toCharTest() {
        assertEquals('a', alphabet2.toChar(0));
        assertEquals('d', alphabet3.toChar(7));
        assertEquals('Z', alphabet1.toChar(25));
    }

    @Test
    public void toIntTest() {
        assertEquals(0, alphabet1.toInt('A'));
        assertEquals(7, alphabet3.toInt('d'));
    }



}
