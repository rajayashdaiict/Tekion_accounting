package com.Jpalearning.jpalearning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDto {
    private int id;
    private String name;
    private List<Integer> currentPlayerIds;
    private List<Integer> allPlayerIds;
    @Builder.Default
    private boolean isDeleted=false;

}
