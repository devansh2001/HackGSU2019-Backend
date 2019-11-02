package com.hackgsu2019.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddItemByCodeModel implements Serializable {
    private String id;
    private String nothing;
}