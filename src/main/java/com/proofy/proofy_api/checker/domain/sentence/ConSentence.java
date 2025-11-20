package com.proofy.proofy_api.checker.domain.sentence;

import lombok.AllArgsConstructor;
import lombok.Getter;

// A ∧ B
@Getter
@AllArgsConstructor
public final class ConSentence implements Sentence {
    private final Sentence left;
    private final Sentence right;

    @Override
    public boolean structurallyEquals(Sentence other) {
        if (!(other instanceof ConSentence o)) return false;
        return left.structurallyEquals(o.left) &&
                right.structurallyEquals(o.right);
    }

    @Override
    public String toString() {
        return "(" + left + " ∧ " + right + ")";
    }
}