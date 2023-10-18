package scanner;

public enum TokenType {
    // Single width tokens
    LEFT_PAREN, RIGHT_PAREN, EQUAL, PLUS, MINUS, SEMICOLON, SLASH, LEFT_CURLY, RIGHT_CURLY, COMMA,

    // Single or more width tokens
    STAR, DOUBLE_STAR,

    // Literals
    IDENTIFIER, NUMBER,

    // Keywords
    PRINT, LET, MUT, NOT, FN, FOR, IF, WHILE, ELSE, RETURN, TRUE, FALSE, AND, OR,

    EOF
}
