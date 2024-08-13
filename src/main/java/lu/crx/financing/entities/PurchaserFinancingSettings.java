package lu.crx.financing.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Financing settings set by the purchaser for a specific creditor.
 */
@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name = "FinancingSettings.Purchaser", attributeNodes =
        {@NamedAttributeNode("purchaser"), @NamedAttributeNode("creditor")})
public class PurchaserFinancingSettings extends BaseEntity {

    @ManyToOne(optional = false)
    private Creditor creditor;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Purchaser purchaser;

    /**
     * The annual financing rate set by the purchaser for this creditor.
     */
    @Basic(optional = false)
    private int annualRateInBps;

}
