package enigma;

import java.util.ArrayList;
import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Jack Mango
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        String dubs = "";
        while (cycles.length() > 0) {
            int start = cycles.indexOf('(');
            int end = cycles.indexOf(')') + 1;
            String cycle = cycles.substring(start, end);
            for (int i = 1; i < cycle.length() - 1; i++) {
                char letter = cycle.charAt(i);
                if (dubs.contains(String.valueOf(letter))) {
                    throw error("Two cycles contain the same character!");
                } else if (!_alphabet.contains(letter)) {
                    throw error("Character in cycle is not in the alphabet!");
                } else {
                    dubs = dubs + letter;
                }
            }
            addCycle(cycle);
            cycles = cycles.replace(cycle, "").trim();
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cycles.add(cycle.substring(1, cycle.length() - 1));
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Return the value of P modulo the size of a given CYCLE. */
    final int wrapCycle(String cycle, int p) {
        int r = p % cycle.length();
        if (r < 0) {
            r += cycle.length();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        for (String cycle : _cycles) {
            int index = cycle.indexOf(_alphabet.toChar(p));
            if (index != -1) {
                char found = cycle.charAt(wrapCycle(cycle, index + 1));
                return _alphabet.toInt(found);
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation
     *  to C modulo the alphabet size. */
    int invert(int c) {
        for (String cycle : _cycles) {
            int index = cycle.indexOf(_alphabet.toChar(c));
            if (index != -1) {
                char found = cycle.charAt(wrapCycle(cycle, index - 1));
                return _alphabet.toInt(found);
            }
        }
        return c;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return _alphabet.toChar(permute(_alphabet.toInt(p)));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        return _alphabet.toChar(invert(_alphabet.toInt(c)));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int tot = 0;
        for (String cycle : _cycles) {
            if (cycle.length() == 1) {
                return false;
            }
            tot += cycle.length();
        }
        return tot == _alphabet.size();
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycles of this permutation. */
    private ArrayList<String> _cycles = new ArrayList<String>();

}
