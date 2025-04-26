package net.quepierts.animata.core.state;

public interface State<StateMachine extends BaseStateMachine> {
    void onEnter(StateMachine stateMachine);

    void onExit(StateMachine stateMachine);

    void onTick(StateMachine stateMachine);

    boolean isFinished(StateMachine stateMachine);

    State<StateMachine> getNextState(StateMachine stateMachine);
}
