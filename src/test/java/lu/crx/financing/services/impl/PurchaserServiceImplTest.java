package lu.crx.financing.services.impl;

import lu.crx.financing.entities.Creditor;
import lu.crx.financing.entities.Purchaser;
import lu.crx.financing.entities.PurchaserFinancingSettings;
import lu.crx.financing.repository.PurchaserFinancingSettingsRepository;
import lu.crx.financing.services.PurchaserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class PurchaserServiceImplTest {
    @Autowired
    private PurchaserService purchaserService;
    @MockBean
    private PurchaserFinancingSettingsRepository purchaserFinancingSettingsRepository;
    @MockBean
    private CommandLineRunner commandLineRunner;

    @Test
    void choosePurchaser_smoke() {
        var purchaser = Purchaser.builder().minimumFinancingTermInDays(30).name("purchaser1").build();
        var creditor = Creditor.builder().maxFinancingRateInBps(10).name("creditor1").build();
        var list = List.of(PurchaserFinancingSettings.builder().
                creditor(creditor)
                .annualRateInBps(10)
                .purchaser(purchaser)
                .build());
        var financingTerm = 30;
        doReturn(list)
                .when(purchaserFinancingSettingsRepository)
                .findByCreditor(creditor);
        var result = purchaserService.choosePurchaser(creditor, financingTerm);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(list.getFirst().getAnnualRateInBps(), result.get().getKey());
        Assertions.assertEquals(purchaser, result.get().getValue());
    }

    @Test
    void choosePurchaser_filterMinimumFinancingTermInDays() {
        var purchaser1 = Purchaser.builder().minimumFinancingTermInDays(31).name("purchaser1").build();
        var purchaser2 = Purchaser.builder().minimumFinancingTermInDays(30).name("purchaser2").build();
        var creditor = Creditor.builder().maxFinancingRateInBps(10).name("creditor1").build();
        var list = List.of(PurchaserFinancingSettings.builder().
                creditor(creditor)
                .annualRateInBps(10)
                .purchaser(purchaser1)
                .build(), PurchaserFinancingSettings.builder().
                creditor(creditor)
                .annualRateInBps(10)
                .purchaser(purchaser2)
                .build());
        var financingTerm = 30;
        doReturn(list)
                .when(purchaserFinancingSettingsRepository)
                .findByCreditor(creditor);
        var result = purchaserService.choosePurchaser(creditor, financingTerm);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(list.getFirst().getAnnualRateInBps(), result.get().getKey());
        Assertions.assertEquals(purchaser2, result.get().getValue());
    }

    @Test
    void choosePurchaser_filterFinanceRate() {
        var purchaser1 = Purchaser.builder().minimumFinancingTermInDays(30).name("purchaser1").build();
        var purchaser2 = Purchaser.builder().minimumFinancingTermInDays(30).name("purchaser2").build();
        var creditor = Creditor.builder().maxFinancingRateInBps(2).name("creditor1").build();
        var list = List.of(PurchaserFinancingSettings.builder().
                creditor(creditor)
                .annualRateInBps(20)
                .purchaser(purchaser1)
                .build(), PurchaserFinancingSettings.builder().
                creditor(creditor)
                .annualRateInBps(30)
                .purchaser(purchaser2)
                .build());
        var financingTerm = 36;
        doReturn(list)
                .when(purchaserFinancingSettingsRepository)
                .findByCreditor(creditor);
        //30*36/360
        var result = purchaserService.choosePurchaser(creditor, financingTerm);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(list.getFirst().getAnnualRateInBps(), result.get().getKey());
        Assertions.assertEquals(purchaser1, result.get().getValue());
    }

    @Test
    void choosePurchaser_choosesMinFinanceRate() {
        var purchaser1 = Purchaser.builder().minimumFinancingTermInDays(30).name("purchaser1").build();
        var purchaser2 = Purchaser.builder().minimumFinancingTermInDays(30).name("purchaser2").build();
        var creditor = Creditor.builder().maxFinancingRateInBps(10).name("creditor1").build();
        var list = List.of(PurchaserFinancingSettings.builder().
                creditor(creditor)
                .annualRateInBps(14)
                .purchaser(purchaser1)
                .build(), PurchaserFinancingSettings.builder().
                creditor(creditor)
                .annualRateInBps(13)
                .purchaser(purchaser2)
                .build());
        var financingTerm = 36;
        doReturn(list)
                .when(purchaserFinancingSettingsRepository)
                .findByCreditor(creditor);
        //13*36/360
        var result = purchaserService.choosePurchaser(creditor, financingTerm);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(list.getLast().getAnnualRateInBps(), result.get().getKey());
        Assertions.assertEquals(purchaser2, result.get().getValue());
    }

    @Test
    void choosePurchaser_empty() {
        var purchaser1 = Purchaser.builder().minimumFinancingTermInDays(30).name("purchaser1").build();
        var creditor = Creditor.builder().maxFinancingRateInBps(1).name("creditor1").build();
        var list = List.of(PurchaserFinancingSettings.builder().
                creditor(creditor)
                .annualRateInBps(30)
                .purchaser(purchaser1)
                .build());
        var financingTerm = 36;
        doReturn(list)
                .when(purchaserFinancingSettingsRepository)
                .findByCreditor(creditor);
        //13*36/360
        var result = purchaserService.choosePurchaser(creditor, financingTerm);
        Assertions.assertTrue(result.isEmpty());
    }
}