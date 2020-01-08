package com.fnranked.tournament.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class User {
    @Id
    private long discordID;

    private String epicID;

    private String name;

    private double boxFightElo;

    private double buildFightElo;

    protected User(){
        //Empty Constructor
    }

    public User(long discordID){
        this.discordID = discordID;
    }

    @ManyToMany
    List<Tournament> tournaments;

    public long getDiscordID() {
        return discordID;
    }

    public void setDiscordID(long discordID) {
        this.discordID = discordID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBoxFightElo() {
        return boxFightElo;
    }

    public void setBoxFightElo(double boxFightElo) {
        this.boxFightElo = boxFightElo;
    }

    public double getBuildFightElo() {
        return buildFightElo;
    }

    public void setBuildFightElo(double buildFightElo) {
        this.buildFightElo = buildFightElo;
    }
}