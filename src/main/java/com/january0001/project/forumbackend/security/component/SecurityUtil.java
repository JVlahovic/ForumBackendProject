package com.january0001.project.forumbackend.security.component;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class SecurityUtil {

    //I was using this across multiple components, so I made a utility for it to be reusable where needed.
    public Boolean hasModOrAdminAuthority(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
            return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MODERATOR"));
    }

    //more broad implementation used for threads and thread categories for access. gonna check later if I can maybe just use this given the accessControl string.
    public Boolean hasCategoryAccess(String accessCtrl, Authentication authentication) {
        if (accessCtrl == null) {
            return false;
        }
        if ("view:everyone".equals(accessCtrl))
        {
            return true;
        }
        return authentication != null && authentication.isAuthenticated() && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(accessCtrl));
    }

    public boolean hasPostAccess(String postCtrl, Authentication authentication) {
        if (postCtrl == null) {
            return false;
        }
        return authentication != null && authentication.isAuthenticated() && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(postCtrl));
    }

    //this one has a TON of usages, and will likely have a ton more. Absolute lifesaver.
    //Note to self: custom message compatibility would be sick as hell here. At the end, check if you can make that (should be ez)
    public void requireAuthentication(Authentication authentication) {
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication is required for this resource!");
        }
    }

}
