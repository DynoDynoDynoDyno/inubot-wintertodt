package main.java.org.rspeer.scripts.f2ppker.task;

import org.rspeer.game.script.Task;
import main.java.org.rspeer.scripts.f2ppker.domain.Domain;

/**
 * Base task class that all PKing script tasks should extend.
 * Provides access to the domain and standardizes execution flow.
 */
public abstract class BotTask extends Task {
    protected final Domain domain;

    protected BotTask(Domain domain) {
        this.domain = domain;
    }

    /**
     * Main execution method. Will only perform doExecute() if shouldExecute() returns true.
     * @return true if the task executed successfully, false otherwise
     */
    @Override
    public final boolean execute() {
        return shouldExecute() && doExecute();
    }

    /**
     * Determines if this task should execute in the current game state.
     * @return true if task conditions are met, false otherwise
     */
    protected abstract boolean shouldExecute();

    /**
     * Performs the actual task execution after shouldExecute() has approved.
     * @return true if the execution succeeded, false otherwise
     */
    protected abstract boolean doExecute();
}