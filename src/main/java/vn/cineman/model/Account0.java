package vn.cineman.model;

/** Immutable value carrier; record components are private final. */
public record Account0(
    long id, String username, String passwordHash, String role, String fullName) {}
