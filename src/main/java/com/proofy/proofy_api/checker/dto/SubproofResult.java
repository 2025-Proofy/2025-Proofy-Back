package com.proofy.proofy_api.checker.dto;

import com.proofy.proofy_api.checker.rule.AbstractRule;

import java.util.List;

public record SubproofResult(
        AbstractRule.Subproof subproof,   // 성공 시 채워짐
        List<ProofErrorResponse> errors   // 실패 시 채워짐
) {
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }
}
