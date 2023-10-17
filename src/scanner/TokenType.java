package scanner;

public enum TokenType {
    // Single width tokens
    LEFT_PAREN, RIGHT_PAREN, EQUAL, PLUS, MINUS, SEMICOLON, SLASH,

    // Single or more width tokens
    STAR, DOUBLE_STAR,

    // Literals
    IDENTIFIER, NUMBER,

    // Keywords
    PRINT, LET, MUT,

    EOF
}
