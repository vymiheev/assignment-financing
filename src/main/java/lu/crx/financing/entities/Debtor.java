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
