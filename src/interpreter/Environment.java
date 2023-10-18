package interpreter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Environment {
    private final Environment enclosing;
    private final Map<String, Object> values = new HashMap<>();
    private final Set<String> mutableVariables = new HashSet<>();

    public Environment() {
        this.enclosing = null;
    }

    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    public static class EnvironmentError extends Interpreter.RuntimeError {
        public final String message;

        public EnvironmentError(String message) {
            this.message = message;
        }
    }

    public void define(String name, Object value, Boolean mutable) throws EnvironmentError {
        if (values.containsKey(name)) {
            throw new EnvironmentError("Variable `" + name + "` is already defined.");
        }
        if (mutable) mutableVariables.add(name);
        values.put(name, value);
    }

    public Object get(String name) throws EnvironmentError {
        if (values.containsKey(name)) {
            return values.get(name);
        }
        if (enclosing != null) {
            return enclosing.get(name);
        }
        throw new EnvironmentError("Variable `" + name + "` is not defined.");
    }

    public void update(String name, Object value) throws EnvironmentError {
        if (values.containsKey(name)) {
            if (!mutableVariables.contains(name)) {
                throw new EnvironmentError("Variable `" + name + "` is not mutable.");
            }
            values.put(name, value);
            return;
        }
        if (enclosing != null) {
            enclosing.update(name, value);
            return;
        }
        throw new EnvironmentError("Variable `" + name + "` is not defined.");
    }
}
