package com.proofy.proofy_api.checker.rule;

import com.proofy.proofy_api.checker.dto.ProofErrorResponse;
import com.proofy.proofy_api.checker.error.CheckError;
import com.proofy.proofy_api.checker.domain.line.Line;
import com.proofy.proofy_api.checker.domain.line.LineNumberType;
import com.proofy.proofy_api.checker.domain.proof.Proof;
import com.proofy.proofy_api.checker.error.ProofCheckException;

import java.util.List;

public interface Rule {

    LineNumberType[] lineOrd();  // expected citation types

    // is_right
    void isRight(Proof proof, Line line, List<ProofErrorResponse> errors);
}
