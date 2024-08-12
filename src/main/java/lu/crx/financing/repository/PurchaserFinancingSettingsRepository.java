package lu.crx.financing.repository;

import lu.crx.financing.entities.Creditor;
import lu.crx.financing.entities.PurchaserFinancingSettings;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaserFinancingSettingsRepository extends JpaRepository<PurchaserFinancingSettings, Long> {

    @EntityGraph(value = "FinancingSettings.Purchaser", type = EntityGraph.EntityGraphType.FETCH)
    List<PurchaserFinancingSettings> findByCreditor(@NotNull Creditor creditor);
}
