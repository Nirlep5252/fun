package language;

import scanner.Token;
import util.AstPrinter;

/**
 * This class represents an expression in the language.
 * It is an abstract class, and has four subclasses:
 * Binary, Unary, Literal, and Grouping.
 */
public abstract class Expression {
    /**
     * This class represents a binary expression. <br />
     * Eg: <code>1 + 2</code>
     */
    public static class Binary extends Expression {
        public final Expression left;
        public final Token operator;
        public final Expression right;

        public Binary(Expression left, Token operator, Expression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitBinaryExpression(this);
        }
    }

    /**
     * This class represents a unary expression. <br />
     * Eg: <code>-1</code>
     */
    public static class Unary extends Expression {
        public final Token operator;
        public final Expression right;

        public Unary(Token operator, Expression right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitUnaryExpression(this);
        }
    }

    /**
     * This class represents a literal expression. <br />
     * Eg: <code>1</code> or <code>a</code>
     */
    public static class Literal extends Expression {
        public final Object value;

        public Literal(Object value) {
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitLiteralExpression(this);
        }
    }

    /**
     * This class represents a grouping expression. <br />
     * Eg: <code>(1 + 2)</code>
     */
    public static class Grouping extends Expression {
        public final Expression expression;

        public Grouping(Expression expression) {
            this.expression = expression;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitGroupingExpression(this);
        }
    }

    public static class Variable extends Expression {
        public final Token identifier;

        public Variable(Token identifier) {
            this.identifier = identifier;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitVariableExpression(this);
        }
    }

    public static class Assignment extends Expression {
        public final Token identifier;
        public final Expression expression;

        public Assignment(Token identifier, Expression expression) {
            this.identifier = identifier;
            this.expression = expression;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitAssignmentExpression(this);
        }
    }

    public static class Logical extends Expression {
        public final Expression left;
        public final Token operator;
        public final Expression right;

        public Logical(Expression left, Token operator, Expression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitLogicalExpression(this);
        }
    }

    /**
     * This interface is used to implement the
     * <a href="https://en.wikipedia.org/wiki/Visitor_pattern">Visitor pattern</a>.
     * It is used to traverse expression trees.
     * <br /> <br />
     * @see AstPrinter
     * @param <T> The return type of the visitor methods.
     */
    public interface Visitor<T> {
        T visitBinaryExpression (Binary expression);
        T visitUnaryExpression (Unary expression);
        T visitLiteralExpression (Literal expression);
        T visitGroupingExpression (Grouping expression);
        T visitVariableExpression (Variable expression);
        T visitAssignmentExpression(Assignment assignment);
        T visitLogicalExpression(Logical expression);
    }

    /**
     * This method is used to implement the
     * <a href="https://en.wikipedia.org/wiki/Visitor_pattern">Visitor pattern</a>.
     * <br /> <br />
     * The subclasses override this method to call the appropriate
     * visitor method.
     * @param visitor The visitor object.
     * @return The return value of the visitor methods.
     * @param <T> The return type of the visitor methods.
     */
    public abstract<T> T accept(Visitor<T> visitor);
}
