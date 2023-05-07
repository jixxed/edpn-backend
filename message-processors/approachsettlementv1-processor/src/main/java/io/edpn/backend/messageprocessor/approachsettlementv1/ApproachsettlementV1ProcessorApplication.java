package io.edpn.backend.messageprocessor.approachsettlementv1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class ApproachsettlementV1ProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApproachsettlementV1ProcessorApplication.class, args);
    }

}
