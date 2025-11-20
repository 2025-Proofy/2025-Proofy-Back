package com.proofy.proofy_api.checker.rule.impl;

import com.proofy.proofy_api.checker.dto.ProofErrorResponse;
import com.proofy.proofy_api.checker.error.CheckError;
import com.proofy.proofy_api.checker.domain.line.LineNumberType;
import com.proofy.proofy_api.checker.domain.line.Line;
import com.proofy.proofy_api.checker.domain.proof.Proof;
import com.proofy.proofy_api.checker.domain.sentence.NegSentence;
import com.proofy.proofy_api.checker.domain.sentence.Sentence;
import com.proofy.proofy_api.checker.error.ProofCheckException;
import com.proofy.proofy_api.checker.rule.AbstractRule;

import java.util.List;

public class ContraI extends AbstractRule {

    @Override
    public LineNumberType[] lineOrd() {
        return new LineNumberType[]{
                LineNumberType.ONE,  // line #1: P
                LineNumberType.ONE   // line #2: ¬P
        };
    }

    @Override
    public void isRight(Proof proof, Line line, List<ProofErrorResponse> errors) {

        // conclusion must be ⊥
        if (!line.getSentence().isBotSignal())
            throw new ProofCheckException(CheckError.BAD_USAGE, line.getNumber());

        // cited sentences
        Sentence s1 = citedSentence(proof, line, 0);
        Sentence s2 = citedSentence(proof, line, 1);

        // contradiction requires: one is negation of the other
        boolean ok =
                same(s1.negate(), s2) ||
                        same(s2.negate(), s1);

        if (!ok)
            throw new ProofCheckException(CheckError.BAD_USAGE, line.getNumber());
    }
}