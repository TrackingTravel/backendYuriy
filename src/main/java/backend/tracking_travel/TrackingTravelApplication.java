package backend.tracking_travel;

import backend.tracking_travel.config.StorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Date;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class TrackingTravelApplication {
    static final Logger log = LoggerFactory.getLogger(TrackingTravelApplication.class);

    public static void main(String[] args) {
        log.info("Начало сборки контекста " + new Date());
        SpringApplication.run(TrackingTravelApplication.class, args);
        log.info("Начало работы приложения " + new Date());
    }

}
