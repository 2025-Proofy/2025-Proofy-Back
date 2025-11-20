package com.proofy.proofy_api.checker.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProofParseException extends RuntimeException{
    private final ParseError error;
    private final int originLine;
}
