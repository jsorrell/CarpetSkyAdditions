package skyblock;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.settings.SettingsManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.registry.Registry;

public class SkyBlockExtension implements CarpetExtension, ModInitializer
{
    private static SettingsManager mySettingManager = new SettingsManager(Build.VERSION, Build.ID, Build.NAME);

    public SkyBlockExtension() {
        CarpetServer.manageExtension(this);
    }

    @Override
    public void onInitialize() {
        // Lets have our own settings class independent from carpet.conf
        mySettingManager.parseSettingsClass(SkyBlockSettings.class);
        Registry.register(Registry.CHUNK_GENERATOR, "skyblock", SkyblockChunkGenerator.CODEC);
    }

    @Override
    public void onGameStarted() {
    }

    @Override
    public SettingsManager customSettingsManager() {
        // this will ensure that our settings are loaded properly when world loads
        return mySettingManager;
    }
    
    @Override
    public String version()
    {
        return "carpet-skyblock 2.0.0";
    }
}
