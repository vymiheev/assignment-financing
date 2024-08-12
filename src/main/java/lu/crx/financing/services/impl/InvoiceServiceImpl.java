package lu.crx.financing.services.impl;

import lombok.extern.slf4j.Slf4j;
import lu.crx.financing.FinancialFormulas;
import lu.crx.financing.entities.Invoice;
import lu.crx.financing.entities.Purchaser;
import lu.crx.financing.repository.InvoiceRepository;
import lu.crx.financing.services.InvoiceService;
import lu.crx.financing.services.PurchaserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processInvoice(@NotNull Invoice invoice) {
        log.info("Process invoice {}", invoice);
        final LocalDate localDate = LocalDate.now();

        long daysBetween = ChronoUnit.DAYS.between(localDate, invoice.getMaturityDate());
        Optional<Map.Entry<Long, Purchaser>> rateAndPurchaser = purchaserService.choosePurchaser(invoice.getCreditor(), daysBetween);
        if (rateAndPurchaser.isEmpty()) {
            log.info("Not Found Purchaser for {}", invoice.getId());
            return;
        }
        long financingRate = rateAndPurchaser.get().getKey();
        invoice.setPurchaser(rateAndPurchaser.get().getValue());
        invoice.setFinancingDate(localDate);
        invoice.setFinancingTermInDays(daysBetween);

        invoice.setFinancingRate(financingRate);
        invoice.setEarlyPaymentAmount(FinancialFormulas.getEarlyPaymentAmount(invoice.getValueInCents(), financingRate));
        invoice.setFinanced(true);
        log.info("Invoice {} successfully financed.", invoice.getId());
        invoiceRepository.saveAndFlush(invoice);
    }
}
