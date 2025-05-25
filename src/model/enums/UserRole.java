package model.enums;

public enum UserRole {
    ADMIN,
    REGULAR_USER,
    HACKER;

    public String enumToString() {
        String lowercase = this.name().toLowerCase();
        String formatted = lowercase.replace('_', ' ');
        return Character.toUpperCase(formatted.charAt(0)) + formatted.substring(1);
    }
}
