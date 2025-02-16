package de.ostfale.qk.parser.player;

public class PlayerRawModel {

    public String name;
    public String id;

    public PlayerRawModel(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public PlayerRawModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
