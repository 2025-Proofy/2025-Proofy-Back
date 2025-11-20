package com.proofy.proofy_api.checker.rule;

import com.proofy.proofy_api.checker.rule.impl.*;

import java.util.Map;

import static java.util.Map.entry;

public class BasicRuleSet {

    public static final Map<String, Rule> RULES = Map.ofEntries(
            entry("PR", new PR()),          // Premise
            entry("R",  new R()),           // Reiteration
            entry("∧I", new ConI()),        // Conjunction Introduction
            entry("∧E", new ConE()),        // Conjunction Elimination
            entry("∨I", new DisI()),        // Disjunction Introduction
            entry("∨E", new DisE()),        // Disjunction Elimination
            entry("→I", new ImpI()),        // Conditional Introduction
            entry("→E", new ImpE()),        // Conditional Elimination
            entry("↔I", new BicI()),        // Biconditional Introduction
            entry("↔E", new BicE()),        // Biconditional Elimination
            entry("¬I", new NegI()),        // Negation Introduction
            entry("¬E", new NegE()),        // Negation Elimination
            entry("⊥I", new ContraI()),     // Contradiction Introduction
            entry("⊥E",  new ContraE())     // Contradiction Elimination
    );
}
