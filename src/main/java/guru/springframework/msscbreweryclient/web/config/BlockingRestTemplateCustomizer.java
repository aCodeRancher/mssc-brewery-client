package guru.springframework.msscbreweryclient.web.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by jt on 2019-08-08.
 */
@Profile("blocking")
@Component
public class BlockingRestTemplateCustomizer implements RestTemplateCustomizer {

   private final int maxtotal ;
   private final int defaultMaxPerRoute;
   private final int connectionRequestTimeout;
   private final int socketTimeout;

    public BlockingRestTemplateCustomizer( @Value("${sfg.rest.blocking.maxtotal}")int maxtotal,
                                           @Value("${sfg.rest.blocking.defaultMaxPerRoute}") int defaultMaxPerRoute,
                                           @Value("${sfg.rest.blocking.connectionRequestTimeout}")int connectionRequestTimeout,
                                           @Value("${sfg.rest.blocking.socketTimeout}") int socketTimeout) {
        this.maxtotal = maxtotal;
        this.defaultMaxPerRoute = defaultMaxPerRoute;
        this.connectionRequestTimeout = connectionRequestTimeout;
        this.socketTimeout = socketTimeout;
    }


    public ClientHttpRequestFactory clientHttpRequestFactory(){
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(maxtotal);
        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);

        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout)
                .build();

        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setConnectionManager(connectionManager)
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setDefaultRequestConfig(requestConfig)
                .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    @Override
    public void customize(RestTemplate restTemplate) {
        restTemplate.setRequestFactory(this.clientHttpRequestFactory());
    }
}
