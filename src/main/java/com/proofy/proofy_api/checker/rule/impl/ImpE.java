package com.proofy.proofy_api.checker.rule.impl;

import com.proofy.proofy_api.checker.dto.ProofErrorResponse;
import com.proofy.proofy_api.checker.error.CheckError;
import com.proofy.proofy_api.checker.domain.line.LineNumberType;
import com.proofy.proofy_api.checker.domain.line.Line;
import com.proofy.proofy_api.checker.domain.proof.Proof;
import com.proofy.proofy_api.checker.domain.sentence.ImpSentence;
import com.proofy.proofy_api.checker.domain.sentence.Sentence;
import com.proofy.proofy_api.checker.error.ProofCheckException;
import com.proofy.proofy_api.checker.rule.AbstractRule;

import java.util.List;

public class ImpE extends AbstractRule {

    @Override
    public LineNumberType[] lineOrd() {
        return new LineNumberType[]{
                LineNumberType.ONE,
                LineNumberType.ONE
        };
    }

    @Override
    public void isRight(Proof proof, Line line, List<ProofErrorResponse> errors) {
        Sentence s1 = citedSentence(proof, line, 0);
        Sentence s2 = citedSentence(proof, line, 1);
        Sentence tgt = line.getSentence();

        // Case 1: s1 = A → B, s2 = A, tgt = B
        if (s1 instanceof ImpSentence imp1) {
            if (same(imp1.getLeft(), s2) && same(imp1.getRight(), tgt)) {
                return;
            }
        }

        // Case 2: s2 = A → B, s1 = A, tgt = B
        if (s2 instanceof ImpSentence imp2) {
            if (same(imp2.getLeft(), s1) && same(imp2.getRight(), tgt)) {
                return;
            }
        }

        throw new ProofCheckException(CheckError.BAD_USAGE, line.getNumber());
    }
}
