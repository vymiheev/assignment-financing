package lu.crx.financing.services.impl;

import lu.crx.financing.entities.Creditor;
import lu.crx.financing.entities.Purchaser;
import lu.crx.financing.entities.PurchaserFinancingSettings;
import lu.crx.financing.repository.PurchaserFinancingSettingsRepository;
import lu.crx.financing.services.PurchaserService;
import lu.crx.financing.tools.FinancialFormulas;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class PurchaserServiceImpl implements PurchaserService {

    @Autowired
    private PurchaserFinancingSettingsRepository financingSettingRepository;

    /**
     *  select 1 purchaser
     * @param creditor input creditor from invoice
     * @param financingTerm calculcated days of invoice
     * @return Map.Entry<Integer, Purchaser> where key is a AnnualRateInBps, value - Purchaser
     */
    @Transactional(readOnly = true)
    public Optional<Map.Entry<Integer, Purchaser>> choosePurchaser(@NotNull Creditor creditor, long financingTerm) {
        final List<PurchaserFinancingSettings> purchasers = financingSettingRepository.findByCreditor(creditor);
        Map.Entry<Integer, Purchaser> result = null;
        for (var it : purchasers) {
            if (financingTerm < it.getPurchaser().getMinimumFinancingTermInDays()) {
                continue;
            }
            long financeRate = FinancialFormulas.calcFinancialRate(it.getAnnualRateInBps(), financingTerm);
            if (creditor.getMaxFinancingRateInBps() < financeRate) {
                continue;
            }

            if (Objects.isNull(result) || it.getAnnualRateInBps() < result.getKey()) {
                result = Map.entry(it.getAnnualRateInBps(), it.getPurchaser());
            }
        }
        return Optional.ofNullable(result);
    }
}
