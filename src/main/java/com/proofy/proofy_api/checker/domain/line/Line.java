package com.proofy.proofy_api.checker.domain.line;

import com.proofy.proofy_api.checker.domain.sentence.Sentence;
import com.proofy.proofy_api.checker.error.ProofParseException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Line {

    private final int number;
    private final int depth;
    private final Sentence sentence;
    private final Citation citation;
    private final boolean invalid;

    public Line(int number, int depth, Sentence sentence, Citation citation) {
        this.number = number;
        this.depth = depth;
        this.sentence = sentence;
        this.citation = citation;
        this.invalid = false;
    }

    public static Line invalid(int number, int depth) {
        return new Line(number, depth, null, null, true);
    }

    public boolean isAssumption() {
        String r = citation.getRuleName();

        // ★ 핵심 FIX
        if (this.depth > 0 && "PR".equals(r)) {
            return true;
        }

        return switch (r) {
            case "→I", "¬I", "∨E", "IP", "⊥I" -> true;
            default -> false;
        };
    }
}
