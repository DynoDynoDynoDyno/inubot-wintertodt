package main.java.org.rspeer.scripts.f2ppker.task;

import main.java.org.rspeer.scripts.f2ppker.data.Action;
import main.java.org.rspeer.scripts.f2ppker.domain.Domain;

/**
 * Specialized task for actions that involve animations and cooldowns.
 * Extends BotTask and adds tracking for specific game actions.
 */
public abstract class AnimatedTask extends BotTask {
    protected final Action action;

    /**
     * Creates a new animated task with the specified action.
     * @param domain The domain containing game state
     * @param action The action that this task performs
     */
    protected AnimatedTask(Domain domain, Action action) {
        super(domain);
        this.action = action;
    }

    /**
     * Checks if this action is currently on cooldown.
     * @return true if the action is ready to be performed, false if on cooldown
     */
    protected boolean isActionReady() {
        return domain.getTimers().isActionReady(action);
    }

    /**
     * Records that this action was performed, starting its cooldown.
     */
    protected void recordAction() {
        domain.getTimers().recordAction(action);
    }
}
