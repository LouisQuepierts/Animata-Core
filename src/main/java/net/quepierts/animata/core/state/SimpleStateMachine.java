package net.quepierts.animata.core.state;

public class SimpleStateMachine extends BaseStateMachine<State<?>> {
    public SimpleStateMachine(State defState) {
        super(defState);
    }
}
