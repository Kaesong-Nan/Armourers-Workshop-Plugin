package moe.plushie.armourers_workshop.init.platform;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class EnvironmentManager {

    public static File getRootDirectory() {
        return JavaPlugin.getProvidingPlugin(EnvironmentManager.class).getDataFolder();
    }

    public static File getConfigDirectory() {
        return getRootDirectory();
    }

    public static File getSkinLibraryDirectory() {
        return new File(getRootDirectory(), "skin-library");
    }

    public static File getSkinCacheDirectory() {
        return new File(getRootDirectory(), "skin-cache");
    }

    public static File getSkinDatabaseDirectory() {
        World world = Bukkit.getServer().getWorlds().get(0);
        return new File(world.getWorldFolder(), "skin-database");
    }
}
