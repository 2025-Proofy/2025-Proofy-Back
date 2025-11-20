package com.proofy.proofy_api.checker.rule.impl;

import com.proofy.proofy_api.checker.domain.line.LineNumberType;
import com.proofy.proofy_api.checker.domain.line.Line;
import com.proofy.proofy_api.checker.domain.proof.Proof;
import com.proofy.proofy_api.checker.domain.sentence.BicSentence;
import com.proofy.proofy_api.checker.domain.sentence.Sentence;
import com.proofy.proofy_api.checker.dto.ProofErrorResponse;
import com.proofy.proofy_api.checker.error.CheckError;
import com.proofy.proofy_api.checker.error.ProofCheckException;
import com.proofy.proofy_api.checker.rule.AbstractRule;

import java.util.List;

public class BicE extends AbstractRule {

    @Override
    public LineNumberType[] lineOrd() {
        return new LineNumberType[]{ LineNumberType.ONE, LineNumberType.ONE };
    }

    @Override
    public void isRight(Proof proof, Line line, List<ProofErrorResponse> errors) {

        Sentence s1 = citedSentence(proof, line, 0);
        Sentence s2 = citedSentence(proof, line, 1);
        Sentence tgt = line.getSentence();

        if (!(s1 instanceof BicSentence bic))
            throw new ProofCheckException(CheckError.BAD_USAGE, line.getNumber());

        boolean ok =
                (same(bic.getLeft(), s2) && same(bic.getRight(), tgt)) ||
                        (same(bic.getRight(), s2) && same(bic.getLeft(), tgt));

        if (!ok)
            throw new ProofCheckException(CheckError.BAD_USAGE, line.getNumber());
    }
}
