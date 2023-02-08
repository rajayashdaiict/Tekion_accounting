package com.Cricket.com.cricketProject;

import java.util.ArrayList;
import java.util.List;

public class Team {
    public Team(String name){
        this.name=name;
        playersArray = new ArrayList<Player>(11);
        for(int i=0;i<11;i++){
            playersArray.add(new Player("Player"+Integer.toString(i+1),i));
        }
    }
    String name;
    List<Player> playersArray;


}
