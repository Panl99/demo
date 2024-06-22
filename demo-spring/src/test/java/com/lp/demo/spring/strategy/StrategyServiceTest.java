package com.lp.demo.spring.strategy;

import com.lp.demo.spring.strategy.common.StrategyFactory;
import com.lp.demo.spring.strategy.common.StrategyService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
class StrategyServiceTest {

    @Resource
    StrategyFactory/*<StrategyTypeEnum_A, StrategyService_A>*/ strategyFactory;

    @Test
    void strategyA1Test() {
        System.out.println("-------------------strategyA1Test------------------");
        StrategyService strategyService = strategyFactory.get(StrategyTypeEnum_A.A_1);
        if (strategyService == null) {
            System.out.println("unsupported strategyService = " + StrategyTypeEnum_A.A_1);
            return;
        }
        strategyService.invoke("strategyA1Test");
    }

    @Test
    void strategyA2Test() {
        System.out.println("-------------------strategyA2Test------------------");
        StrategyService strategyService = strategyFactory.get(StrategyTypeEnum_A.A_2);
        if (strategyService == null) {
            System.out.println("unsupported strategyService = " + StrategyTypeEnum_A.A_2);
            return;
        }
        strategyService.invoke("strategyA2Test");
    }

    @Test
    void strategyA3Test() {
        System.out.println("-------------------strategyA3Test------------------");
        StrategyService strategyService = strategyFactory.get(StrategyTypeEnum_A.A_3);
        if (strategyService == null) {
            System.out.println("unsupported strategyService = " + StrategyTypeEnum_A.A_3);
            return;
        }
        strategyService.invoke("strategyA3Test");
    }

    @Test
    void strategyA4Test() {
        System.out.println("-------------------strategyA4Test------------------");
        StrategyService strategyService = strategyFactory.get(StrategyTypeEnum_A.A_4);
        if (strategyService == null) {
            System.out.println("unsupported strategyService = " + StrategyTypeEnum_A.A_4);
            return;
        }
        strategyService.invoke("strategyA4Test");
    }


    @Test
    void strategyB1Test() {
        System.out.println("-------------------strategyB1Test------------------");
        StrategyService strategyService = strategyFactory.get(StrategyTypeEnum_B.B_1);
        if (strategyService == null) {
            System.out.println("unsupported strategyService = " + StrategyTypeEnum_B.B_1);
            return;
        }
        strategyService.invoke("strategyB1Test");
    }

    @Test
    void strategyB2Test() {
        System.out.println("-------------------strategyB2Test------------------");
        StrategyService strategyService = strategyFactory.get(StrategyTypeEnum_B.B_2);
        if (strategyService == null) {
            System.out.println("unsupported strategyService = " + StrategyTypeEnum_B.B_2);
            return;
        }
        strategyService.invoke("strategyB2Test");
    }

    @Test
    void strategyB3Test() {
        System.out.println("-------------------strategyB3Test------------------");
        StrategyService strategyService = strategyFactory.get(StrategyTypeEnum_B.B_3);
        if (strategyService == null) {
            System.out.println("unsupported strategyService = " + StrategyTypeEnum_B.B_3);
            return;
        }
        strategyService.invoke("strategyB3Test");
    }

}