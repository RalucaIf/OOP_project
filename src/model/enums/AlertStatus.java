package model.enums;

public enum AlertStatus {
    NEW,
    IN_PROGRESS,
    CLOSED;

    public String enumToString() {
        String lowercase = this.name().toLowerCase();
        String formatted = lowercase.replace('_', ' ');
        return Character.toUpperCase(formatted.charAt(0)) + formatted.substring(1);
    }
}
