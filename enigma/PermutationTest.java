package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @JackMango
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    private String cycle1  = "(abcdefghijk)";
    private String cycle2 = "(ABC) (EFG) (IJK) (LMN) (PQR) (TUV) (WXY) (Z)";

    private Alphabet alphabet1 = new Alphabet("abcdefghijk");

    private Permutation perm1 = new Permutation(cycle1, alphabet1);
    private Permutation perm2 = new Permutation(cycle2, new Alphabet());

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void sizeTest() {
        assertEquals(11, perm1.size());
        assertEquals(26, perm2.size());
    }

    @Test
    public void permuteTest() {
        assertEquals('b', perm1.permute('a'));
        assertEquals('a', perm1.permute('k'));
        assertEquals('C', perm2.permute('B'));
        assertEquals('D', perm2.permute('D'));
    }

    @Test
    public void invertTest() {
        assertEquals('a', perm1.invert('b'));
        assertEquals('k', perm1.invert('a'));
        assertEquals('D', perm2.invert('D'));
    }

    @Test
    public void derangementTest() {
        assertTrue(perm1.derangement());
        assertFalse(perm2.derangement());
    }
}
