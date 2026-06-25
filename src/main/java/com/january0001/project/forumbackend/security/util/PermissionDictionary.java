package com.january0001.project.forumbackend.security.util;

public class PermissionDictionary {
    public static final String CAN_POST = "post:create";
    public static final String CAN_DELETE_OWN = "post:delete_own";
    public static final String CAN_DELETE_OTHERS = "post:delete_others";
    public static final String CAN_BAN = "user:ban";
    public static final String CAN_KICK = "user:kick";
    public static final String CAN_LOCK = "thread:lock";
    public static final String CAN_DELETE_THREAD = "thread:delete";
    public static final String CAN_VIEW_RESTRICTED = "view:everyone";
    public static final String CAN_VIEW_THREADS = "view:registered";
    public static final String CAN_VIEW_ADMIN = "view:admin";
    public static final String CAN_VIEW_MODERATOR = "view:moderator";
    public static final String CAN_VIEW_ADMINPANEL = "view:adminpanel";
}
