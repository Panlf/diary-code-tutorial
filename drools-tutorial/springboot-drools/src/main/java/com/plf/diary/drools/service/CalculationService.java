package com.plf.diary.drools.service;

import com.plf.diary.drools.entity.Calculation;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {

    @Autowired
    private KieBase kieBase;

    public Calculation calculationWage(Calculation calculation){
        KieSession kieSession = kieBase.newKieSession();
        kieSession.insert(calculation);
        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("rule_calculation"));
        kieSession.dispose();
        return calculation;
    }
}
