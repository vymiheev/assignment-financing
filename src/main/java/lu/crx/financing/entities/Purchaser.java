package lu.crx.financing.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import lombok.*;

/**
 * Purchaser is an entity (usually a bank) that wants to purchase the invoices. I.e. it issues a loan
 * to the creditor for the term and the value of the invoice, according to the rate set up by this purchaser.
 */
@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Purchaser extends BaseEntity {

    @Basic(optional = false)
    private String name;

    /**
     * The minimum financing term (duration between the financing date and the maturity date of the invoice).
     */
    @Basic(optional = false)
    private int minimumFinancingTermInDays;

}
