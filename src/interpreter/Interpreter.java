package interpreter;

import language.Expression;
import language.Statement;
import util.Message;

import java.util.List;

public class Interpreter implements Expression.Visitor<Object>, Statement.Visitor<Void> {

    static class RuntimeError extends RuntimeException {};
    private boolean hadError = false;
    public boolean isHadError() {
        return this.hadError;
    }

    public void interpret(List<Statement> statements) {
        try {
            for (Statement statement : statements) {
                execute(statement);
            }
        } catch (RuntimeError e) {
            this.hadError = true;
        }
    }

    public void execute(Statement statement) {
        statement.accept(this);
    }

    public String stringify(Object value) {
        if (value == null) return "NULL";
        if (value instanceof Double) {
            String text = value.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return value.toString();
    }

    @Override
    public Void visitExpressionStatement(Statement.ExpressionStatement expressionStatement) {
        evaluate(expressionStatement.expression);
        return null;
    }

    @Override
    public Void visitPrintStatement(Statement.PrintStatement printStatement) {
        Object value = evaluate(printStatement.expression);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Object visitBinaryExpression(Expression.Binary expression) throws RuntimeError {
        switch (expression.operator.type) {
            case MINUS -> {
                return (double) evaluate(expression.left) - (double) evaluate(expression.right);
            }
            case PLUS -> {
                return (double) evaluate(expression.left) + (double) evaluate(expression.right);
            }
            case STAR -> {
                return (double) evaluate(expression.left) * (double) evaluate(expression.right);
            }
            case SLASH -> {
                Object right = evaluate(expression.right);
                if (right instanceof Double && (double) right == 0.0) {
                    Message.error(expression.operator.line, "Division by zero");
                    throw new RuntimeError();
                }
                return (double) evaluate(expression.left) / (double) evaluate(expression.right);
            }
        }

        return null;
    }

    @Override
    public Object visitUnaryExpression(Expression.Unary expression) {
        switch (expression.operator.type) {
            case MINUS -> {
                return -(double) evaluate(expression.right);
            }
        }

        return null;
    }

    @Override
    public Object visitLiteralExpression(Expression.Literal expression) {
        return expression.value;
    }

    @Override
    public Object visitGroupingExpression(Expression.Grouping expression) {
        return evaluate(expression.expression);
    }

    public Object evaluate(Expression expression) {
        return expression.accept(this);
    }
}
