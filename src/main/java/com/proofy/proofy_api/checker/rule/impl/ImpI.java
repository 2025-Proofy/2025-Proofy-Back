package com.proofy.proofy_api.checker.rule.impl;

import com.proofy.proofy_api.checker.dto.ProofErrorResponse;
import com.proofy.proofy_api.checker.error.CheckError;
import com.proofy.proofy_api.checker.domain.line.LineNumberType;
import com.proofy.proofy_api.checker.domain.line.Line;
import com.proofy.proofy_api.checker.domain.proof.Proof;
import com.proofy.proofy_api.checker.domain.sentence.ImpSentence;
import com.proofy.proofy_api.checker.error.ProofCheckException;
import com.proofy.proofy_api.checker.rule.AbstractRule;

import java.util.List;

public class ImpI extends AbstractRule {

    @Override
    public LineNumberType[] lineOrd() {
        return new LineNumberType[]{ LineNumberType.MANY };
    }

    @Override
    public void isRight(Proof proof, Line line, List<ProofErrorResponse> errors) {
        Subproof sp = citedSubproof(proof, line, 0, errors);
        if (sp == null) return;

        if (!(line.getSentence() instanceof ImpSentence imp))
            throw new ProofCheckException(CheckError.BAD_USAGE, line.getNumber());

        // sp.premise = A, sp.conclusion = B
        if (!(same(imp.getLeft(), sp.premise()) &&
                same(imp.getRight(), sp.conclusion())))
            throw new ProofCheckException(CheckError.BAD_USAGE, line.getNumber());
    }
}
