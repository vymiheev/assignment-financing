package lu.crx.financing.services.impl;

import lu.crx.financing.entities.Invoice;
import lu.crx.financing.repository.PurchaserFinancingSettingsRepository;
import lu.crx.financing.services.FinancingService;
import lu.crx.financing.services.InvoiceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class FinancingServiceImplTest extends AbstractDataPreparerTest {
    @Autowired
    private FinancingService financingService;
    @SpyBean
    private InvoiceService invoiceService;
    @MockBean
    private CommandLineRunner commandLineRunner;
    @Autowired
    TransactionTemplate txTemplate;


    @Test
    void finance_empty() {
        PageImpl<Invoice> pageInvoice = new PageImpl<>(new ArrayList<>());
        doReturn(pageInvoice)
                .when(invoiceService)
                .findAllByFinancedFalse(any(Pageable.class));

        financingService.finance();
        Mockito.verify(invoiceService, times(0)).processInvoice(any(Invoice.class));
    }

    @Test
    void finance_exception() {

    }

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void highLoad() {
        txTemplate.executeWithoutResult(_ -> {
            var debtor = prepareDebtorData();
            var creditor = prepareCreditorData();
            prepareInvoiceData(creditor, debtor);
            preparePurchaserData(creditor);

            for (int i = 0; i < 10_000; i++) {
                prepareInvoiceData(creditor, debtor);
            }
        });

        txTemplate.executeWithoutResult(_ -> {
            financingService.finance();
        });
    }
}