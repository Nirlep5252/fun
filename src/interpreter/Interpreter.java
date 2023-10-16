package interpreter;

import parser.Expression;
import util.Message;

public class Interpreter implements Expression.Visitor<Object> {
    static class RuntimeError extends RuntimeException {};
    private boolean hadError = false;

    public void interpret(Expression expression) {
        try {
            System.out.println(stringify(evaluate(expression)));
        } catch (RuntimeError e) {
            this.hadError = true;
        }
    }

    public boolean isHadError() {
        return this.hadError;
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
