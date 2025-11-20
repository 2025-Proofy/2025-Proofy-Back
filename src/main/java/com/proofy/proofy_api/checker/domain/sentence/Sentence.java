package com.proofy.proofy_api.checker.domain.sentence;

public sealed interface Sentence permits AtomSentence, BicSentence, BotSentence, ConSentence, DisSentence, ImpSentence, NegSentence {
    // (1) 논리식 문자열 표현용
    String toString();  // ※ AST를 문자열로 재출력하기 위한 메소드

    // (2) 두 문장이 구조적으로 같은지 비교
    boolean structurallyEquals(Sentence other); // ※ 패턴 매칭 규칙 검증용

    // (3) 편의 메소드: ¬S 생성
    default Sentence negate() {                  // ※ 규칙에서 자주 쓰임 (ex. ¬E)
        return new NegSentence(this);
    }

    // (4) 편의 메소드: ⊥ 판별
    default boolean isBotSignal() {        // ⊥ 판별
        return this instanceof BotSentence;
    }
}
