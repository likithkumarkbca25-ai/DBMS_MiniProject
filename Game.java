package com.gamecollection;

import java.math.BigDecimal;

public class Game {
    private final int id;
    private final String title;
    private final String platform;
    private final String genre;
    private final BigDecimal price;
    private final String status;

    public Game(int id, String title, String platform, String genre, BigDecimal price, String status) {
        this.id = id;
        this.title = title;
        this.platform = platform;
        this.genre = genre;
        this.price = price;
        this.status = status;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getPlatform() { return platform; }
    public String getGenre() { return genre; }
    public BigDecimal getPrice() { return price; }
    public String getStatus() { return status; }
}
