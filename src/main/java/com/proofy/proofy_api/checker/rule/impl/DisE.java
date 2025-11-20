package com.proofy.proofy_api.checker.rule.impl;

import com.proofy.proofy_api.checker.domain.sentence.Sentence;
import com.proofy.proofy_api.checker.dto.ProofErrorResponse;
import com.proofy.proofy_api.checker.error.CheckError;
import com.proofy.proofy_api.checker.domain.line.LineNumberType;
import com.proofy.proofy_api.checker.domain.line.Line;
import com.proofy.proofy_api.checker.domain.proof.Proof;
import com.proofy.proofy_api.checker.domain.sentence.DisSentence;
import com.proofy.proofy_api.checker.error.ProofCheckException;
import com.proofy.proofy_api.checker.rule.AbstractRule;

import java.util.List;

public class DisE extends AbstractRule {

    @Override
    public LineNumberType[] lineOrd() {
        return new LineNumberType[]{
                LineNumberType.ONE,
                LineNumberType.MANY,
                LineNumberType.MANY
        };
    }

    @Override
    public void isRight(Proof proof, Line line, List<ProofErrorResponse> errors) {

        // cited[0] = A ∨ B
        Sentence dis = citedSentence(proof, line, 0);
        if (!(dis instanceof DisSentence d))
            throw new ProofCheckException(CheckError.BAD_USAGE, line.getNumber());

        // cited[1] = subproof A ... C
        Subproof sp1 = citedSubproof(proof, line, 1, errors);
        // cited[2] = subproof B ... C
        Subproof sp2 = citedSubproof(proof, line, 2, errors);
        if (sp1 == null || sp2 == null) return;

        Sentence p1 = sp1.premise();
        Sentence c1 = sp1.conclusion();
        Sentence p2 = sp2.premise();
        Sentence c2 = sp2.conclusion();

        // 결론 두 개가 같아야 한다
        if (!same(c1, c2) || !same(c1, line.getSentence()))
            throw new ProofCheckException(CheckError.BAD_USAGE, line.getNumber());

        // (A == p1 && B == p2) or (A == p2 && B == p1)
        boolean ok =
                (same(d.getLeft(), p1) && same(d.getRight(), p2)) ||
                        (same(d.getLeft(), p2) && same(d.getRight(), p1));

        if (!ok)
            throw new ProofCheckException(CheckError.BAD_USAGE, line.getNumber());
    }
}
