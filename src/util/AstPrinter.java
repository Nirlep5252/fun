package util;

import language.Expression;

public class AstPrinter implements Expression.Visitor<String> {
    public String print(Expression expression) {
        return expression.accept(this);
    }

    @Override
    public String visitBinaryExpression(Expression.Binary expression) {
        return postfix(expression.operator.lexeme, expression.left, expression.right);
    }

    @Override
    public String visitUnaryExpression(Expression.Unary expression) {
        return postfix(expression.operator.lexeme, expression.right);
    }

    @Override
    public String visitGroupingExpression(Expression.Grouping expression) {
        return postfix("", expression.expression);
    }

    @Override
    public String visitLiteralExpression(Expression.Literal expression) {
        if (expression.value == null) return "null";
        return expression.value.toString();
    }

    String postfix(String name, Expression... expressions) {
        StringBuilder builder = new StringBuilder();
        for (Expression expression : expressions) {
            builder.append(expression.accept(this));
            builder.append(" ");
        }
        builder.append(name);
        return builder.toString();
    }
}
