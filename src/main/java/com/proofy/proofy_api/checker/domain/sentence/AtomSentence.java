package com.proofy.proofy_api.checker.domain.sentence;

import lombok.AllArgsConstructor;

// A
@AllArgsConstructor
public final class AtomSentence implements Sentence{
    private final String name;                   // ※ "A", "B", "P", "Q" 등

    @Override
    public boolean structurallyEquals(Sentence other) {
        return (other instanceof AtomSentence a) &&
                a.name.equals(this.name);
    }

    @Override
    public String toString() { return name; }
}
