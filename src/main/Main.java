package main;

import interpreter.Interpreter;
import language.Statement;
import parser.Parser;
import scanner.Lexer;
import scanner.Token;
import util.Message;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            Message.error("No source file specified.");
            System.exit(69);
        }

        try {
            byte[] bytes = Files.readAllBytes(Paths.get(args[0]));
            String source = new String(bytes, Charset.defaultCharset());
            run(source);
        } catch (IOException e) {
            Message.error("File `" + args[0] + "` not found.");
            System.exit(69);
        }
    }

    private static void run(String source) {
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();

        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();

        if (parser.isHadError()) return;

        Interpreter interpreter = new Interpreter();
        interpreter.interpret(statements);

        if (interpreter.isHadError()) {
            System.exit(69);
        }
    }
}