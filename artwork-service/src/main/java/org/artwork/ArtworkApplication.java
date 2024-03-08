package org.artwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "org.artwork"
        }
)
public class ArtworkApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArtworkApplication.class, args); 
    }
}