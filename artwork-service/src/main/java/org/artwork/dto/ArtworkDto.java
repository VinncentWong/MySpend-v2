package org.artwork.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.artwork.entity.Artwork;

public class ArtworkDto {

    public record Create(
            @NotNull(message = "description can't null")
            @NotBlank(message = "description can't blank")
            String description,

            @NotNull(message = "category can't null")
            @NotBlank(message = "category can't blank")
            String category,

            @NotNull(message = "stock can't null")
            @PositiveOrZero(message = "stock should be >= 0")
            Long stock,

            @NotNull(message = "weight can't null")
            @PositiveOrZero(message = "weight should be >= 0")
            Double weight,

            @NotNull(message = "dimensionX can't null")
            @PositiveOrZero(message = "dimensionX should be >= 0")
            Double dimensionX,

            @NotNull(message = "dimensionY can't null")
            @PositiveOrZero(message = "dimensionY should be >= 0")
            Double dimensionY,

            @NotNull(message = "dimensionZ can't null")
            @PositiveOrZero(message = "dimensionZ should be >= 0")
            Double dimensionZ,

            @NotNull(message = "isPreorder can't null")
            Boolean isPreorder
    ){
            public Artwork toArtwork(){
                    return Artwork
                            .builder()
                            .category(category)
                            .description(description)
                            .dimensionX(dimensionX)
                            .dimensionY(dimensionY)
                            .dimensionZ(dimensionZ)
                            .isPreorder(isPreorder)
                            .stock(stock)
                            .weight(weight)
                            .build();
            }
    }
}
