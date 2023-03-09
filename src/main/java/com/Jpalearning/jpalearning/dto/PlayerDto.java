package com.Jpalearning.jpalearning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerDto {
    private int id;
    private String name;
    private String teamName;
    private boolean isDeleted;
}
