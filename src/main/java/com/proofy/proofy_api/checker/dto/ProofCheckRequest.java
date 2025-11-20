package com.proofy.proofy_api.checker.dto;

import com.proofy.proofy_api.checker.domain.sentence.Sentence;

import java.util.List;

public record ProofCheckRequest(
        List<String> premises,
        String conclusion,
        List<LineRequest> lines
) {
}
