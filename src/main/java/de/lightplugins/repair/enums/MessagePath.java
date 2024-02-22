package de.lightplugins.repair.enums;

import org.bukkit.configuration.file.FileConfiguration;
import de.lightplugins.repair.master.Main;

public enum MessagePath {

    Prefix("prefix"),
    NoPermission("noPermission"),
    PlayerNotFound("playerNotFound"),
    WrongCommand("wrongCommand"),
    KitNotFound("kitNotFound"),
    GetKitSuccess("getKitSuccess"),
    GetKitInvFull("getKitInvFull"),
    OnSuccessRepair("onSuccessRepair"),
    AlreadyFullRepaired("alreadyFullRepaired"),
    Reload("reload"),
    ;

    private final String path;

    MessagePath(String path) { this.path = path; }
    public String getPath() {
        FileConfiguration paths = Main.messages.getConfig();
        try {
            return paths.getString(this.path);
        } catch (Exception e) {
            throw new RuntimeException("Error occured on Message creation", e);
        }
    }
}
