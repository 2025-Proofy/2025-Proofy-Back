package com.proofy.proofy_api.checker.domain.sentence;

// ¬A
public final class NegSentence implements Sentence {
    private final Sentence inner;                // ※ ¬S 의 S

    public NegSentence(Sentence inner) {
        this.inner = inner;
    }

    public Sentence inner() { return inner; }    // ※ 규칙에서 내부 식 비교할 때 사용

    @Override
    public boolean structurallyEquals(Sentence other) {
        return (other instanceof NegSentence n) &&
                n.inner.structurallyEquals(inner);
    }

    @Override
    public String toString() { return "¬" + inner; }
}
