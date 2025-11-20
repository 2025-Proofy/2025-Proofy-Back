package com.proofy.proofy_api.checker.domain.sentence;

public final class BotSentence implements Sentence {

    @Override
    public boolean structurallyEquals(Sentence other) {
        return other instanceof BotSentence;
    }

    @Override
    public String toString() {
        return "⊥";
    }
}
