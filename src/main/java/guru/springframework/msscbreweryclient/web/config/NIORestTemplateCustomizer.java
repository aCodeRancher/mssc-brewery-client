package guru.springframework.msscbreweryclient.web.config;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.IOReactorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by jt on 2019-08-07.
 */
@Profile("nio")
@Component
public class NIORestTemplateCustomizer implements RestTemplateCustomizer {


    private final int connectionTimeout;
    private final int ioThreadCount;
    private final int soTimeout;
    private final int defaultMaxPerRoute;
    private final int maxtotal;

    public NIORestTemplateCustomizer( @Value("${sfg.rest.nio.connectTimeout}")int connectionTimeout,
                                     @Value("${sfg.rest.nio.ioThreadCount}")int ioThreadCount,
                                     @Value("${sfg.rest.nio.soTimeout}")int soTimeout,
                                     @Value("${sfg.rest.nio.defaultMaxPerRoute}")int defaultMaxPerRoute,
                                     @Value("${sfg.rest.nio.maxtotal}")int maxtotal
                                    ){
        this.connectionTimeout = connectionTimeout;
        this.ioThreadCount = ioThreadCount;
        this.soTimeout = soTimeout;
        this.defaultMaxPerRoute = defaultMaxPerRoute;
        this.maxtotal = maxtotal;

    }
    public ClientHttpRequestFactory clientHttpRequestFactory() throws IOReactorException {
        final DefaultConnectingIOReactor ioreactor = new DefaultConnectingIOReactor(IOReactorConfig.custom().
                setConnectTimeout(connectionTimeout).
                setIoThreadCount(ioThreadCount).
                setSoTimeout(soTimeout).
                build());

        final PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager(ioreactor);
        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        connectionManager.setMaxTotal(maxtotal);

        CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.custom()
                .setConnectionManager(connectionManager)
                .build();

        return new HttpComponentsAsyncClientHttpRequestFactory(httpAsyncClient);

    }

    @Override
    public void customize(RestTemplate restTemplate) {
        try {
            restTemplate.setRequestFactory(clientHttpRequestFactory());
        } catch (IOReactorException e) {
            e.printStackTrace();
        }
    }
}
