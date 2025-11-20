package com.proofy.proofy_api.checker.rule;

import com.proofy.proofy_api.checker.rule.BasicRuleSet;
import com.proofy.proofy_api.checker.rule.Rule;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Component
@Repository
public class RuleRepository {

    private final Map<String, Rule> rules = new HashMap<>();

    public RuleRepository() {
        rules.putAll(BasicRuleSet.RULES);
    }

    // 룰 추가
    public void addRules(Map<String, Rule> rules) {
        rules.putAll(rules);
    }

    // 룰 조회
    public Rule get(String name) {
        return rules.get(name);
    }

    public boolean contains(String name) {
        return rules.containsKey(name);
    }
}
