package vn.cineman.model;

/** Immutable value carrier; record components are private final. */
public record Registration0(
    String fullName,
    String phone,
    String email,
    String username,
    String password,
    String confirmPassword) {}
