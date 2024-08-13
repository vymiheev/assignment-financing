package lu.crx.financing.services.impl;

import lu.crx.financing.entities.Purchaser;
import lu.crx.financing.services.InvoiceService;
import lu.crx.financing.tools.FinancialFormulas;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
@Transactional
class InvoiceServiceImplTest extends AbstractDataPreparerTest {

    @Autowired
    private InvoiceService invoiceService;
    @MockBean
    private CommandLineRunner commandLineRunner;

    @Test
    void processInvoice_smoke() {
        var debtor = prepareDebtorData();
        var creditor = prepareCreditorData();
        var invoice = prepareInvoiceData(creditor, debtor);
        var purchaser = preparePurchaserData(invoice.getCreditor());

        invoiceService.processInvoiceSameTransaction(invoice);

        var localDateNow = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(localDateNow, invoice.getMaturityDate());
        Optional<Map.Entry<Integer, Purchaser>> result = Optional.of(Map.entry(10, purchaser));
        long financingRate = FinancialFormulas.calcFinancialRate(result.get().getKey(), daysBetween);
        long earlyPaymentAmount = FinancialFormulas.getEarlyPaymentAmount(invoice.getValueInCents(), financingRate);

        Assertions.assertEquals(result.get().getValue(), invoice.getPurchaser());
        Assertions.assertEquals(localDateNow, invoice.getFinancingDate());
        Assertions.assertEquals(daysBetween, invoice.getFinancingTermInDays());
        Assertions.assertEquals(financingRate, invoice.getFinancingRate());
        Assertions.assertEquals(earlyPaymentAmount, invoice.getEarlyPaymentAmount());
        Assertions.assertTrue(invoice.isFinanced());
    }

    @Test
    void processInvoice_noPurchaser() {
        var debtor = prepareDebtorData();
        var creditor = prepareCreditorData();
        var invoice = prepareInvoiceData(creditor, debtor);

        invoiceService.processInvoice(invoice);

        Assertions.assertNull(invoice.getPurchaser());
        Assertions.assertNull(invoice.getFinancingDate());
        Assertions.assertEquals(0, invoice.getFinancingTermInDays());
        Assertions.assertEquals(0, invoice.getFinancingRate());
        Assertions.assertEquals(0, invoice.getEarlyPaymentAmount());
        Assertions.assertFalse(invoice.isFinanced());
    }

    @Test
    void processInvoice_exception() {
    }

}