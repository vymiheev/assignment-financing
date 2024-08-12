package lu.crx.financing;

import lu.crx.financing.tools.FinancialFormulas;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FinancialFormulasTest {

    @Test
    void calcFinancialRate() {
        Assertions.assertEquals(0, FinancialFormulas.calcFinancialRate(0, 1));
        Assertions.assertEquals(0, FinancialFormulas.calcFinancialRate(1, 0));
        Assertions.assertEquals(0, FinancialFormulas.calcFinancialRate(1, 1));
        Assertions.assertEquals(1, FinancialFormulas.calcFinancialRate(1, 180));
        Assertions.assertEquals(0, FinancialFormulas.calcFinancialRate(0, 179));
        Assertions.assertEquals(10, FinancialFormulas.calcFinancialRate(3600, 1));
    }

    @Test
    void getEarlyPaymentAmount() {
        Assertions.assertEquals(99, FinancialFormulas.getEarlyPaymentAmount(100, 1));
        Assertions.assertEquals(97, FinancialFormulas.getEarlyPaymentAmount(100, 3));
        Assertions.assertEquals(0, FinancialFormulas.getEarlyPaymentAmount(0, 3));
        Assertions.assertEquals(0, FinancialFormulas.getEarlyPaymentAmount(0, 0));
        Assertions.assertEquals(100, FinancialFormulas.getEarlyPaymentAmount(100, 0));
    }
}