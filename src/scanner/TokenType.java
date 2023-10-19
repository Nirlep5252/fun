package scanner;

public enum TokenType {
    // Single width tokens
    LEFT_PAREN, RIGHT_PAREN, PLUS, MINUS, SEMICOLON, SLASH, LEFT_CURLY, RIGHT_CURLY, COMMA,

    // Single or more width tokens
    STAR, DOUBLE_STAR, EQUAL, DOUBLE_EQUAL, GREATER, GREATER_EQUAL, LESS, LESS_EQUAL,

    // Literals
    IDENTIFIER, NUMBER,

    // Keywords
    PRINT, LET, MUT, NOT, FN, IF, WHILE, ELSE, RETURN, TRUE, FALSE, AND, OR, FOR, FROM, TO, STEP,

    EOF
}
