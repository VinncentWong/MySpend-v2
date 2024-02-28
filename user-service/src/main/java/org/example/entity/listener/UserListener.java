package org.example.entity.listener;

import centwong.utility.util.BcryptUtil;
import jakarta.persistence.PrePersist;
import org.example.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserListener {

    @PrePersist
    public void prePersist(User user){
        user.setIsActive(true);
        user.setPassword(BcryptUtil.encode(user.getPassword()));
    }
}
