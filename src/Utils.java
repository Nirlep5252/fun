public class Utils {
    static public void error(int line, String message) {
        System.err.print("[line " + line + "] ");
        error(message);
    }

    static public void error(String message) {
        System.err.println("ERROR: " + message);
        System.exit(1);
    }
}
