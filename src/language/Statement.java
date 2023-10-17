package language;

public abstract class Statement {
    public static class ExpressionStatement extends Statement {
        public final Expression expression;

        public ExpressionStatement(Expression expression) {
            this.expression = expression;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitExpressionStatement(this);
        }
    }

    public static class PrintStatement extends Statement {
        public final Expression expression;

        public PrintStatement(Expression expression) {
            this.expression = expression;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitPrintStatement(this);
        }
    }

    public interface Visitor<T> {
        T visitExpressionStatement(ExpressionStatement expressionStatement);
        T visitPrintStatement(PrintStatement printStatement);
    }

    public abstract<T> T accept(Visitor<T> visitor);
}
