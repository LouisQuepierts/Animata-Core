package net.quepierts.animata.core.state;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unchecked"})
public abstract class BaseStateMachine<StateType extends State> {
    private final StateType defState;

    @NotNull
    @Getter
    private StateType currState;

    @NotNull
    @Getter
    private StateType nextState;

    protected BaseStateMachine(StateType defState) {
        this.defState = defState;

        this.currState = defState;
        this.nextState = (StateType) defState.getNextState(this);
    }

    public void tick() {
        if (this.currState.isFinished(this)) {
            this.play(this.nextState);
        }

        this.currState.onTick(this);
    }

    public void play(@NotNull StateType nextState) {
        this.currState.onExit(this);
        this.currState = nextState;
        this.currState.onEnter(this);
        this.nextState = (StateType) this.currState.getNextState(this);
    }

    public void stop() {
        if (this.currState != this.defState) {
            this.play(this.defState);
        }
    }
}
