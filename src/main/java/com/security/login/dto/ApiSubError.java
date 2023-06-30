package com.security.login.dto;

public record ApiSubError (String field, Object rejectedValue, String message) {}
