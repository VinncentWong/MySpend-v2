package org.artwork.mapper;

import org.artwork.entity.Artwork;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ArtworkMapper {

    ArtworkMapper INSTANCE = Mappers.getMapper(ArtworkMapper.class);

    @Mapping(
            source = "deletedAt",
            target = "deletedAt",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
    )
    Artwork update(Artwork from, @MappingTarget Artwork to);
}
