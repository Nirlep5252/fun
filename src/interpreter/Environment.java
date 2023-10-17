package interpreter;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, Object> values = new HashMap<>();
    public static class EnvironmentError extends Interpreter.RuntimeError {
        public final String message;

        public EnvironmentError(String message) {
            this.message = message;
        }
    }

    public void define(String name, Object value) throws EnvironmentError {
        if (values.containsKey(name)) {
            throw new EnvironmentError("Variable `" + name + "` is already defined.");
        }
        values.put(name, value);
    }

    public Object get(String name) throws EnvironmentError {
        if (values.containsKey(name)) {
            return values.get(name);
        }
        throw new EnvironmentError("Variable `" + name + "` is not defined.");
    }

    public void update(String name, Object value) throws EnvironmentError {
        if (values.containsKey(name)) {
            values.put(name, value);
            return;
        }
        throw new EnvironmentError("Variable `" + name + "` is not defined.");
    }
}
