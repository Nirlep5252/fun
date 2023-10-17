package parser;

import language.Expression;
import language.Statement;
import scanner.Token;
import scanner.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import util.Message;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;
    private boolean hadError = false;

    private static class ParserError extends RuntimeException {};

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();
        while (!isAtEnd()) {
            try {
                statements.add(statement());
            } catch (ParserError e) {
                synchronize();
                hadError = true;
            }
        }

        return statements;
    }

    public boolean isHadError() {
        return this.hadError;
    }

    public Statement statement() throws ParserError {
        if (match(TokenType.PRINT)) return printStatement();
        return expressionStatement();
    }

    private Statement printStatement() throws ParserError {
        Expression value = expression();
        consume(TokenType.SEMICOLON, "Expected `;` after value.");
        return new Statement.PrintStatement(value);
    }

    private Statement expressionStatement() throws ParserError {
        Expression value = expression();
        consume(TokenType.SEMICOLON, "Expected `;` after value.");
        return new Statement.ExpressionStatement(value);
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
        Expression expression = pow();

        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token operator = previous();
            Expression right = pow();
            expression = new Expression.Binary(expression, operator, right);
        }

        return expression;
    }

    private Expression pow() throws ParserError {
        Stack<Token> operators = new Stack<>();
        Stack<Expression> expressions = new Stack<>();
        expressions.add(unary());

        while (match(TokenType.DOUBLE_STAR)) {
            operators.add(previous());
            expressions.add(unary());
        }

        while (expressions.size() > 1) {
            Expression expression1 = expressions.pop();
            Expression expression2 = expressions.pop();
            Token operator = operators.pop();
            expressions.add(new Expression.Binary(expression2, operator, expression1));
        }

        return expressions.pop();
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
