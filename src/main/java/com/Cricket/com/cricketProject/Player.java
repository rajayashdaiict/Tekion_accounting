package com.Cricket.com.cricketProject;

public class Player implements Comparable<Player> {
    public Player(String name, int id){
        this.name=name;
        this.id=id;
    }
    public Player(String name){
        this.name=name;
    }
    String name;
    Integer id;

    @Override
    public int compareTo(Player o) {
        return id-o.id;
    }
}
