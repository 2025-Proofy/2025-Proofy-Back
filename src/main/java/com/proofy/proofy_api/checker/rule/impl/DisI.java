package com.proofy.proofy_api.checker.rule.impl;

import com.proofy.proofy_api.checker.domain.line.LineNumberType;
import com.proofy.proofy_api.checker.domain.line.Line;
import com.proofy.proofy_api.checker.domain.proof.Proof;
import com.proofy.proofy_api.checker.domain.sentence.DisSentence;
import com.proofy.proofy_api.checker.domain.sentence.Sentence;
import com.proofy.proofy_api.checker.dto.ProofErrorResponse;
import com.proofy.proofy_api.checker.error.CheckError;
import com.proofy.proofy_api.checker.error.ProofCheckException;
import com.proofy.proofy_api.checker.rule.AbstractRule;

import java.util.List;

public class DisI extends AbstractRule {

    @Override
    public LineNumberType[] lineOrd() {
        return new LineNumberType[]{ LineNumberType.ONE };
    }

    @Override
    public void isRight(Proof proof, Line line, List<ProofErrorResponse> errors) {
        Sentence src = citedSentence(proof, line, 0);
        Sentence tgt = line.getSentence();

        if (!(tgt instanceof DisSentence dis))
            throw new ProofCheckException(CheckError.BAD_USAGE, line.getNumber());

        if (!(same(dis.getLeft(), src) || same(dis.getRight(), src)))
            throw new ProofCheckException(CheckError.BAD_USAGE, line.getNumber());
    }
}
