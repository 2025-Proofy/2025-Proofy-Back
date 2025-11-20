package com.proofy.proofy_api.checker.dto;

public record LineRequest(
        Integer number,
        Integer depth,
        String sentence,
        String citation
) {
}
