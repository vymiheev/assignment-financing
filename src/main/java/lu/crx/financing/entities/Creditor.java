package lu.crx.financing.entities;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
public class Creditor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Basic(optional = false)
    private String name;

    /**
     * Maximum acceptable financing rate for this creditor.
     */
    @Basic(optional = false)
    private int maxFinancingRateInBps;

}
