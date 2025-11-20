package com.proofy.proofy_api.checker.service;

import com.proofy.proofy_api.checker.checker.ProofChecker;
import com.proofy.proofy_api.checker.domain.line.Line;
import com.proofy.proofy_api.checker.domain.proof.Proof;
import com.proofy.proofy_api.checker.domain.sentence.Sentence;
import com.proofy.proofy_api.checker.dto.ProofCheckRequest;
import com.proofy.proofy_api.checker.dto.ProofErrorResponse;
import com.proofy.proofy_api.checker.error.CheckError;
import com.proofy.proofy_api.checker.error.ProofCheckException;
import com.proofy.proofy_api.checker.error.ProofParseException;
import com.proofy.proofy_api.checker.parser.CitationParser;
import com.proofy.proofy_api.checker.parser.SentenceParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CheckerService {

    private final ProofChecker proofChecker;
    private final CitationParser citationParser;
    private final SentenceParser sentenceParser;

    public List<ProofErrorResponse> checkProof(ProofCheckRequest request) {
        List<ProofErrorResponse> errors = new ArrayList<>();

        // 1) 요청을 Proof 도메인으로 변환
        List<Line> lines = request.lines().stream()
                .map(req -> {
                    try {
                        return new Line(
                                req.number(),
                                req.depth(),
                                sentenceParser.parse(req.sentence(), req.number()),
                                citationParser.parse(req.citation(), req.number())
                        );
                    } catch (ProofParseException e) {
                        errors.add(new ProofErrorResponse(
                                e.getOriginLine(),
                                e.getError().name(),
                                "문장, 인용 검사 중 파싱에 실패했습니다."
                        ));
                        return Line.invalid(req.number(), req.depth());
                    }
                })
                .toList();
        Proof proof = new Proof(lines);

        // 2) premise와 conclusion이 맞는지 확인
        List<Sentence> premises = request.premises().stream()
                .map(premiseString -> sentenceParser.parse(premiseString, 0))
                .toList();
        String conclusion = sentenceParser.parse(request.conclusion(), 0).toString();
        List<Line> proofLines = proof.getLines();   // Proof(lines) 내부에서 파싱된 라인

        // premise 개수 검증
        if (proofLines.size() < premises.size() + 1) // 제공된 증명 < 전제 + 결론 개수
            errors.add(new ProofErrorResponse(0, "INVALID_PROOF_STRUCTURE", "충분한 Line을 작성하지 않았습니다."));

        // ----- Premise 검증 -----
        for (int i = 0; i < premises.size(); i++) {
            Line line = proofLines.get(i);

            // 파싱 실패한 줄이면 premise mismatch 체크 생략
            if (line.isInvalid()) continue;

            String expected = premises.get(i).toString();
            String actual = line.getSentence().toString();

            if (!expected.equals(actual))
                errors.add(new ProofErrorResponse(line.getNumber(),
                        "PREMISE_MISMATCH",
                        String.format("Premise %d가 문제의 premise와 일치하지 않습니다. expected=%s, actual=%s",
                                i + 1, expected, actual)));
        }

        // ----- Conclusion 검증 -----
        Line actualConclusion = proofLines.get(proofLines.size() - 1);

        // 1) 결론 줄 자체가 invalid → conclusion mismatch 검사 skip
        if (actualConclusion.isInvalid()) {
            // 결론 parse error는 이미 파싱 단계에서 errors 리스트에 들어간 상태.
            // 여기서 중복 에러 찍지 않음.
            return errors;
        }

        String actualConclusionSentence = actualConclusion.getSentence().toString();

        if (!conclusion.equals(actualConclusionSentence)) {
            errors.add(new ProofErrorResponse(
                    actualConclusion.getNumber(),
                    "CONCLUSION_MISMATCH",
                    String.format("결론이 문제에서 요구한 conclusion과 다릅니다. expected=%s, actual=%s",
                            conclusion, actualConclusionSentence)
            ));
        }

        // 2) ProofChecker 실행
        errors.addAll(proofChecker.check(proof));  // ← 성공 시 예외 없음
        return errors;
    }
}
