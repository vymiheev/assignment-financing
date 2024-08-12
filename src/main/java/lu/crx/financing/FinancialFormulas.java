package lu.crx.financing;

public class FinancialFormulas {

    public static final float DAYS_AMOUNT_IN_YEAR = 360f;

    public static long calcFinancialRate(int annualRate, long invoiceTerm) {
        return Math.round(annualRate * invoiceTerm / DAYS_AMOUNT_IN_YEAR);
    }

    //todo BigDecimal
    public static long getEarlyPaymentAmount(long valueInCents, long financingRate) {
        return Math.round(valueInCents * (1 - financingRate / 100f));
    }
}
