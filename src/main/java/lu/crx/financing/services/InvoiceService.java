package lu.crx.financing.services;

import lu.crx.financing.entities.Invoice;
import org.jetbrains.annotations.NotNull;


public interface InvoiceService {

    void processInvoice(@NotNull Invoice invoice);
}
