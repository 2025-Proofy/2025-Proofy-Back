package com.proofy.proofy_api.checker.domain.proof;

import com.proofy.proofy_api.checker.domain.line.Line;
import com.proofy.proofy_api.checker.dto.ProofErrorResponse;
import com.proofy.proofy_api.checker.dto.SubproofResult;
import com.proofy.proofy_api.checker.rule.AbstractRule;
import com.proofy.proofy_api.checker.error.CheckError;
import com.proofy.proofy_api.checker.error.ProofCheckException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static com.proofy.proofy_api.checker.error.CheckError.BAD_RANGE;

@AllArgsConstructor
@Getter
@Setter
public class Proof {

    private final List<Line> lines;
    public int size() { return lines.size(); }
    public Line line1Based(int n) { return lines.get(n - 1); }

    public SubproofResult extractSubproof(Line useLine, int start, int end) {
        List<ProofErrorResponse> errors = new ArrayList<>();

        // 기본 유효성: start ~ end 범위는 존재해야 함
        if (start < 1 || end < start)
            errors.add(new ProofErrorResponse(useLine.getNumber(), BAD_RANGE.name(), "기본 유효성 에러: start ~ end 범위는 존재해야 함"));

        Line startLine = line1Based(start);
        Line endLine = line1Based(end);

        int depth = startLine.getDepth();

        // ★ subproof는 depth >= 1 이어야 함
        if (depth < 1)
            errors.add(new ProofErrorResponse(useLine.getNumber(), BAD_RANGE.name(), "subproof는 depth >= 1 이어야 함"));

        // ★ subproof의 시작과 끝 depth는 동일해야 함
        if (endLine.getDepth() != depth)
            errors.add(new ProofErrorResponse(useLine.getNumber(), BAD_RANGE.name(), "subproof의 시작과 끝 depth는 동일해야 함"));

        // ★ subproof 내부는 depth가 감소하면 안 됨
        for (int i = start + 1; i <= end; i++) {
            Line mid = line1Based(i);
            if (mid.getDepth() < depth)
                errors.add(new ProofErrorResponse(useLine.getNumber(), BAD_RANGE.name(), "subproof 내부는 depth가 감소하면 안 됨"));
        }

        // ★ subproof conclusion 다음 라인(depth 감소)는 검사하지 않는다
        // 교재식 자연연역에서는 바로 다음 subproof가 와도 정상임.

        if (errors.isEmpty())
            return new SubproofResult(new AbstractRule.Subproof(startLine.getSentence(), endLine.getSentence()), null);
        else return new SubproofResult(null, errors);
    }

    public List<ProofErrorResponse> validateStructure() {
        List<ProofErrorResponse> errors = new ArrayList<>();

        Deque<Integer> stack = new ArrayDeque<>();
        int prevDepth = 0;
        int expectedNumber = 1;

        for (Line line : lines) {
            int depth = line.getDepth();
            int num = line.getNumber();

            // ------------------------------
            // 0) Line 번호 오름차순 검증
            // ------------------------------
            if (num != expectedNumber) {
                errors.add(new ProofErrorResponse(
                        num,
                        CheckError.INVALID_PROOF_STRUCTURE.name(),
                        String.format(
                                "Line 번호가 순차적으로 증가하지 않습니다. expected=%d, actual=%d",
                                expectedNumber, num
                        )
                ));
            }
            expectedNumber++;

            // ------------------------------
            // 1) depth < 0 (불가능)
            // ------------------------------
            if (depth < 0) {
                errors.add(new ProofErrorResponse(
                        num,
                        CheckError.BAD_RANGE.name(),
                        "Depth cannot be negative (line " + num + ", depth=" + depth + ")"
                ));
                prevDepth = depth;
                continue;
            }

            // ------------------------------
            // 2) depth jump > 1 (불가능)
            // ex: depth 0 → 2 점프 같은 경우
            // ------------------------------
            if (depth > prevDepth + 1) {
                errors.add(new ProofErrorResponse(
                        num,
                        CheckError.BAD_RANGE.name(),
                        "Invalid depth jump from " + prevDepth + " to " + depth + " at line " + num
                ));
            }

            // ------------------------------
            // 3) 새 subproof 시작 (depth 증가)
            // ------------------------------
            if (depth == prevDepth + 1) {
                if (!line.isInvalid()) {
                    if (!line.isAssumption()) {
                        errors.add(new ProofErrorResponse(
                                num,
                                CheckError.BAD_RANGE.name(),
                                "Depth increased from " + prevDepth + " to " + depth +
                                        " but line " + num + " is not an assumption."
                        ));
                    }
                    stack.push(num);
                }
            }

            // ------------------------------
            // 4) subproof 닫힘 (depth 감소)
            // ------------------------------
            else if (depth < prevDepth) {
                while (stack.size() > depth) {
                    stack.pop();
                }
            }

            // depth == prevDepth → ok, 아무 것도 안 함

            prevDepth = depth;
        }

        // ------------------------------
        // 5) subproof가 열린 채로 끝나면 오류
        // ------------------------------
        if (!stack.isEmpty()) {
            int last = lines.get(lines.size() - 1).getNumber();
            errors.add(new ProofErrorResponse(
                    last,
                    CheckError.BAD_RANGE.name(),
                    "Unclosed subproof detected at line " + last
            ));
        }

        return errors;
    }
}
