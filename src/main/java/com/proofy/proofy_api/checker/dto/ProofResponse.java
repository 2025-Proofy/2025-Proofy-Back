package com.proofy.proofy_api.checker.dto;

import java.util.List;

public record ProofResponse(
        boolean success,
        List<ProofErrorResponse> errors
) {
    public static ProofResponse ok() {
        return new ProofResponse(true, List.of());
    }

    public static ProofResponse fail(List<ProofErrorResponse> errors) {
        return new ProofResponse(false, errors);
    }
}
