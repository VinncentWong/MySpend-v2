package org.image.service;

import org.image.entity.CloudinaryResp;
import org.springframework.web.multipart.MultipartFile;

public interface IService {
    CloudinaryResp save(MultipartFile file);
}
