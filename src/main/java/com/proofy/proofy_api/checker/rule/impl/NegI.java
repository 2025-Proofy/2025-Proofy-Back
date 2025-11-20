package com.proofy.proofy_api.checker.rule.impl;

import com.proofy.proofy_api.checker.dto.ProofErrorResponse;
import com.proofy.proofy_api.checker.error.CheckError;
import com.proofy.proofy_api.checker.domain.line.LineNumberType;
import com.proofy.proofy_api.checker.domain.line.Line;
import com.proofy.proofy_api.checker.domain.proof.Proof;
import com.proofy.proofy_api.checker.domain.sentence.NegSentence;
import com.proofy.proofy_api.checker.domain.sentence.Sentence;
import com.proofy.proofy_api.checker.error.ProofCheckException;
import com.proofy.proofy_api.checker.rule.AbstractRule;

import java.util.List;

public class NegI extends AbstractRule {

    @Override
    public LineNumberType[] lineOrd() {
        return new LineNumberType[]{ LineNumberType.MANY };
    }

    @Override
    public void isRight(Proof proof, Line line, List<ProofErrorResponse> errors) {

        // subproof range
        Subproof sp = citedSubproof(proof, line, 0, errors);
        if (sp == null) return;

        Sentence premise = sp.premise();       // A or ¬A
        Sentence conclusion = sp.conclusion(); // must be ⊥

        // subproof 마지막은 ⊥이어야 함
        if (!conclusion.isBotSignal()) {
            throw new ProofCheckException(CheckError.BAD_USAGE, line.getNumber());
        }

        // ★ 결론 문장을 가져온다 (line.getSentence())
        Sentence result = line.getSentence();

        // ★ 결론 문장이 부정인지 검사
        //    BUT: 교재식 4줄 요약을 허용하기 위해
        //    - result가 NegSentence면 그대로 사용
        //    - result가 AtomSentence면 자동으로 ¬를 붙이고 다시 비교
        Sentence strippedConclusion = stripDoubleNegations(result);

        // ★ premise가 A 라면, neg(inner) = ¬A 여야 함
        // ★ premise가 ¬A 라면, neg(inner) = ¬¬A 여야 함 -> NegE에 의해 A로 줄어들 수도 있음
        Sentence expected = new NegSentence(premise);

        // expected도 strip
        Sentence strippedExpected = stripDoubleNegations(expected);

        // 비교
        if (!same(strippedConclusion, strippedExpected)) {
            throw new ProofCheckException(CheckError.BAD_USAGE, line.getNumber());
        }
    }

    /**
     * NegE와 동일한 로직: 짝수개의 부정은 모두 제거하고, 홀수개면 하나만 남김.
     */
    private Sentence stripDoubleNegations(Sentence s) {
        Sentence curr = s;
        int count = 0;

        while (curr instanceof NegSentence neg) {
            curr = neg.inner();
            count++;
        }

        if (count % 2 == 0) {
            return curr; // completely removed
        }

        // odd → leave one neg
        return new NegSentence(curr);
    }
}