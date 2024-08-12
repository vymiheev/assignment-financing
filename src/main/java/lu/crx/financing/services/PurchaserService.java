package lu.crx.financing.services;

import lu.crx.financing.entities.Creditor;
import lu.crx.financing.entities.Purchaser;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public interface PurchaserService {

    Optional<Map.Entry<Long, Purchaser>> choosePurchaser(@NotNull Creditor creditor, long financingTerm);
}
