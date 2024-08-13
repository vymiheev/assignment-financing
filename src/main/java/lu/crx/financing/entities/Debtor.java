package lu.crx.financing.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import lombok.*;

/**
 * A debtor is an entity that purchased some goods from the {@link Creditor}, received an {@link Invoice}
 * and is obliged to pay for the invoice at the specified date called maturity date
 * (see {@link Invoice#getMaturityDate()}).
 */
@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Debtor extends BaseEntity {

    @Basic(optional = false)
    private String name;

}
