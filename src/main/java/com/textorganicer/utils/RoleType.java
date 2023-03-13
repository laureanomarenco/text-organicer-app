package com.textorganicer.utils;

public final class RoleType {
    public static final String COLLABORATOR = "collaborator";
    public static final String OWNER = "owner";

    private RoleType() {
        throw new IllegalStateException("Utility class");
    }
}
