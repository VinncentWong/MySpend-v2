package org.artwork.entity.listener;

import jakarta.persistence.PrePersist;
import org.artwork.entity.Artwork;
import org.springframework.stereotype.Component;

@Component
public class ArtworkListener {

    @PrePersist
    public void prePersist(Artwork artwork){
        artwork.setIsActive(true);
    }
}
