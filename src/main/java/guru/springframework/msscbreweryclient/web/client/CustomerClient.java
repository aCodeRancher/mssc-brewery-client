package guru.springframework.msscbreweryclient.web.client;

import guru.springframework.msscbreweryclient.web.model.CustomerDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@ConfigurationProperties(prefix="sfg.brewery", ignoreUnknownFields=false)
@Component
public class CustomerClient {
    public final String CUSTOMER_PATH_V1 = "/api/v1/customer/";
    private String apihost;

    private final RestTemplate restTemplate;

    public CustomerClient(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }

    public ResponseEntity<CustomerDto> getCustomerById(UUID uuid){
        String path = CUSTOMER_PATH_V1+"/{customerId}";
        return restTemplate.getForEntity(apihost + path, CustomerDto.class, uuid.toString());
    }

    public ResponseEntity<CustomerDto> saveNewCustomer(CustomerDto customerDto){
         return restTemplate.postForEntity(apihost + CUSTOMER_PATH_V1 + "/add",customerDto, CustomerDto.class);
    }
    public void setApihost(String apihost) {
        this.apihost = apihost;
    }

}
