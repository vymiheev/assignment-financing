package lu.crx.financing.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import lombok.*;

/**
 * A creditor is a company that shipped some goods to the {@link Debtor}, issued an {@link Invoice} for the shipment
 * and is waiting for this invoice to be paid by the debtor.
 */
@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Creditor extends BaseEntity {

    @Basic(optional = false)
    private String name;

    /**
     * Maximum acceptable financing rate for this creditor.
     */
    @Basic(optional = false)
    private int maxFinancingRateInBps;

}
