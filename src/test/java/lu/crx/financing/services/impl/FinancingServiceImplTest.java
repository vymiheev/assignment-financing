package lu.crx.financing.services.impl;

import lu.crx.financing.entities.Invoice;
import lu.crx.financing.repository.InvoiceRepository;
import lu.crx.financing.services.FinancingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class FinancingServiceImplTest {
    @Autowired
    private FinancingService financingService;
    @MockBean
    private InvoiceRepository invoiceRepository;

    @Test
    void finance_empty() {
        PageImpl<Invoice> pageInvoice = new PageImpl<>(new ArrayList<>());
        Pageable pageable = PageRequest.of(0, 3);
        doReturn(pageInvoice)
                .when(invoiceRepository)
                .findAllByFinancedFalse(any(Pageable.class));

        financingService.finance();
    }

    @Test
    void finance_exception() {
    }
}