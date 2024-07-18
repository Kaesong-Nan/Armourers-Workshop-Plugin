package moe.plushie.armourers_workshop.utils;

import moe.plushie.armourers_workshop.core.skin.Skin;
import moe.plushie.armourers_workshop.core.skin.SkinDescriptor;
import moe.plushie.armourers_workshop.core.skin.SkinSlotType;
import moe.plushie.armourers_workshop.core.skin.SkinWardrobe;
import moe.plushie.armourers_workshop.init.ModConfig;
import net.cocoonmc.core.item.ItemStack;
import net.cocoonmc.core.item.Items;
import net.cocoonmc.core.utils.BukkitHelper;
import net.cocoonmc.core.world.entity.Entity;
import net.cocoonmc.core.world.entity.EntityTypes;
import net.cocoonmc.core.world.entity.LivingEntity;
import net.cocoonmc.core.world.entity.Player;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.bukkit.inventory.EntityEquipment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

public final class SkinUtils {


    public static boolean shouldKeepWardrobe(Player entity, boolean keepInventory) {
        if (entity.isSpectator()) {
            return true;
        }
        // 0 = use keep inventory rule
        // 1 = never drop
        // 2 = always drop
        int keep = ModConfig.Common.prefersWardrobeDropOnDeath;
        if (keep == 1) {
            return true;
        }
        if (keep == 2) {
            return false;
        }
        return keepInventory;
    }

    public static void dropAllIfNeeded(Player player, boolean keepInventory) {
        if (SkinUtils.shouldKeepWardrobe(player, keepInventory)) {
            return; // ignore
        }
        SkinWardrobe oldWardrobe = SkinWardrobe.of(player);
        if (oldWardrobe != null) {
            oldWardrobe.dropAll(itemStack -> BukkitHelper.dropItem(itemStack, player));
            oldWardrobe.broadcast();
        }
    }


    public static void copySkinFromOwner(Entity entity) {
        Projectile projectile = ObjectUtils.safeCast(entity.asBukkit(), Projectile.class);
        if (projectile == null || (!(projectile.getShooter() instanceof org.bukkit.entity.Entity))) {
            return;
        }
        Entity owner = Entity.of((org.bukkit.entity.Entity) projectile.getShooter());
        if (projectile instanceof Trident) {
            copySkin(owner, entity, SkinSlotType.TRIDENT, 0, SkinSlotType.TRIDENT, 0);
            return;
        }
        if (projectile instanceof AbstractArrow) {
            copySkin(owner, entity, SkinSlotType.BOW, 0, SkinSlotType.BOW, 0);
            return;
        }
        // no supported projectile entity.
    }

    public static void copySkin(Entity src, Entity dest, SkinSlotType fromSlotType, int fromIndex, SkinSlotType toSlotType, int toIndex) {
        ItemStack itemStack = getSkin(src, fromSlotType, fromIndex);
        if (itemStack.isEmpty()) {
            return;
        }
        copySkin(dest, itemStack, toSlotType, toIndex);
    }

    public static void copySkin(Entity dest, ItemStack itemStack, SkinSlotType toSlotType, int toIndex) {
        SkinWardrobe wardrobe = SkinWardrobe.of(dest);
        if (wardrobe != null) {
            wardrobe.setItem(toSlotType, toIndex, itemStack.copy());
            wardrobe.broadcast();
        }
    }


    public static Skin copySkin(Skin skin) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SkinFileStreamUtils.saveSkinToStream(outputStream, skin);
        byte[] skinData = outputStream.toByteArray();
        Skin skinCopy = SkinFileStreamUtils.loadSkinFromStream(new ByteArrayInputStream(skinData));
        return skinCopy;
    }

    public static ItemStack getSkin(Entity entity, SkinSlotType slotType, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        if (entity instanceof LivingEntity) {
            itemStack = getUsingItem((LivingEntity) entity);
        }
        // embedded skin is the highest priority
        SkinDescriptor descriptor = SkinDescriptor.of(itemStack);
        if (Objects.equals(slotType.getSkinType(), descriptor.getType())) {
            return itemStack;
        }
        SkinWardrobe wardrobe = SkinWardrobe.of(entity);
        if (wardrobe != null) {
            ItemStack itemStack1 = wardrobe.getItem(slotType, index);
            descriptor = SkinDescriptor.of(itemStack1);
            if (Objects.equals(slotType.getSkinType(), descriptor.getType())) {
                return itemStack1;
            }
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack getUsingItem(LivingEntity entity) {
        EntityEquipment entityEquipment = entity.asBukkit().getEquipment();
        if (entityEquipment == null) {
            return ItemStack.EMPTY;
        }
        ItemStack itemStack = ItemStack.of(entityEquipment.getItemInMainHand());
        if (itemStack.is(Items.CROSSBOW) ) {
            return itemStack;
        }
        return ItemStack.EMPTY;
    }
}
