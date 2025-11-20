package com.proofy.proofy_api.checker.rule.impl;

import com.proofy.proofy_api.checker.dto.ProofErrorResponse;
import com.proofy.proofy_api.checker.error.CheckError;
import com.proofy.proofy_api.checker.domain.line.LineNumberType;
import com.proofy.proofy_api.checker.domain.line.Line;
import com.proofy.proofy_api.checker.domain.proof.Proof;
import com.proofy.proofy_api.checker.domain.sentence.BicSentence;
import com.proofy.proofy_api.checker.domain.sentence.Sentence;
import com.proofy.proofy_api.checker.error.ProofCheckException;
import com.proofy.proofy_api.checker.rule.AbstractRule;

import java.util.List;

public class BicI extends AbstractRule {

    @Override
    public LineNumberType[] lineOrd() {
        return new LineNumberType[]{ LineNumberType.MANY, LineNumberType.MANY };
    }

    @Override
    public void isRight(Proof proof, Line line, List<ProofErrorResponse> errors) {

        Subproof sp1 = citedSubproof(proof, line, 0, errors); // A → B
        Subproof sp2 = citedSubproof(proof, line, 1, errors); // B → A
        if (sp1 == null || sp2 == null) return;

        if (!(line.getSentence() instanceof BicSentence bic))
            throw new ProofCheckException(CheckError.BAD_USAGE, line.getNumber());

        Sentence A = bic.getLeft();
        Sentence B = bic.getRight();

        boolean ok1 =
                same(sp1.premise(), A) &&
                        same(sp1.conclusion(), B) &&
                        same(sp2.premise(), B) &&
                        same(sp2.conclusion(), A);

        boolean ok2 =
                same(sp1.premise(), B) &&
                        same(sp1.conclusion(), A) &&
                        same(sp2.premise(), A) &&
                        same(sp2.conclusion(), B);

        if (!(ok1 || ok2))
            throw new ProofCheckException(CheckError.BAD_USAGE, line.getNumber());
    }
}
