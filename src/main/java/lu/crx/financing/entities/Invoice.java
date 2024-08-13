package lu.crx.financing.entities;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static jakarta.persistence.ConstraintMode.NO_CONSTRAINT;

/**
 * An invoice issued by the {@link Creditor} to the {@link Debtor} for shipped goods.
 */
@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice extends BaseEntity {

    /**
     * Creditor is the entity that issued the invoice.
     */
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Creditor creditor;

    /**
     * Debtor is the entity obliged to pay according to the invoice.
     */
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Debtor debtor;

    /**
     * Maturity date is the date on which the {@link #debtor} is to pay for the invoice.
     * In case the invoice was financed, the money will be paid in full on this date to the purchaser of the invoice.
     */
    @Basic(optional = false)
    private LocalDate maturityDate;

    /**
     * The value is the amount to be paid for the shipment by the Debtor.
     */
    @Basic(optional = false)
    private long valueInCents;

    // BEGIN MY UPDATES

    @Basic(optional = false)
    private boolean financed;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Purchaser purchaser;

    @Basic
    private long financingTermInDays;

    @Basic
    private long financingRate;

    //todo to BigDecimal
    @Basic
    private long earlyPaymentAmount;

    @Basic
    private LocalDate financingDate;
}
