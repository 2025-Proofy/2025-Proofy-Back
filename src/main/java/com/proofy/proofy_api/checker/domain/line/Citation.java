package com.proofy.proofy_api.checker.domain.line;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Citation {
    private final String ruleName;          // ※ "^I", "→I", "¬E" 등
    private final List<LineNumber> cites;   // ※ 라인 인용 목록
}
