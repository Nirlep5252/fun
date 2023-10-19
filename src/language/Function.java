package language;

import interpreter.Environment;
import interpreter.Interpreter;

import java.util.List;

public class Function implements Callable{
    private final Statement.FunctionDeclaration declaration;
    public Function(Statement.FunctionDeclaration declaration) {
        this.declaration = declaration;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(interpreter.globals);
        for (int i = 0; i < declaration.parameters.size(); i++) {
            environment.define(declaration.parameters.get(i).lexeme, arguments.get(i), true);
        }
        interpreter.executeBlock(declaration.body, environment);
        return null;
    }

    @Override
    public int arity() {
        return this.declaration.parameters.size();
    }

    @Override
    public String toString() {
        return "<fn " + declaration.identifier.lexeme + ">";
    }
}
