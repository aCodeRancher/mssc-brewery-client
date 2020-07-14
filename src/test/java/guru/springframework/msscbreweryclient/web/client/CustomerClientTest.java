package guru.springframework.msscbreweryclient.web.client;

import guru.springframework.msscbreweryclient.web.model.BeerDto;
import guru.springframework.msscbreweryclient.web.model.CustomerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerClientTest {
   @Autowired
    CustomerClient client;

   @Test
    public void getCustomerId() {
        ResponseEntity<CustomerDto> customerDtoResponseEntity = client.getCustomerById(UUID.randomUUID());
        assertNotNull(customerDtoResponseEntity);
        assertTrue(customerDtoResponseEntity.getBody().getName().equals("Joe Buck"));

   }

    @Test
    void saveNewCustomer() {

        CustomerDto customerDto = CustomerDto.builder().name("Nancy Newton").build();

        ResponseEntity<CustomerDto> customerDtoResponseEntity= client.saveNewCustomer(customerDto);
        assertNotNull(customerDtoResponseEntity);
        assertTrue(customerDtoResponseEntity.getBody().getName().equals("Nancy Newton"));
        assertTrue(customerDtoResponseEntity.getBody().getId()!=null);

    }

    @Test
    public void updateCustomer(){
        UUID id = UUID.randomUUID();
        CustomerDto customerDto = CustomerDto.builder().name("Like To Change").build();
        ResponseEntity<CustomerDto> customerDtoResponseEntity = client.updateCustomer(id, customerDto);
        assertTrue(customerDtoResponseEntity.getBody().getName().equals("Like To Change"));
    }
}