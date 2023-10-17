package scanner;

import util.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to perform <a href="https://en.wikipedia.org/wiki/Lexical_analysis">Lexical analysis</a>
 */
public class Lexer {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String, TokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("print", TokenType.PRINT);
    }

    public Lexer(String source) {
        this.source = source;
    }

    /**
     * Scan the source code and return a list of tokens.
     * @return A list of tokens.
     *
     * @see Token
     */
    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    /**
     * Scan a single token.
     *
     * @see Token
     */
    private void scanToken() {
        char c = advance();
        switch (c) {
            case ' ', '\r', '\t' -> {} // Ignore whitespaces
            case '\n' -> this.line++;

            // Single width tokens
            case '(' -> addToken(TokenType.LEFT_PAREN);
            case ')' -> addToken(TokenType.RIGHT_PAREN);
            case '=' -> addToken(TokenType.EQUAL);
            case '+' -> addToken(TokenType.PLUS);
            case '-' -> addToken(TokenType.MINUS);
            case ';' -> addToken(TokenType.SEMICOLON);
            case '/' -> addToken(TokenType.SLASH);
//            case '*' -> addToken(TokenType.STAR);

            case '*' -> {
                if (peek() == '*') {
                    advance();
                    addToken(TokenType.DOUBLE_STAR);
                } else {
                    addToken(TokenType.STAR);
                }
            }

            // Comments
            case '#' -> {
                while (peek() != '\n' && !isAtEnd())
                    advance();
            }

            default -> {
                if (isDigit(c)) {
                    addNumber();
                } else if (isAlpha(c)) {
                    addIdentifier();
                } else {
                    Message.error(line, "Unexpected character: " + c);
                    System.exit(69);
                }
            }
        }
    }

    /**
     * Add a token to the list of tokens with `null` as the literal value.
     * @param type The type of the token.
     */
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    /**
     * Add a token to the list of tokens.
     * @param type The type of the token.
     * @param literal The literal value of the token.
     */
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    /**
     * Add a number token to the list of tokens.
     * A number is a string that starts with a digit (0-9),
     * and contains only digits (0-9) and a single period (.).
     * <br /> <br />
     * The period OR dot (.) cannot be in the end of the token,
     * otherwise it won't be included in this token.
     *
     * @see TokenType
     */
    private void addNumber() {
        while (isDigit(peek())) {
            advance();
        }
        if (peek() == '.' && isDigit(peekNext())) {
            advance();
            while (isDigit(peek())) {
                advance();
            }
        }
        addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    /**
     * Add an identifier token to the list of tokens.
     * If the identifier is a keyword, the token type will be the keyword type.
     * Otherwise, the token type will be an identifier.
     * <br /> <br />
     * An identifier is a string that starts with an alpha character (a-z, A-Z, _),
     * and contains only alpha characters and digits (0-9).
     *
     * @see TokenType
     */
    private void addIdentifier() {
        while (isAlpha(peek()) || isDigit(peek())) {
            advance();
        }
        String text = source.substring(start, current);
        TokenType type = keywords.getOrDefault(text, TokenType.IDENTIFIER);
        addToken(type);
    }

    /**
     * Advance the current pointer.
     * @return The character at the current position.
     */
    private char advance() {
        return source.charAt(this.current++);
    }

    /**
     * Return the character at the current position.
     * @return The character at the current position.
     */
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    /**
     * Return the character at the next position.
     * @return The character at the next position.
     */
    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    /**
     * Check if the character is a digit (between 0 and 9).
     * @param c The character to check.
     * @return True if the character is a digit, false otherwise.
     */
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Check if the character is an alpha character (a-z, A-Z, _).
     * @param c The character to check.
     * @return True if the character is an alpha character, false otherwise.
     */
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    /**
     * Check if the current pointer is at the end of the source.
     * @return True if the current pointer is at the end of the source, false otherwise.
     */
    private boolean isAtEnd() {
        return current >= source.length();
    }
}
