package com.hackgsu2019.backend.model;

public enum Category {
    HEADPHONE("headphone"),
    COFFEE("coffee"),
    JACKETS("jackets"),
    CHAIR("chair");

    private String name;
    Category(String name) {
        this.name = name;
    }
}
