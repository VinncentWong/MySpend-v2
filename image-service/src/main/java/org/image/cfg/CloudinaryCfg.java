package org.image.cfg;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryCfg {

    @Value("${api.cloudinary.url}")
    private String cloudinaryUrl;

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(cloudinaryUrl);
    }
}
