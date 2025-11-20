package com.proofy.proofy_api.checker.domain.sentence;

import lombok.AllArgsConstructor;
import lombok.Getter;

// A → B
@Getter
@AllArgsConstructor
public final class ImpSentence implements Sentence {
    private final Sentence left;   // antecedent
    private final Sentence right;  // consequent

    @Override
    public boolean structurallyEquals(Sentence other) {
        if (!(other instanceof ImpSentence o)) return false;
        return left.structurallyEquals(o.left) &&
                right.structurallyEquals(o.right);
    }

    @Override
    public String toString() {
        return "(" + left + " → " + right + ")";
    }
}