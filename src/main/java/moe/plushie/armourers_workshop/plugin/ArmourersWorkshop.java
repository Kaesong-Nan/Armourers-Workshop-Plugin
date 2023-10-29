package moe.plushie.armourers_workshop.plugin;

import com.comphenix.protocol.ProtocolLibrary;
import moe.plushie.armourers_workshop.plugin.core.data.DataManager;
import moe.plushie.armourers_workshop.plugin.core.data.LocalDataService;
import moe.plushie.armourers_workshop.plugin.core.network.NetworkManager;
import moe.plushie.armourers_workshop.plugin.core.recipe.SkinningRecipes;
import moe.plushie.armourers_workshop.plugin.core.skin.SkinLoader;
import moe.plushie.armourers_workshop.plugin.init.ModBlocks;
import moe.plushie.armourers_workshop.plugin.init.ModCommands;
import moe.plushie.armourers_workshop.plugin.init.ModConfig;
import moe.plushie.armourers_workshop.plugin.init.ModEntityProfiles;
import moe.plushie.armourers_workshop.plugin.init.ModItems;
import moe.plushie.armourers_workshop.plugin.init.ModMenuTypes;
import moe.plushie.armourers_workshop.plugin.init.ModPackets;
import moe.plushie.armourers_workshop.plugin.init.handler.EntityEventHandler;
import moe.plushie.armourers_workshop.plugin.init.handler.PacketEventHandler;
import net.cocoonmc.Cocoon;
import org.bukkit.plugin.java.JavaPlugin;

public final class ArmourersWorkshop extends JavaPlugin {

    public static ArmourersWorkshop INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        ModConfig.init();
        ModPackets.init();

        ModBlocks.init();
        ModItems.init();
        ModEntityProfiles.init();
        ModMenuTypes.init();
        ModCommands.init();

        NetworkManager.init();
        SkinningRecipes.init();
        LocalDataService.start(getServer());
        DataManager.start();
        SkinLoader.getInstance().setup();

        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new EntityEventHandler(), this);
//        getServer().getPluginManager().registerEvents(new ItemEventHandler(), this);

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketEventHandler(this));


        Cocoon.getInstance().init(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        LocalDataService.stop();
        DataManager.stop();
    }
}
