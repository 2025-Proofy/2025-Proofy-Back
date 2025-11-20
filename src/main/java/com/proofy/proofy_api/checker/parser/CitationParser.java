package com.proofy.proofy_api.checker.parser;

import com.proofy.proofy_api.checker.domain.line.*;
import com.proofy.proofy_api.checker.error.ParseError;
import com.proofy.proofy_api.checker.error.ProofParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CitationParser {

    public Citation parse(String raw, int originLine) {
        raw = raw.trim();

        if (raw.isEmpty()) {
            return new Citation("", List.of());
        }

        String[] parts = raw.split("\\s+");

        String ruleName = parts[0];
        List<LineNumber> cites = new ArrayList<>();

        if (parts.length == 1) {
            return new Citation(ruleName, cites);
        }

        for (int i = 1; i < parts.length; i++) {
            String token = parts[i];

            if (token.contains("-")) {
                String[] lr = token.split("-");
                if (lr.length != 2)
                    throw new ProofParseException(ParseError.INVALID_CITATION, originLine);

                int start = parseInt(lr[0], originLine);
                int end   = parseInt(lr[1], originLine);

                cites.add(new RangeLineNumber(originLine, start, end));

            } else {
                int one = parseInt(token, originLine);
                cites.add(new OneLineNumber(originLine, one));
            }
        }

        return new Citation(ruleName, cites);
    }

    private int parseInt(String s, int originLine) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            throw new ProofParseException(ParseError.INVALID_CITATION, originLine);
        }
    }
}
