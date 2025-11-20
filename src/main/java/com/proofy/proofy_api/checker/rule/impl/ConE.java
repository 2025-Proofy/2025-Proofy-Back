package com.proofy.proofy_api.checker.rule.impl;

import com.proofy.proofy_api.checker.domain.line.LineNumberType;
import com.proofy.proofy_api.checker.domain.line.Line;
import com.proofy.proofy_api.checker.domain.proof.Proof;
import com.proofy.proofy_api.checker.domain.sentence.ConSentence;
import com.proofy.proofy_api.checker.domain.sentence.Sentence;
import com.proofy.proofy_api.checker.dto.ProofErrorResponse;
import com.proofy.proofy_api.checker.error.CheckError;
import com.proofy.proofy_api.checker.error.ProofCheckException;
import com.proofy.proofy_api.checker.rule.AbstractRule;

import java.util.List;

public class ConE extends AbstractRule {

    @Override
    public LineNumberType[] lineOrd() {
        return new LineNumberType[]{ LineNumberType.ONE };
    }

    @Override
    public void isRight(Proof proof, Line line, List<ProofErrorResponse> errors) {
        Sentence source = citedSentence(proof, line, 0);

        if (!(source instanceof ConSentence con))
            throw new ProofCheckException(CheckError.BAD_USAGE, line.getNumber());

        boolean ok = same(con.getLeft(), line.getSentence()) ||
                same(con.getRight(), line.getSentence());

        if (!ok) throw new ProofCheckException(CheckError.BAD_USAGE, line.getNumber());
    }
}
