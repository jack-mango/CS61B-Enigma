package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.regex.Pattern;

import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Jack Mango
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine mach = readConfig();
        while (_input.hasNext()) {
            String next = _input.nextLine();
            if (next.isBlank()) {
                _output.println();
            } else if (next.charAt(0) == '*') {
                setUp(mach, next);
            } else {
                printMessageLine(mach.convert(next.replaceAll("\\s+", "")));
            }
        }
        while (_input.hasNextLine()) {
            _output.println();
            _input.nextLine();
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.nextLine());
            int numRotors = Integer.parseInt(_config.next("\\d+"));
            int numPawls = Integer.parseInt(_config.next("\\d+"));
            ArrayList<Rotor> rotors = new ArrayList<>();
            _config.nextLine();
            while (_config.hasNext()) {
                rotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, numPawls, rotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String type = _config.next();
            String cycles = "";
            while (_config.hasNext("(\\([\\w\\.]*\\))+")) {
                cycles += _config.next();
            }
            if (type.charAt(0) == 'M') {
                String notches = "";
                for (int i = 1; i < type.length(); i++) {
                    notches = notches + type.charAt(i);
                }
                return new MovingRotor(name,
                        new Permutation(cycles, _alphabet), notches);
            } else if (type.charAt(0) == 'N') {
                return new FixedRotor(name,
                        new Permutation(cycles, _alphabet));
            } else if (type.charAt(0) == 'R') {
                return new Reflector(name,
                        new Permutation(cycles, _alphabet));
            } else {
                throw error("bad rotor description");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        Pattern p = Pattern.compile(" ");
        String[] s = p.split(settings);
        String[] rotors = new String[M.numRotors()];
        for (int i = 1; i <= M.numRotors(); i++) {
            rotors[i - 1] = s[i];
        }
        M.insertRotors(rotors);
        M.setRotors(s[M.numRotors() + 1]);
        String cycles = "";
        for (int i = M.numRotors() + 2; i < s.length; i++) {
            cycles = cycles + s[i];
        }
        M.setPlugboard(new Permutation(cycles, _alphabet));
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String formattedMsg = "";
        for (int i = 0, j = 0; i < msg.length(); i++) {
            if (j == 5) {
                formattedMsg = formattedMsg + " ";
                j = 0;
            }
            formattedMsg = formattedMsg + msg.charAt(i);
            j += 1;
        }
        _output.println(formattedMsg);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
