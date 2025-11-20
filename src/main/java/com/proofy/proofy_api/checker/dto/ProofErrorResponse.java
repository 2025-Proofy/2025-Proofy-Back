package com.proofy.proofy_api.checker.dto;

public record ProofErrorResponse(
        int line,
        String code,
        String message
) {
}
