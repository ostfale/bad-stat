package de.ostfale.qk.parser.player;

public class PlayerDTO {

    public String name;
    public String id;

    public PlayerDTO(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public PlayerDTO(String name) {
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
