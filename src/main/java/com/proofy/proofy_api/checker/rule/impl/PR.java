package com.proofy.proofy_api.checker.rule.impl;

import com.proofy.proofy_api.checker.domain.line.Line;
import com.proofy.proofy_api.checker.domain.line.LineNumberType;
import com.proofy.proofy_api.checker.domain.proof.Proof;
import com.proofy.proofy_api.checker.dto.ProofErrorResponse;
import com.proofy.proofy_api.checker.rule.AbstractRule;

import java.util.List;

public class PR extends AbstractRule {
    @Override
    public LineNumberType[] lineOrd() {
        return new LineNumberType[]{};
    }

    @Override
    public void isRight(Proof proof, Line line, List<ProofErrorResponse> errors) {
        // 항상 OKAY
    }
}
