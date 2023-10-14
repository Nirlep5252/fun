import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) Utils.error("No source file specified.");

        try {
            byte[] bytes = Files.readAllBytes(Paths.get(args[0]));
            String source = new String(bytes, Charset.defaultCharset());
            run(source);
        } catch (IOException e) {
            Utils.error("File `" + args[0] + "` not found.");
        }
    }

    private static void run(String source) {
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}