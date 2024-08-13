package lu.crx.financing.services;

import lu.crx.financing.entities.Invoice;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface InvoiceService {

    void processInvoice(@NotNull Invoice invoice);

    Page<Invoice> findAllByFinancedFalse(Pageable pageable);
}
