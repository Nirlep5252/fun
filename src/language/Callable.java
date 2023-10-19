package language;

import interpreter.Interpreter;
import java.util.List;

public interface Callable {
    Object call (Interpreter interpreter, List<Object> arguments);
    int arity();
}
