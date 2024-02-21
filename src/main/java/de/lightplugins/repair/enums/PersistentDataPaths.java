package de.lightplugins.repair.enums;

public enum PersistentDataPaths {

    DURABILITY_VALUE("durability_add"),
            ;

    private String type;
    PersistentDataPaths(String type) { this.type = type; }
    public String getType() {

        return type;
    }

}
