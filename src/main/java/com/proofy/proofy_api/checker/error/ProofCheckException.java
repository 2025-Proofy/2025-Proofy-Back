package com.proofy.proofy_api.checker.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProofCheckException extends RuntimeException{
    private final CheckError error;
    private final int line;
}
