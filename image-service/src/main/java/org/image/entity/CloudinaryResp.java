package org.image.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CloudinaryResp {
    private String secureUrl;
    private String publicId;
}