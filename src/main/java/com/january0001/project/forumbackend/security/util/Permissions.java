package com.january0001.project.forumbackend.security.util;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permissions {


    private Set<String> capabilities = new HashSet<>();

    //If we ever want to check a perm, we can just slap it in there.
    public boolean canDo(String capability) {
        return capabilities != null && capabilities.contains(capability);
    }

    public static Permissions adminPermissions() {
        Set<String> caps = new HashSet<>();
        caps.add(PermissionDictionary.CAN_POST);
        caps.add(PermissionDictionary.CAN_DELETE_OWN);
        caps.add(PermissionDictionary.CAN_DELETE_OTHERS);
        caps.add(PermissionDictionary.CAN_LOCK);
        caps.add(PermissionDictionary.CAN_DELETE_THREAD);
        caps.add(PermissionDictionary.CAN_KICK);
        caps.add(PermissionDictionary.CAN_BAN);
        caps.add(PermissionDictionary.CAN_VIEW_THREADS);
        caps.add(PermissionDictionary.CAN_VIEW_ADMIN);
        caps.add(PermissionDictionary.CAN_VIEW_MODERATOR);
        caps.add(PermissionDictionary.CAN_VIEW_ADMINPANEL);
        return Permissions.builder().capabilities(caps).build();
    }

    public static Permissions userPermissions() {
        Set<String> caps = new HashSet<>();
        caps.add(PermissionDictionary.CAN_POST);
        caps.add(PermissionDictionary.CAN_DELETE_OWN);
        caps.add(PermissionDictionary.CAN_VIEW_THREADS);
        return Permissions.builder().capabilities(caps).build();
    }

    public static Permissions moderatorPermissions() {
        Set<String> caps = new HashSet<>();
        caps.add(PermissionDictionary.CAN_POST);
        caps.add(PermissionDictionary.CAN_DELETE_OWN);
        caps.add(PermissionDictionary.CAN_VIEW_THREADS);
        caps.add(PermissionDictionary.CAN_KICK);
        caps.add(PermissionDictionary.CAN_BAN);
        caps.add(PermissionDictionary.CAN_LOCK);
        caps.add(PermissionDictionary.CAN_VIEW_MODERATOR);
        return Permissions.builder().capabilities(caps).build();
    }

    //simple guest role, I don't want everyone to see the forum posts without registering. They can see announcements and rules tho.
    public static Permissions guestPermissions() {
        return Permissions.builder()
                .capabilities(Set.of(PermissionDictionary.CAN_VIEW_RESTRICTED))
                .build();
    }



}
