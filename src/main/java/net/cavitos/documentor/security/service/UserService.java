package net.cavitos.documentor.security.service;

import net.cavitos.documentor.security.domain.UserProfile;

public interface UserService {

    UserProfile getUserProfile(String username);
}
