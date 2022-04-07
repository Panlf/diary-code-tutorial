package com.plf.diary.drools.controller;

import com.plf.diary.drools.entity.Calculation;
import com.plf.diary.drools.service.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rule")
public class CalculationController {

    @Autowired
    private CalculationService calculationService;

    @GetMapping("calculation")
    public Calculation calculation(double wage){
        Calculation calculation = new Calculation();
        calculation.setWage(wage);
        calculation = calculationService.calculationWage(calculation);
        return calculation;
    }
}
