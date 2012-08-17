package org.springmodules.validation.valang.functions;

import org.springframework.util.Assert;
import org.springmodules.validation.valang.ValangException;

/**
 * @author Uri Boness
 */
public abstract class AbstractInitializableFunction implements Function {

    private Function[] arguments;

    private FunctionTemplate functionTemplate;

    protected AbstractInitializableFunction() {
    }

    protected AbstractInitializableFunction(Function[] arguments, int line, int column) {
        init(arguments, line, column);
    }

    public void init(Function[] arguments, int line, int column) {
        Assert.state(this.arguments == null, "Function already initialized");
        Assert.notNull(arguments, "The arguments of a function cannot be null");

        try {
            validateArguments(arguments);
        } catch (Throwable t) {
            throw new ValangException(t, line, column);
        }

        this.arguments = arguments;
        functionTemplate = new FunctionTemplate(line, column);
    }

    public Object getResult(Object target) {
        if (!isInitialized()) {
            throw new IllegalStateException("Function " + this + "' is not initialized. init(Function[]) " +
                "must be called in order to initialize a function");
        }
        return functionTemplate.execute(target, new FunctionCallback() {
            public Object execute(Object target) throws Exception {
                return getResult(target, arguments);
            }
        });
    }

    protected abstract void validateArguments(Function[] arguments) throws RuntimeException;

    protected abstract Object getResult(Object target, Function[] arguments);

    //=============================================== Setter/Getter ====================================================

    public Function[] getArguments() {
        return arguments;
    }

    //=============================================== Helper Methods ===================================================

    protected boolean isInitialized() {
        return arguments != null;
    }

}
