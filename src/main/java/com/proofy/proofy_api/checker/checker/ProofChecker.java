package com.proofy.proofy_api.checker.checker;

import com.proofy.proofy_api.checker.domain.line.Line;
import com.proofy.proofy_api.checker.domain.line.LineNumber;
import com.proofy.proofy_api.checker.domain.line.LineNumberType;
import com.proofy.proofy_api.checker.domain.proof.Proof;
import com.proofy.proofy_api.checker.dto.ProofErrorResponse;
import com.proofy.proofy_api.checker.error.CheckError;
import com.proofy.proofy_api.checker.error.ProofCheckException;
import com.proofy.proofy_api.checker.rule.Rule;
import com.proofy.proofy_api.checker.rule.RuleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ProofChecker {

    private final RuleRepository ruleRepository;

    // 전체 증명 검증
    public List<ProofErrorResponse> check(Proof proof) {

        List<ProofErrorResponse> errors = new ArrayList<>();

        // 1) 구조 검증
        List<ProofErrorResponse> structureErrors = proof.validateStructure();
        errors.addAll(structureErrors);

        // 2) 각 라인 규칙 검증
        for (Line line : proof.getLines()) {

            // 파싱 실패 라인은 rule 검증 skip
            if (line.isInvalid()) continue;

            String ruleName = line.getCitation().getRuleName();
            Rule rule = ruleRepository.get(ruleName);

            // 존재하지 않는 규칙
            if (rule == null) {
                errors.add(new ProofErrorResponse(
                        line.getNumber(),
                        CheckError.NO_SUCH_RULE.name(),
                        "Unknown rule: " + ruleName
                ));
                continue;
            }

            // (A) 규칙 시그니처 검증 (lineOrd 기반 operand 개수/type 체크)
            try {
                LineNumberType[] expected = rule.lineOrd();
                List<LineNumber> actual = line.getCitation().getCites();

                // operand 개수 불일치
                if (expected.length != actual.size()) {
                    errors.add(new ProofErrorResponse(
                            line.getNumber(),
                            CheckError.BAD_LINE_COUNT.name(),
                            ruleName + " requires " + expected.length +
                                    " operand(s), but got " + actual.size()
                    ));
                    continue;
                }

                // operand 타입 체크 (ONE / MANY)
                for (int i = 0; i < expected.length; i++) {
                    if (actual.get(i).getType() != expected[i]) {
                        errors.add(new ProofErrorResponse(
                                line.getNumber(),
                                CheckError.BAD_LINE_TYPE.name(),
                                ruleName + " operand " + (i + 1) +
                                        " must be " + expected[i] +
                                        " but was " + actual.get(i).getType()
                        ));
                    }
                }

            } catch (ProofCheckException ex) {
                errors.add(new ProofErrorResponse(
                        ex.getLine(),
                        ex.getError().name(),
                        ex.getMessage()
                ));
                continue;
            }

            // (B) 규칙의 실제 의미 검증 (isRight)
            try {
                rule.isRight(proof, line, errors);
            } catch (ProofCheckException ex) {
                errors.add(new ProofErrorResponse(
                        ex.getLine(),
                        ex.getError().name(),
                        ex.getMessage()
                ));
            }
        }

        return errors;
    }
}
