package interpreter;

import language.Expression;
import language.Statement;
import util.Message;

import java.util.List;

public class Interpreter implements Expression.Visitor<Object>, Statement.Visitor<Void> {

    private final Environment environment = new Environment();

    static class RuntimeError extends RuntimeException {}
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
    public Void visitVariableDeclarationStatement(Statement.VariableDeclaration variableDeclaration) throws RuntimeError {
        try {
            environment.define(variableDeclaration.identifier.lexeme, evaluate(variableDeclaration.expression));
        } catch (Environment.EnvironmentError e) {
            Message.error(variableDeclaration.identifier.line, e.message);
            throw new RuntimeError();
        }
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
            case DOUBLE_STAR -> {
                return Math.pow((double) evaluate(expression.left), (double) evaluate(expression.right));
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

    @Override
    public Object visitVariableExpression(Expression.Variable expression) throws RuntimeError {
        try {
            return environment.get(expression.identifier.lexeme);
        } catch (Environment.EnvironmentError e) {
            Message.error(expression.identifier.line, e.message);
            throw new RuntimeError();
        }
    }

    @Override
    public Object visitAssignmentExpression(Expression.Assignment assignment) {
        Object value = evaluate(assignment.expression);
        try {
            environment.update(assignment.identifier.lexeme, value);
        } catch (Environment.EnvironmentError e) {
            Message.error(assignment.identifier.line, e.message);
            throw new RuntimeError();
        }
        return value;
    }

    public Object evaluate(Expression expression) {
        return expression.accept(this);
    }
}
