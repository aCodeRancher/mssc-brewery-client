package guru.springframework.msscbreweryclient.web.client;

import guru.springframework.msscbreweryclient.web.model.CustomerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

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
}