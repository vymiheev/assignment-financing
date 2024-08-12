package lu.crx.financing.repository;

import lu.crx.financing.entities.Creditor;
import lu.crx.financing.entities.Purchaser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaserRepository extends JpaRepository<Purchaser, Long> {

   /* @Query("SELECT p FROM Purchaser p JOIN p.financingSettings fs WHERE fs.creditor.id = :creditorId")
    List<Purchaser> findPurchasersByCreditorId(@Param("creditorId") Long creditorId);

    @Query("SELECT p FROM Purchaser p JOIN p.financingSettings fs WHERE fs.creditor = :creditor")
    List<Purchaser> findPurchasersByCreditor(Creditor creditor);*/

    @Query("SELECT fs.purchaser FROM PurchaserFinancingSettings fs WHERE fs.creditor = :creditor")
    List<Purchaser> findPurchasersByCreditor(Creditor creditor);

}