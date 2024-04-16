package moe.plushie.armourers_workshop.library.data;

import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.world.entity.Player;

public class SkinLibrarySetting {

    public static final SkinLibrarySetting DEFAULT = new SkinLibrarySetting();

    private final int flags;

    public SkinLibrarySetting() {
        this.flags = 0x0f;
    }

    public SkinLibrarySetting(Player player) {
        int flags = 0;
        SkinLibraryManager manager = SkinLibraryManager.getServer();
        if (manager.shouldUploadFile(player)) {
            flags |= 0x01;
        }
        if (manager.shouldDownloadFile(player)) {
            flags |= 0x02;
        }
        if (manager.shouldMaintenanceFile(player)) {
            flags |= 0x04;
        }
        this.flags = flags;
    }

    public SkinLibrarySetting(CompoundTag tag) {
        this.flags = tag.getInt("Flags");
    }

    public boolean allowsUpload() {
        return (flags & 0x01) != 0;
    }

    public boolean allowsDownload() {
        return (flags & 0x02) != 0;
    }

    public boolean allowsMaintenance() {
        return (flags & 0x04) != 0;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = CompoundTag.newInstance();
        tag.putInt("Flags", flags);
        return tag;
    }
}