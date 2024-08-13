package lu.crx.financing.services.impl;

import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.entities.Invoice;
import lu.crx.financing.entities.Purchaser;
import lu.crx.financing.repository.InvoiceRepository;
import lu.crx.financing.services.InvoiceService;
import lu.crx.financing.services.PurchaserService;
import lu.crx.financing.tools.FinancialFormulas;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private PurchaserService purchaserService;

    public Page<Invoice> findAllByFinancedFalse(Pageable pageable) {
        return invoiceRepository.findAllByFinancedFalse(pageable);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processInvoice(@NotNull Invoice invoice) {
        processInvoiceSameTransaction(invoice);
    }

    @Override
    @Transactional
    //used in tests
    public void processInvoiceSameTransaction(@NotNull Invoice invoice) {
        log.info("Process invoice {}", invoice);
        final LocalDate localDate = LocalDate.now();

        long daysBetween = ChronoUnit.DAYS.between(localDate, invoice.getMaturityDate());
        Optional<Map.Entry<Integer, Purchaser>> rateAndPurchaser = purchaserService.choosePurchaser(invoice.getCreditor(), daysBetween);
        if (rateAndPurchaser.isEmpty()) {
            log.info("Not Found Purchaser for {}", invoice.getId());
            return;
        }
        invoice.setPurchaser(rateAndPurchaser.get().getValue());
        invoice.setFinancingDate(localDate);
        invoice.setFinancingTermInDays(daysBetween);

        long financingRate = FinancialFormulas.calcFinancialRate(rateAndPurchaser.get().getKey(), daysBetween);
        invoice.setFinancingRate(financingRate);
        invoice.setEarlyPaymentAmount(FinancialFormulas.getEarlyPaymentAmount(invoice.getValueInCents(), financingRate));
        invoice.setFinanced(true);
        log.info("Invoice {} successfully financed.", invoice.getId());
        invoiceRepository.save(invoice);
    }
}
