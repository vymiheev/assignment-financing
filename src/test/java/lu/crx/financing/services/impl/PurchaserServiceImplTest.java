package lu.crx.financing.services.impl;

import lu.crx.financing.entities.Creditor;
import lu.crx.financing.entities.PurchaserFinancingSettings;
import lu.crx.financing.repository.PurchaserFinancingSettingsRepository;
import lu.crx.financing.services.PurchaserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class PurchaserServiceImplTest {
    @Autowired
    private PurchaserService purchaserService;
    @MockBean
    private PurchaserFinancingSettingsRepository purchaserFinancingSettingsRepository;

    @BeforeEach
    public void before() {
        doReturn(List.of(PurchaserFinancingSettings.builder().build()))
                .when(purchaserFinancingSettingsRepository)
                .findByCreditor(Creditor.builder().build());
    }

    @Test
    void choosePurchaser_smoke() {
        purchaserService.choosePurchaser(Creditor.builder().build(), 1L);
    }

    @Test
    void choosePurchaser_filterMinimumFinancingTermInDays() {
    }

    @Test
    void choosePurchaser_filterFinanceRate() {
    }

    @Test
    void choosePurchaser_choosesMinFinanceRate() {
    }

    @Test
    void choosePurchaser_empty() {
    }
}