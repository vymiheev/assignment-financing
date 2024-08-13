package lu.crx.financing.services.impl;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.config.CrxConfig;
import lu.crx.financing.entities.Invoice;
import lu.crx.financing.services.FinancingService;
import lu.crx.financing.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class FinancingServiceImpl implements FinancingService {

    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private CrxConfig crxConfig;

    private ExecutorService executorService;


    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(crxConfig.getInvoiceProcessThreadsAmount());
    }

    @Transactional(readOnly = true)
    public void finance() {
        log.info("Financing started");
        AtomicLong invoicesProcessed = new AtomicLong();
        Pageable pageable = PageRequest.of(0, crxConfig.getInvoicePageSize());
        Page<Invoice> invoices;
        do {
            invoices = invoiceService.findAllByFinancedFalse(pageable);

            for (Invoice invoice : invoices.getContent()) {
                Page<Invoice> finalInvoices = invoices;
                executorService.submit(() -> {
                    try {
                        invoiceService.processInvoice(invoice);
                        invoicesProcessed.incrementAndGet();
                        log.info("Invoices processed {} of {}", invoicesProcessed, finalInvoices.getTotalElements());
                    } catch (Exception ex) {
                        log.error("Invoice processing exception {}", invoice, ex);
                    }
                });
            }
            pageable = invoices.nextPageable();
        } while (invoices.hasNext());

        destroy();
        log.info("Financing completed");
    }

    @PreDestroy
    public void destroy() {
        if (executorService.isShutdown()) {
            return;
        }
        executorService.shutdown();
        try {
            // Wait for existing tasks to terminate
            if (!executorService.awaitTermination(crxConfig.getAwaitTermination(), TimeUnit.SECONDS)) {
                log.warn("Timeout occurred, forcing shutdown.");
                List<Runnable> pendingTasks = executorService.shutdownNow();
                log.warn("Pending tasks: " + pendingTasks.size());
            }
        } catch (InterruptedException e) {
            // Current thread was interrupted while waiting
            log.error("Shutdown interrupted.");
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
