package com.january0001.project.forumbackend.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permissions {

    private Boolean manageMessages;
    private Boolean banUsers;
    private Boolean viewAuditLog;
    private Boolean manageRoles;

    //Admin perms check helper
    //Might be useful?
    public Boolean hasAnyAdminPerms() {
        return manageRoles || manageMessages || viewAuditLog;
    }

    //Default Role Builder Helper, rule of no perms unless authorized
    public static Permissions noPerms() {
        return new Permissions(false, false, false, false);
    }

    //Admin creator
    public static Permissions adminPerms() {
        return new Permissions(true, true, true, true);
    }

}
