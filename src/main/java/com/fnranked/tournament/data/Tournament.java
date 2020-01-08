package com.fnranked.tournament.data;


import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Tournament {

    @Id
    @GeneratedValue
    long tournamentId;

    @ManyToMany
    List<User> players;

    Boolean published;

    Boolean completed;

    Boolean privacy;

    String password;

    Double prizePool;

    int maxPlayers;

    Timestamp startTime;

    Timestamp creationTime;

    TournamentMode tournamentMode;

    String mapCode;

    Region region;

    double maxElo;

    double minElo;

    @ManyToOne
    User host;

    public Tournament() {
        //shouldn't be used
    }

    public Tournament(User host) {
        this.host = host;
        published = false;
        completed = false;
        privacy = false;
        prizePool = 0.0;
        maxPlayers = 8;
        creationTime = Timestamp.valueOf(LocalDateTime.now());
        tournamentMode = TournamentMode.ROUNDS;
        mapCode = "1722-4263-2279";
        region = Region.EU;
        maxElo = 1500;
        minElo = 0;
    }

    public List<User> getPlayers() {
        return players;
    }

    public Boolean getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Boolean privacy) {
        this.privacy = privacy;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getPrizePool() {
        return prizePool;
    }

    public void setPrizePool(Double prizePool) {
        this.prizePool = prizePool;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public TournamentMode getTournamentMode() {
        return tournamentMode;
    }

    public void setTournamentMode(TournamentMode tournamentMode) {
        this.tournamentMode = tournamentMode;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getMapCode() {
        return mapCode;
    }

    public void setMapCode(String mapCode) {
        this.mapCode = mapCode;
    }

    public double getMaxElo() {
        return maxElo;
    }

    public void setMaxElo(double maxElo) {
        this.maxElo = maxElo;
    }

    public double getMinElo() {
        return minElo;
    }

    public void setMinElo(double minElo) {
        this.minElo = minElo;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
