package com.proofy.proofy_api.checker.domain.line;

import com.proofy.proofy_api.checker.error.CheckError;
import com.proofy.proofy_api.checker.error.ProofCheckException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public final class RangeLineNumber implements LineNumber{
    private final int originLine;
    private final int start;      // subproof 시작 라인
    private final int end;        // subproof 끝 라인

    @Override
    public LineNumberType getType() { return LineNumberType.MANY; }

    @Override
    public Range asRange() {
        if (start <= 0 || end <= 0 || start > end) {
            throw new ProofCheckException(CheckError.BAD_RANGE, originLine);
        }
        return new Range(start, end); // 내부 record 사용
    }
}
