package main;

import parser.Expression;
import scanner.Lexer;
import scanner.Token;
import scanner.TokenType;
import util.AstPrinter;
import util.Message;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) Message.error("No source file specified.");

        try {
            byte[] bytes = Files.readAllBytes(Paths.get(args[0]));
            String source = new String(bytes, Charset.defaultCharset());
            run(source);

            // This is an example of something that our future parser will generate (hopefully).
            Expression expression = new Expression.Binary(
                    new Expression.Literal(6),
                    new Token(TokenType.PLUS, "+", null, 1),
                    new Expression.Binary(
                            new Expression.Literal(9),
                            new Token(TokenType.PLUS, "+", null, 1),
                            new Expression.Binary(
                                    new Expression.Literal(6),
                                    new Token(TokenType.STAR, "*", null, 1),
                                    new Expression.Literal(9)
                            )
                    )
            );
            AstPrinter printer = new AstPrinter();
            System.out.println("POSTFIX: " + printer.print(expression));
        } catch (IOException e) {
            Message.error("File `" + args[0] + "` not found.");
        }
    }

    private static void run(String source) {
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        for (Token token : tokens) {
            System.out.println(token);
        }
        // TODO: Parsing and evaluating
    }
}