package com.Jpalearning.jpalearning;

import com.Jpalearning.jpalearning.Entity.Team;
import com.Jpalearning.jpalearning.dto.GameplayDto;
import com.Jpalearning.jpalearning.dto.InningTeamsDto;
import com.Jpalearning.jpalearning.service.Helper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HelperTest {

    @InjectMocks
    Helper helper;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void tossTest(){
        Team team1 = Team.builder().id(1).name("IND").build();
        Team team2 = Team.builder().id(2).name("AUS").build();

        GameplayDto gameplayDto = GameplayDto.builder().team1(team1).team2(team2).build();

        InningTeamsDto inningTeamsDto = helper.toss(gameplayDto);

        Assertions.assertNotNull(inningTeamsDto.getBattingTeam());
        Assertions.assertNotNull(inningTeamsDto.getBowlingTeam());
    }
    @Test
    public void swapTeamsTest(){
        InningTeamsDto inningTeamsDto1 = new InningTeamsDto();
        Team team1 = Team.builder().id(1).name("IND").build();
        Team team2 = Team.builder().id(2).name("AUS").build();

        inningTeamsDto1.setBattingTeam(team1);
        inningTeamsDto1.setBowlingTeam(team2);

        InningTeamsDto inningTeamsDto = helper.swapTeams(inningTeamsDto1);

        Assertions.assertEquals(team1,inningTeamsDto.getBowlingTeam());
        Assertions.assertEquals(team2,inningTeamsDto.getBattingTeam());
    }
}
