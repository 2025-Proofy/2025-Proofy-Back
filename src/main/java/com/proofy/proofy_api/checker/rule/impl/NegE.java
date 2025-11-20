package com.proofy.proofy_api.checker.rule.impl;

import com.proofy.proofy_api.checker.domain.sentence.NegSentence;
import com.proofy.proofy_api.checker.dto.ProofErrorResponse;
import com.proofy.proofy_api.checker.error.CheckError;
import com.proofy.proofy_api.checker.domain.line.LineNumberType;
import com.proofy.proofy_api.checker.domain.line.Line;
import com.proofy.proofy_api.checker.domain.proof.Proof;
import com.proofy.proofy_api.checker.domain.sentence.Sentence;
import com.proofy.proofy_api.checker.error.ProofCheckException;
import com.proofy.proofy_api.checker.rule.AbstractRule;

import java.util.List;

public class NegE extends AbstractRule {

    @Override
    public LineNumberType[] lineOrd() {
        return new LineNumberType[]{ LineNumberType.ONE };
    }

    @Override
    public void isRight(Proof proof, Line line, List<ProofErrorResponse> errors) {

        Sentence premise = citedSentence(proof, line, 0);

        // premise 전체에서 부정(~ or ¬) 반복적으로 제거
        Sentence inner = stripDoubleNegations(premise);

        // 결론은 strip된 inner와 동일해야 함
        if (!same(inner, line.getSentence())) {
            throw new ProofCheckException(CheckError.BAD_USAGE, line.getNumber());
        }
    }

    /**
     * ¬¬¬¬P → P
     * ¬¬¬¬¬¬Q → Q
     * 짝수 개의 부정이면 모두 제거.
     */
    private Sentence stripDoubleNegations(Sentence s) {
        Sentence curr = s;
        int count = 0;

        // negs count
        while (curr instanceof NegSentence neg) {
            curr = neg.inner();
            count++;
        }

        // 부정이 짝수면 다 제거
        if (count % 2 == 0) {
            return curr;
        }

        // 홀수 부정이면 마지막 하나는 남겨야 함 → ¬(inner)
        Sentence result = curr;
        for (int i = 0; i < count % 2; i++) {
            result = new NegSentence(result);
        }
        return result;
    }
}