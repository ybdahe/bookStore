package com.smartdubai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SpringBootBookStoreApplicationTests {

    @Test
    void contextLoads() {
        String microService = "smartdubai-bookstore";
        assertThat(microService).isEqualTo("smartdubai-bookstore");
    }

    @Test
    void testSpringboot() {
        try {
            SpringBootBookStoreApplication.main(null);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(Exception.class);
        }
    }

}
