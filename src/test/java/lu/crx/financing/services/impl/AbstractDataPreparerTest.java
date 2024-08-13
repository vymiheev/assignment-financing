package lu.crx.financing.services.impl;

import jakarta.persistence.EntityManager;
import lu.crx.financing.entities.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public abstract class AbstractDataPreparerTest {
    @Autowired
    private EntityManager entityManager;

    public Purchaser preparePurchaserData(Creditor creditor) {
        var purchaser = Purchaser.builder().minimumFinancingTermInDays(30).name("purchaser1").build();
        entityManager.persist(purchaser);
        var finSettings = PurchaserFinancingSettings.builder()
                .purchaser(purchaser)
                .annualRateInBps(10)
                .creditor(creditor).build();
        entityManager.persist(finSettings);
        return purchaser;
    }

    public Invoice prepareInvoiceData() {
        var localDateInvoice = LocalDate.now().plusDays(36);
        var creditor1 = Creditor.builder()
                .name("Coffee Beans LLC")
                .maxFinancingRateInBps(5)
                .build();
        var debtor1 = Debtor.builder()
                .name("ChocoLoco")
                .build();
        entityManager.persist(creditor1);
        entityManager.persist(debtor1);
        var invoice = Invoice.builder()
                .creditor(creditor1)
                .debtor(debtor1)
                .valueInCents(100000)
                .maturityDate(localDateInvoice)
                .build();
        invoice = entityManager.merge(invoice);

        return invoice;
    }
}
