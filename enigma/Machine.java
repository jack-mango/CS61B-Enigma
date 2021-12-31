package enigma;

import static enigma.EnigmaException.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

/** Class that represents a complete enigma machine.
 *  @author Jack Mango
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        if (pawls > numRotors) {
            throw error("Too many pawls given!");
        }
        _alphabet = alpha;
        _numRotors = numRotors;
        _numPawls = pawls;
        _allRotors = new Hashtable<String, Rotor>();
        _slots = new Rotor[numRotors + 1];
        for (Rotor rotor : allRotors) {
            if (_allRotors.contains(rotor)) {
                throw error("Duplicate rotors not allowed!");
            } else {
                _allRotors.put(rotor.name(), rotor);
            }
        }
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (rotors.length > _slots.length) {
            throw error("Too many rotors provided!");
        }
        int numMoving = 0;
        _slots[_slots.length - 1] = new FixedRotor("Plugboard",
                new Permutation("", _alphabet));
        ArrayList<String> dups = new ArrayList<String>();
        for (int i = 0; i < rotors.length; i++) {
            Rotor rotor = _allRotors.get(rotors[i]);
            if (rotor == null) {
                throw error("Rotor doesn't exist!");
            }
            if (rotor.rotates()) {
                numMoving += 1;
            }
            if (numMoving > _numPawls) {
                throw error("Too many moving rotors!");
            } else if (rotor.reflecting() && i != 0) {
                throw error("Only reflectors can be in the first slot!");
            } else if (i > 0 && _slots[i - 1].rotates() && !rotor.rotates()) {
                throw error("Fixed rotors can't go after rotating rotors!");
            } else if (dups.indexOf(rotor.name()) != -1) {
                throw error("Duplicate rotors!");
            } else {
                _slots[i] = _allRotors.get(rotors[i]);
                dups.add(_slots[i].name());
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw error("Invalid settings provided!");
        }
        for (int i = 0; i < setting.length(); i++) {
            if (!_alphabet.contains(setting.charAt(i))) {
                throw error("Setting not in alphabet!");
            }
            _slots[i + 1].set(setting.charAt(i));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _slots[_slots.length - 1] = new FixedRotor("Plugboard", plugboard);
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        int letter = c;
        for (int i = 0; i < _slots.length - 1; i++) {
            if (_slots[i + 1].atNotch()) {
                _slots[i].advance();
                if (i < _slots.length - 3 && _slots[i].rotates()) {
                    _slots[i + 1].advance();
                    i += 1;
                }
            }
        }
        _slots[_slots.length - 2].advance();
        for (int i = _slots.length - 1; i >= 0; i--) {
            letter = _slots[i].convertForward(letter);
        }
        for (int i = 1; i < _slots.length; i++) {
            letter = _slots[i].convertBackward(letter);
        }
        return letter;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String msgOut = "";
        if (_slots[0] == null) {
            throw error("Incomplete setup");
        }
        for (int i = 0; i < msg.length(); i++) {
            char letter = msg.charAt(i);
            if (!_alphabet.contains(letter)) {
                throw error(String.format("Unknown character: %c", letter));
            }
            msgOut += _alphabet.toChar(convert(_alphabet.toInt(letter)));
        }
        return msgOut;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Stores all possible rotors that a Machine might use. */
    private Hashtable<String, Rotor> _allRotors;

    /** Represents the slots of the machine; contains rotors. */
    private Rotor[] _slots;

    /** Number of rotors stored in the machine. */
    private int _numRotors;

    /** Number of pawls in the machine. */
    private int _numPawls;

}
