package org.image.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.image.entity.CloudinaryResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@org.springframework.stereotype.Service
@Slf4j
public class Service implements IService{

    @Autowired
    private Cloudinary cloudinary;

    @Override
    @SneakyThrows
    public CloudinaryResp save(MultipartFile file) {
        try{
            log.info("get file: {}", file.getResource());
            var data = this
                    .cloudinary
                    .uploader()
                    .upload(file.getBytes(), ObjectUtils.asMap(
                            "folder", "earts"
                    ));
            return CloudinaryResp
                    .builder()
                    .publicId((String)data.get("public_id"))
                    .secureUrl((String)data.get("secure_url"))
                    .build();
        } catch (Exception ex){
            return null;
        }
    }
}
