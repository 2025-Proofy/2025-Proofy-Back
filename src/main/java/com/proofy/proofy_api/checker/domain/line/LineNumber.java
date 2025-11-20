package com.proofy.proofy_api.checker.domain.line;

import com.proofy.proofy_api.checker.error.CheckError;
import com.proofy.proofy_api.checker.error.ProofCheckException;

public sealed interface LineNumber
        permits OneLineNumber, RangeLineNumber {

    int getOriginLine();

    LineNumberType getType();    // 규칙(lineOrd)에서 type 검사용

    // ONE 라인 인용을 기대할 때 호출
    default int asOne() {
        throw new ProofCheckException(CheckError.BAD_LINE_TYPE, getOriginLine());
    }

    // MANY(subproof) 라인 인용을 기대할 때 호출
    default Range asRange() {
        throw new ProofCheckException(CheckError.BAD_LINE_TYPE, getOriginLine());
    }

    // Range 반환용 record
    record Range(int start, int end) {}
}