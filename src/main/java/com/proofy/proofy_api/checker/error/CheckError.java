package com.proofy.proofy_api.checker.error;

public enum CheckError {
    NO_SUCH_RULE,        // 존재하지 않는 규칙
    BAD_LINE_COUNT,      // 라인 인용 개수 불일치
    BAD_LINE_TYPE,       // ONE vs MANY 타입 불일치
    BAD_USAGE,           // 규칙 자체 적용 실패
    BAD_LINE,            // 미래 라인 인용, self 인용 등
    BAD_RANGE,           // subproof 범위 이상함
    UNAVAILABLE,         // 접근 불가 라인 인용
    INVALID_PROOF_STRUCTURE // 구조 자체가 이상함
}
