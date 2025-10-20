package archives.tater.xom;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class XomSounds {
    private static SoundEvent register(Identifier id) {
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
    private static SoundEvent register(String path) {
        return register(Xom.id(path));
    }

    private static RegistryEntry.Reference<SoundEvent> registerReference(Identifier id) {
        return Registry.registerReference(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
    private static RegistryEntry.Reference<SoundEvent> registerReference(String path) {
        return registerReference(Xom.id(path));
    }

    public static final SoundEvent CONE_BREAK = register("block.cone.break");
    public static final SoundEvent CONE_STEP = register("block.cone.step");
    public static final SoundEvent CONE_PLACE = register("block.cone.place");
    public static final SoundEvent CONE_HIT = register("block.cone.hit");
    public static final SoundEvent CONE_FALL = register("block.cone.fall");
    public static final BlockSoundGroup CONE_SOUNDS = new BlockSoundGroup(1f, 1f, CONE_BREAK, CONE_STEP, CONE_PLACE, CONE_HIT, CONE_FALL);

    public static final RegistryEntry<SoundEvent> CONE_EQUIP = registerReference("block.cone.equip");
    public static final SoundEvent CONE_LAND = register("block.cone.land");
    public static final SoundEvent CONE_FALLS = register("block.cone.falls");
}
