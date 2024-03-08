package org.artwork.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.artwork.entity.listener.ArtworkListener;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
@Builder
@EntityListeners({
        ArtworkListener.class
})
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "stock")
    private Long stock;

    @Column(name = "photo")
    private String photo;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "dimension_x")
    private Double dimensionX;

    @Column(name = "dimension_y")
    private Double dimensionY;

    @Column(name = "dimension_z")
    private Double dimensionZ;

    @Column(name = "is_preorder")
    private Boolean isPreorder;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime deletedAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "fk_user_id")
    private Long fkUserId;

    @Column(name = "public_id_image")
    private String publicIdImage;
}
