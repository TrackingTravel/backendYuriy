package backend.tracking_travel;

import backend.tracking_travel.config.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class TrackingTravelApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackingTravelApplication.class, args);
    }

}
