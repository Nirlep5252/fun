package parser;

import scanner.Token;
import scanner.TokenType;

import java.util.List;

import util.Message;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;
    private boolean hadError = false;

    private static class ParserError extends RuntimeException {};

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Expression parse() {
        try {
            return expression();
        } catch (ParserError e) {
            this.hadError = true;
            return null;
        }
    }

    public boolean isHadError() {
        return this.hadError;
    }

    private Expression expression() throws ParserError {
        return term();
    }

    private Expression term() throws ParserError {
        Expression expression = factor();

        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            Expression right = factor();
            expression = new Expression.Binary(expression, operator, right);
        }

        return expression;
    }

    private Expression factor() throws ParserError {
        Expression expression = unary();

        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token operator = previous();
            Expression right = unary();
            expression = new Expression.Binary(expression, operator, right);
        }

        return expression;
    }

    private Expression unary() throws ParserError {
        if (match(TokenType.MINUS)) {
            Token operator = previous();
            Expression right = unary();
            return new Expression.Unary(operator, right);
        }

        return primary();
    }

    private Expression primary() throws ParserError {
        if (match(TokenType.NUMBER)) {
            return new Expression.Literal(previous().literal);
        }

        if (match(TokenType.LEFT_PAREN)) {
            Expression expression = expression();
            consume(TokenType.RIGHT_PAREN, "Expected ')' after expression.");
            return new Expression.Grouping(expression);
        }

        Message.error(peek().line, "Expected expression.");
        throw new ParserError();
    }

    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().type == TokenType.SEMICOLON) return;

            switch (peek().type) {
                case PRINT -> {return;}
                default -> {}
            }
            advance();
        }
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token consume(TokenType type, String message) throws ParserError {
        if (check(type)) return advance();
        Message.error(peek().line, message);
        throw new ParserError();
    }
}
