package com.proofy.proofy_api.checker.domain.line;

import com.proofy.proofy_api.checker.error.CheckError;
import com.proofy.proofy_api.checker.error.ProofCheckException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public final class OneLineNumber implements LineNumber{
    private final int originLine;
    private final int number;     // ※ ex) 3

    @Override
    public LineNumberType getType() { return LineNumberType.ONE; }

    @Override
    public int asOne() {
        if (number <= 0) {
            throw new ProofCheckException(CheckError.BAD_LINE, originLine); // 0 이하 라인 인용 불가
        }
        return number;
    }
}
