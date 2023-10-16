package util;

public class Message {
    /**
     * Print an error message including the line number at which it occurred and exit.
     * @param line The line number at which the error occurred.
     * @param message The error message.
     */
    static public void error(int line, String message) {
        System.err.print("[line " + line + "] ");
        error(message);
    }

    /**
     * Print an error message and exit.
     * @param message The error message.
     */
    static public void error(String message) {
        System.err.println("ERROR: " + message);
    }
}
