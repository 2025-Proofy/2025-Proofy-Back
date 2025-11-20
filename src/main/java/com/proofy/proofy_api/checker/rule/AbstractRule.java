package com.proofy.proofy_api.checker.rule;

import com.proofy.proofy_api.checker.domain.line.Line;
import com.proofy.proofy_api.checker.domain.line.LineNumber;
import com.proofy.proofy_api.checker.domain.proof.Proof;
import com.proofy.proofy_api.checker.domain.sentence.Sentence;
import com.proofy.proofy_api.checker.dto.ProofErrorResponse;
import com.proofy.proofy_api.checker.dto.SubproofResult;

import java.util.List;

public abstract class AbstractRule implements Rule {

    // ONE citation sentence
    protected Sentence citedSentence(Proof proof, Line line, int index) {
        LineNumber ln = line.getCitation().getCites().get(index);
        int n = ln.asOne();
        return proof.line1Based(n).getSentence();
    }

    // MANY citation => subproof
    protected Subproof citedSubproof(Proof proof, Line line, int index, List<ProofErrorResponse> errors) {
        var range = line.getCitation().getCites().get(index).asRange();
        SubproofResult result = proof.extractSubproof(line, range.start(), range.end());

        if (result.hasErrors()) {
            errors.addAll(result.errors());
            return null;
        }

        return result.subproof();
    }

    protected boolean same(Sentence a, Sentence b) {
        return a.structurallyEquals(b);
    }

    public record Subproof(Sentence premise, Sentence conclusion) {}
}
