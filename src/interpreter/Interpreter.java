package interpreter;

import language.Expression;
import language.Statement;
import scanner.TokenType;
import util.Message;
import java.util.List;
import java.util.Scanner;

public class Interpreter implements Expression.Visitor<Object>, Statement.Visitor<Void> {

    private Environment environment = new Environment();
    private final Scanner scanner = new Scanner(System.in);

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

    private void execute(Statement statement) {
        statement.accept(this);
    }

    private void executeBlock(Statement.Block block, Environment environment) {
        Environment previous = this.environment;
        try {
            this.environment = environment;
            for (Statement statement : block.statements) {
                execute(statement);
            }
        } finally {
            this.environment = previous;
        }
    }

    private String stringify(Object value) {
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
            environment.define(
                variableDeclaration.identifier.lexeme,
                evaluate(variableDeclaration.expression),
                variableDeclaration.mutable
            );
        } catch (Environment.EnvironmentError e) {
            Message.error(variableDeclaration.identifier.line, e.message);
            throw new RuntimeError();
        }
        return null;
    }

    @Override
    public Void visitBlockStatement(Statement.Block blockStatement) {
        executeBlock(blockStatement, new Environment(environment));
        return null;
    }

    @Override
    public Void visitIfStatement(Statement.IfStatement ifStatement) {
        if (truthy(evaluate(ifStatement.condition))) {
            execute(ifStatement.thenBranch);
        } else if (ifStatement.elseBranch != null) {
            execute(ifStatement.elseBranch);
        }
        return null;
    }

    @Override
    public Void visitWhileStatement(Statement.WhileStatement whileStatement) {
        while (truthy(evaluate(whileStatement.condition))) {
            execute(whileStatement.body);
        }
        return null;
    }

    @Override
    public Void visitForStatement(Statement.ForStatement forStatement) throws RuntimeError {
        Object lower = evaluate(forStatement.lower);
        Object higher = evaluate(forStatement.higher);
        if (!(lower instanceof Double) || !(higher instanceof Double)) {
            Message.error(forStatement.identifier.line, "Lower and upper bounds must be numbers");
            throw new RuntimeError();
        }
        environment.define(forStatement.identifier.lexeme, lower, true);
        while (true) {
            execute(forStatement.body);
            Object step = evaluate(forStatement.step);
            if (!(step instanceof Double)) {
                Message.error(forStatement.identifier.line, "The step should be a number.");
                throw new RuntimeError();
            }
            double i = (double) environment.get(forStatement.identifier.lexeme);
            if ((double) step > 0 && (i + (double) step) > (double) higher)
                break;
            if ((double) step < 0 && (i + (double) step) < (double) higher)
                break;
            if (i == (double) higher)
                break;
            environment.update(
                forStatement.identifier.lexeme,
                (double) environment.get(forStatement.identifier.lexeme) + (double) step
            );
        }

        return null;
    }

    @Override
    public Object visitBinaryExpression(Expression.Binary expression) throws RuntimeError {
        Object left = evaluate(expression.left);
        Object right = evaluate(expression.right);
        switch (expression.operator.type) {
            case MINUS -> {
                if (left instanceof Double && right instanceof Double) {
                    return (double) left - (double) right;
                } else {
                    Message.error(expression.operator.line, "Expected number values");
                    throw new RuntimeError();
                }
            }
            case PLUS -> {
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                } else {
                    Message.error(expression.operator.line, "Expected number values");
                    throw new RuntimeError();
                }
            }
            case STAR -> {
                if (left instanceof Double && right instanceof Double) {
                    return (double) left * (double) right;
                } else {
                    Message.error(expression.operator.line, "Expected number values");
                    throw new RuntimeError();
                }
            }
            case DOUBLE_STAR -> {
                if (left instanceof Double && right instanceof Double) {
                    return Math.pow((double) left, (double) right);
                } else {
                    Message.error(expression.operator.line, "Expected number values");
                    throw new RuntimeError();
                }
            }
            case SLASH -> {
                if (left instanceof Double && right instanceof Double) {
                    return Math.pow((double) left, (double) right);
                } else {
                    if ((double) right == 0.0) {
                        Message.error(expression.operator.line, "Division by zero is not allowed");
                        throw new RuntimeError();
                    }
                    Message.error(expression.operator.line, "Expected number values");
                    throw new RuntimeError();
                }
            }
            case DOUBLE_EQUAL -> {
                if (left == null && right == null)
                    return true;
                if (left == null)
                    return false;
                return left.equals(right);
            }
            case GREATER -> {
                if (left instanceof Double && right instanceof Double) {
                    return (double) left > (double) right;
                } else {
                    Message.error(expression.operator.line, "Expected number values");
                    throw new RuntimeError();
                }
            }
            case GREATER_EQUAL -> {
                if (left instanceof Double && right instanceof Double) {
                    return (double) left >= (double) right;
                } else {
                    Message.error(expression.operator.line, "Expected number values");
                    throw new RuntimeError();
                }
            }
            case LESS -> {
                if (left instanceof Double && right instanceof Double) {
                    return (double) left < (double) right;
                } else {
                    Message.error(expression.operator.line, "Expected number values");
                    throw new RuntimeError();
                }
            }
            case LESS_EQUAL -> {
                if (left instanceof Double && right instanceof Double) {
                    return (double) left <= (double) right;
                } else {
                    Message.error(expression.operator.line, "Expected number values");
                    throw new RuntimeError();
                }
            }
        }

        return null;
    }

    @Override
    public Object visitUnaryExpression(Expression.Unary expression) {
        switch (expression.operator.type) {
            case MINUS -> {
                Object right = evaluate(expression.right);
                if (right instanceof Double)
                    return -(double) right;
                else {
                    Message.error(expression.operator.line, "Expected number value");
                    throw new RuntimeError();
                }
            }
            case NOT -> {
                Object right = evaluate(expression.right);
                if (right instanceof Boolean) {
                    return !(boolean) right;
                } else {
                    Message.error(expression.operator.line, "Expected boolean value");
                    throw new RuntimeError();
                }
            }
        }

        return null;
    }

    @Override
    public Object visitLiteralExpression(Expression.Literal expression) {
        return expression.value;
    }

    @Override
    public Object visitGetExpression(Expression.Get expression) {
        double value;
        try {
            value = scanner.nextDouble();
        } catch (Exception e) {
            return null;
        }
        return value;
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

    @Override
    public Object visitLogicalExpression(Expression.Logical expression) {
        Object left = evaluate(expression.left);
        if (expression.operator.type == TokenType.OR) {
            if (truthy(left)) return left;
        } else {
            if (!truthy(left)) return left;
        }

        return evaluate(expression.right);
    }

    private Object evaluate(Expression expression) {
        return expression.accept(this);
    }

    private Boolean truthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean) object;
        if (object instanceof Double) return (double) object != 0.0;
        return true;
    }
}
