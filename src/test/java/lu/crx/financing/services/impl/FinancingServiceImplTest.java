package lu.crx.financing.services.impl;

import jakarta.persistence.EntityManager;
import lu.crx.financing.entities.*;
import lu.crx.financing.repository.InvoiceRepository;
import lu.crx.financing.services.FinancingService;
import lu.crx.financing.services.InvoiceService;
import lu.crx.financing.services.PurchaserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@jakarta.transaction.Transactional
class FinancingServiceImplTest {
    @Autowired
    private FinancingService financingService;
    @SpyBean
    private InvoiceService invoiceService;
    @MockBean
    private CommandLineRunner commandLineRunner;


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
}