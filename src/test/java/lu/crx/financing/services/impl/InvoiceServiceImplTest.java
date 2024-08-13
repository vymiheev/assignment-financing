package lu.crx.financing.services.impl;

import lu.crx.financing.entities.*;
import lu.crx.financing.services.InvoiceService;
import lu.crx.financing.services.PurchaserService;
import lu.crx.financing.tools.FinancialFormulas;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;

@SpringBootTest
class InvoiceServiceImplTest {

    @Autowired
    private InvoiceService invoiceService;
    @MockBean
    private PurchaserService purchaserService;
    @MockBean
    private CommandLineRunner commandLineRunner;

    @Test
    void processInvoice_smoke() {
        var localDateNow = LocalDate.now();
        var localDateInvoice = LocalDate.now().plusDays(36);
        var creditor1 = Creditor.builder()
                .name("Coffee Beans LLC")
                .maxFinancingRateInBps(5)
                .build();
        var debtor1 = Debtor.builder()
                .name("ChocoLoco")
                .build();
        var invoice = Invoice.builder()
                .creditor(creditor1)
                .debtor(debtor1)
                .valueInCents(100000)
                .maturityDate(localDateInvoice)
                .build();

        var purchaser = Purchaser.builder().minimumFinancingTermInDays(30).name("purchaser1").build();
        long daysBetween = ChronoUnit.DAYS.between(localDateNow, invoice.getMaturityDate());
        Optional<Map.Entry<Integer, Purchaser>> result = Optional.of(Map.entry(10, purchaser));

        doReturn(result)
                .when(purchaserService)
                .choosePurchaser(creditor1, daysBetween);
        invoiceService.processInvoice(invoice);

        long financingRate = FinancialFormulas.calcFinancialRate(result.get().getKey(), daysBetween);
        long earlyPaymentAmount = FinancialFormulas.getEarlyPaymentAmount(invoice.getValueInCents(), financingRate);

        Assertions.assertEquals(result.get().getValue(), invoice.getPurchaser());
        Assertions.assertEquals(LocalDate.now(), invoice.getFinancingDate());
        Assertions.assertEquals(daysBetween, invoice.getFinancingTermInDays());
        Assertions.assertEquals(financingRate, invoice.getFinancingRate());
        Assertions.assertEquals(earlyPaymentAmount, invoice.getEarlyPaymentAmount());
        Assertions.assertTrue(invoice.isFinanced());
    }

    @Test
    void processInvoice_noPurchaser() {
        var localDateNow = LocalDate.now();
        var localDateInvoice = LocalDate.now().plusDays(36);
        var creditor1 = Creditor.builder()
                .name("Coffee Beans LLC")
                .maxFinancingRateInBps(5)
                .build();
        var debtor1 = Debtor.builder()
                .name("ChocoLoco")
                .build();
        var invoice = Invoice.builder()
                .creditor(creditor1)
                .debtor(debtor1)
                .valueInCents(100000)
                .maturityDate(localDateInvoice)
                .build();

        long daysBetween = ChronoUnit.DAYS.between(localDateNow, invoice.getMaturityDate());

        doReturn(Optional.empty())
                .when(purchaserService)
                .choosePurchaser(creditor1, daysBetween);
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

    @Test
    void highLoad() {

    }

}