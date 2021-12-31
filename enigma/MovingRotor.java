package enigma;



/** Class that represents a rotating rotor in the enigma machine.
 *  @author Jack Mango
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = new int[notches.length()];
        for (int i = 0; i < notches.length(); i++) {
            _notches[i] = perm.alphabet().toInt(notches.charAt(i));
        }
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        for (int i = 0; i < _notches.length; i++) {
            if (_notches[i] == setting()) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        set(setting() + 1);
    }

    /** Stores my notches. */
    private int[] _notches;

}
