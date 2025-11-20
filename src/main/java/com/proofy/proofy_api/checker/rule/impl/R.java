package com.proofy.proofy_api.checker.rule.impl;

import com.proofy.proofy_api.checker.domain.line.LineNumberType;
import com.proofy.proofy_api.checker.domain.line.Line;
import com.proofy.proofy_api.checker.domain.proof.Proof;
import com.proofy.proofy_api.checker.dto.ProofErrorResponse;
import com.proofy.proofy_api.checker.error.CheckError;
import com.proofy.proofy_api.checker.error.ProofCheckException;
import com.proofy.proofy_api.checker.rule.AbstractRule;

import java.util.List;

public class R extends AbstractRule {

    @Override
    public LineNumberType[] lineOrd() {
        return new LineNumberType[]{ LineNumberType.ONE };
    }

    @Override
    public void isRight(Proof proof, Line line, List<ProofErrorResponse> errors) {
        var source = citedSentence(proof, line, 0);

        if (!same(source, line.getSentence())) {
            throw new ProofCheckException(
                    CheckError.BAD_USAGE, line.getNumber()
            );
        }
    }
}