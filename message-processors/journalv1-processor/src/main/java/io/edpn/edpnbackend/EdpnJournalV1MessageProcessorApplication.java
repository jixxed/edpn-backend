package io.edpn.edpnbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class EdpnJournalV1MessageProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdpnJournalV1MessageProcessorApplication.class, args);
    }

}
