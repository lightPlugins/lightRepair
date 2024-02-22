package de.lightplugins.repair.enums;

public enum PersistentDataPaths {

    DURABILITY_VALUE("durability_add"),
    KIT_ID("repair_kit_id"),
            ;

    private String type;
    PersistentDataPaths(String type) { this.type = type; }
    public String getType() {

        return type;
    }

}
