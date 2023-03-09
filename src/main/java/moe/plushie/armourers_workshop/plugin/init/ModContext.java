package moe.plushie.armourers_workshop.plugin.init;

import moe.plushie.armourers_workshop.plugin.utils.DataSerializers;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.UUID;

public class ModContext {

    private static ModContext current;

    private UUID t0;
    private UUID t1;
    private byte[] x0;
    private byte[] x1;

    public ModContext() {
        random();
        setDirty();
    }

    public ModContext(CompoundTag tag) {
        int count = 0;
        if (tag.containsKey("t0")) {
            t0 = DataSerializers.loadUUID(tag.get("t0"));
            count += 1;
        }
        if (tag.containsKey("t1")) {
            t1 = DataSerializers.loadUUID(tag.get("t1"));
            count += 1;
        }
        if (count != 2) {
            random();
            setDirty();
        }
        apply(t0, t1);
    }

    public static void init(File file) {
        // load from data.
        if (file.exists()) {
            try {
                CompoundTag contextTag = (CompoundTag) NBTUtil.read(file, true).getTag();
                contextTag = contextTag.getCompoundTag("data");
                current = new ModContext(contextTag);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // save new context into world.
        current = new ModContext();
        CompoundTag tag1 = new CompoundTag();
        CompoundTag tag = new CompoundTag();
        current.save(tag);
        tag1.put("data", tag);
        tag1.putInt("DataVersion", 3120);
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            NBTUtil.write(tag1, file, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init(UUID t0, UUID t1) {
        current = new ModContext();
        current.apply(t0, t1);
    }

    public static void reset() {
        current = null;
    }

    public static UUID t0() {
        if (current != null) {
            return current.t0;
        }
        return null;
    }

    public static UUID t1() {
        if (current != null) {
            return current.t1;
        }
        return null;
    }

    public static UUID t2(UUID p) {
        return md5(Objects.requireNonNull(t0()), p);
    }

    public static UUID t3(UUID p) {
        return md5(Objects.requireNonNull(t1()), p);
    }

    public static byte[] x0() {
        if (current != null) {
            return current.x0;
        }
        return null;
    }

    public static byte[] x1() {
        if (current != null) {
            return current.x1;
        }
        return null;
    }

//    public static String md5(String value) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            byte[] sig = md.digest(value.getBytes(StandardCharsets.UTF_8));
//            return new String(Hex.encodeHex(sig, true));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return value;
//        }
//    }

    @NotNull
    private static UUID md5(UUID v1, UUID v2) {
        int v0 = 20220616;
        ByteBuffer buffer0 = ByteBuffer.allocate(8 * Long.BYTES);
        buffer0.putLong(v0 + 0xe08e99f7);
        buffer0.putLong(v1.getLeastSignificantBits());
        buffer0.putLong(v0 + 0x9ee714d5);
        buffer0.putLong(v2.getMostSignificantBits());
        buffer0.putLong(v0 + 0x3cf6f6ac);
        buffer0.putLong(v1.getLeastSignificantBits());
        buffer0.putLong(v0 + 0x6c8caf3c);
        buffer0.putLong(v2.getLeastSignificantBits());
        return UUID.nameUUIDFromBytes(buffer0.array());
    }

    private void apply(UUID t0, UUID t1) {
        ModLog.debug("apply context");
        this.t0 = t0;
        this.t1 = t1;
        this.x0 = null;
        this.x1 = null;
        if (t0 == null || t1 == null) {
            return;
        }
        try {
            int v0 = 20220616;
            MessageDigest md = MessageDigest.getInstance("MD5");
            ByteBuffer buffer0 = ByteBuffer.allocate(8 * Long.BYTES);
            ByteBuffer buffer1 = ByteBuffer.allocate(24 * Long.BYTES);
            buffer0.putLong(v0 + 0xe08e99f7);
            buffer0.putLong(t0.getLeastSignificantBits());
            buffer0.putLong(v0 + 0x9ee714d5);
            buffer0.putLong(t0.getMostSignificantBits());
            buffer0.putLong(v0 + 0x3cf6f6ac);
            buffer0.putLong(t1.getLeastSignificantBits());
            buffer0.putLong(v0 + 0x6c8caf3c);
            x0 = md.digest(buffer0.array());
            buffer1.putLong(v0 + 0xe08e99f9);
            buffer1.putLong(t1.getMostSignificantBits());
            buffer1.putLong(t0.getLeastSignificantBits());
            buffer1.putLong(v0 + 0x9ee714d5);
            buffer1.put(x0);
            buffer1.putLong(v0 + 0x3cf6f6ac);
            buffer1.putLong(t1.getLeastSignificantBits());
            buffer1.putLong(t0.getMostSignificantBits());
            buffer1.putLong(v0 + 0x6c8caf3c);
            x1 = md.digest(buffer1.array());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void random() {
        t0 = UUID.randomUUID();
        t1 = UUID.randomUUID();
    }

    public CompoundTag save(CompoundTag nbt) {
        nbt.put("t0", DataSerializers.createUUID(t0));
        nbt.put("t1", DataSerializers.createUUID(t1));
        return nbt;
    }

    public void setDirty() {

    }
}
