package lu.crx.financing.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "crx")
@Getter
@Setter
@ToString
public class CrxConfig {
    @Value("${crx.pagination.invoice-page-size}")
    private int invoicePageSize;

    @Value("${crx.invoice.thread-pool-amount}")
    private int invoiceProcessThreadsAmount;

    @Value("${crx.invoice.await-sec-time}")
    private int awaitTermination;
}
