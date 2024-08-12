package lu.crx.financing.services.impl;

import lu.crx.financing.FinancialFormulas;
import lu.crx.financing.entities.Creditor;
import lu.crx.financing.entities.Purchaser;
import lu.crx.financing.repository.PurchaserFinancingSettingsRepository;
import lu.crx.financing.services.PurchaserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class PurchaserServiceImpl implements PurchaserService {

    @Autowired
    private PurchaserFinancingSettingsRepository financingSettingRepository;

/*    @NotNull
    //todo doc
    public Pair<Long, Purchaser> choosePurchaser(@NotNull Creditor creditor, long financingTerm) {
        final List<PurchaserFinancingSettings> purchasers = financingSettingRepository.findByCreditor(creditor);
        Pair<Long, Purchaser> result = new Pair<>(Long.MAX_VALUE, null);
        for (var it : purchasers) {
            if (financingTerm < it.getPurchaser().getMinimumFinancingTermInDays()) {
                continue;
            }
            long financeRate = FinancialFormulas.calcFinancialRate(it.getAnnualRateInBps(), financingTerm);
            if (creditor.getMaxFinancingRateInBps() > financeRate) {
                continue;
            }

            if (financeRate < result.a) {
                result = new Pair<>(financeRate, it.getPurchaser());
            }
        }
        return result;
    }*/

    //todo doc
    @Transactional(readOnly = true)
    public Optional<Map.Entry<Long, Purchaser>> choosePurchaser(@NotNull Creditor creditor, long financingTerm) {
        return financingSettingRepository.findByCreditor(creditor).stream()
                .filter(it -> financingTerm >= it.getPurchaser().getMinimumFinancingTermInDays())
                .map(settings -> {
                    long financeRate = FinancialFormulas.calcFinancialRate(settings.getAnnualRateInBps(), financingTerm);
                    return Map.entry(financeRate, settings.getPurchaser());
                })
                .filter(entry -> creditor.getMaxFinancingRateInBps() <= entry.getKey())
                .min(Map.Entry.comparingByKey());
    }
}
