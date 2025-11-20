package com.proofy.proofy_api.checker.parser;

import com.proofy.proofy_api.checker.domain.sentence.*;
import com.proofy.proofy_api.checker.error.ParseError;
import com.proofy.proofy_api.checker.error.ProofParseException;
import org.springframework.stereotype.Component;

@Component
public class SentenceParser {

    public Sentence parse(String raw, int originLine) {
        raw = raw.trim();

        // 1) 바깥 괄호가 전체 문장을 감싸는 경우에만 제거
        raw = stripOuterParens(raw);

        // 2) 연산자 우선순위: ↔ → ∨ ∧  (항상 top-level에서만 찾아야 함)
        int idx;

        // ↔
        idx = findMainOperator(raw, "↔");
        if (idx != -1) {
            return new BicSentence(
                    parse(raw.substring(0, idx).trim(), originLine),
                    parse(raw.substring(idx + 1).trim(), originLine)
            );
        }

        // →
        idx = findMainOperator(raw, "→");
        if (idx != -1) {
            return new ImpSentence(
                    parse(raw.substring(0, idx).trim(), originLine),
                    parse(raw.substring(idx + 1).trim(), originLine)
            );
        }

        // ∨
        idx = findMainOperator(raw, "∨");
        if (idx != -1) {
            return new DisSentence(
                    parse(raw.substring(0, idx).trim(), originLine),
                    parse(raw.substring(idx + 1).trim(), originLine)
            );
        }

        // ∧
        idx = findMainOperator(raw, "∧");
        if (idx != -1) {
            return new ConSentence(
                    parse(raw.substring(0, idx).trim(), originLine),
                    parse(raw.substring(idx + 1).trim(), originLine)
            );
        }

        // ¬ 또는 ~
        if (raw.startsWith("¬") || raw.startsWith("~")) {
            return new NegSentence(parse(raw.substring(1).trim(), originLine));
        }

        // ⊥ 또는 #
        if (raw.equals("⊥") || raw.equals("#")) {
            return new BotSentence();
        }

        // Atom
        if (raw.matches("[A-Za-z][A-Za-z0-9]*")) {
            return new AtomSentence(raw);
        }

        throw new ProofParseException(ParseError.INVALID_SENTENCE, originLine);
    }

    /** 괄호가 전체 문장을 감싸는 경우에만 제거 */
    private String stripOuterParens(String raw) {
        if (!raw.startsWith("(") || !raw.endsWith(")")) return raw;

        int depth = 0;
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (c == '(') depth++;
            else if (c == ')') depth--;

            if (depth == 0 && i != raw.length() - 1) {
                return raw; // 바깥을 완전히 감싸는 괄호가 아님
            }
        }
        // 완전 외곽 괄호
        return raw.substring(1, raw.length() - 1).trim();
    }

    /** top-level 연산자(괄호 깊이 0)만 찾는 함수 */
    int findMainOperator(String raw, String symbol) {
        int depth = 0;
        int len = symbol.length();

        for (int i = 0; i < raw.length() - len + 1; i++) {
            char c = raw.charAt(i);

            if (c == '(') depth++;
            else if (c == ')') depth--;

            if (depth == 0 && raw.startsWith(symbol, i)) {
                return i;
            }
        }
        return -1;
    }
}