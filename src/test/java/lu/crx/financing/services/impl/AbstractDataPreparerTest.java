package lu.crx.financing.services.impl;

import jakarta.persistence.EntityManager;
import lu.crx.financing.entities.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Random;

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

    public Invoice prepareInvoiceData(Creditor creditor, Debtor debtor) {
        var localDateInvoice = LocalDate.now().plusDays(36);
        var invoice = Invoice.builder()
                .creditor(creditor)
                .debtor(debtor)
                .valueInCents(100000)
                .maturityDate(localDateInvoice)
                .build();
        invoice = entityManager.merge(invoice);

        return invoice;
    }

    public Debtor prepareDebtorData() {
        var debtor = Debtor.builder()
                .name("ChocoLoco")
                .build();
        entityManager.persist(debtor);
        return debtor;
    }

    public Creditor prepareCreditorData() {
        var creditor = Creditor.builder()
                .name(String.valueOf(new Random().nextInt()))
                .maxFinancingRateInBps(5)
                .build();
        entityManager.persist(creditor);
        return creditor;
    }
}
