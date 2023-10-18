package language;

import scanner.Token;

import java.util.List;

public abstract class Statement {
    public static class Block extends Statement {
        public List<Statement> statements;

        public Block(List<Statement> statements) {
            this.statements = statements;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitBlockStatement(this);
        }
    }

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

    public static class VariableDeclaration extends Statement {
        public final Token identifier;
        public final Expression expression;
        public final Boolean mutable;

        public VariableDeclaration(Token identifier, Expression expression, Boolean mutable) {
            this.identifier = identifier;
            this.expression = expression;
            this.mutable = mutable;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitVariableDeclarationStatement(this);
        }
    }

    public static class IfStatement extends Statement {
        public final Expression condition;
        public final Statement thenBranch;
        public final Statement elseBranch;

        public IfStatement(Expression condition, Statement thenBranch, Statement elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitIfStatement(this);
        }
    }

    public interface Visitor<T> {
        T visitExpressionStatement(ExpressionStatement expressionStatement);
        T visitPrintStatement(PrintStatement printStatement);
        T visitVariableDeclarationStatement(VariableDeclaration variableAssignmentOrDeclarationStatement);
        T visitBlockStatement(Block blockStatement);
        T visitIfStatement(IfStatement ifStatement);
    }

    public abstract<T> T accept(Visitor<T> visitor);
}
