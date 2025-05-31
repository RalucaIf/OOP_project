package model;

import java.time.LocalDateTime;

public record Event(String type, Device source, LocalDateTime time) {}
