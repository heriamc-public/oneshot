package fr.heriamc.games.oneshot.cosmetic;

import fr.heriamc.games.oneshot.cosmetic.block.BlockCosmetics;
import fr.heriamc.games.oneshot.cosmetic.kill.KillCosmetics;
import fr.heriamc.games.oneshot.cosmetic.sound.SoundCosmetics;
import fr.heriamc.games.oneshot.cosmetic.sword.SwordCosmetics;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CosmeticType {

    BLOCK (BlockCosmetics.class, "blocks", "Bloc"),
    KILL_EFFECT (KillCosmetics.class, "effects", "Effet"),
    SOUND_EFFECT (SoundCosmetics.class, "sounds", "Son"),
    SWORD (SwordCosmetics.class, "swords", "Épée");

    private final Class<? extends Enum<? extends Cosmetic>> cosmeticClass;
    private final String id;
    private final String displayName;

    public static final CosmeticType[] types = values();

    public static CosmeticType fromName(String name) {
        return Arrays.stream(types).filter(type -> type.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static CosmeticType fromId(String id) {
        return Arrays.stream(types).filter(type -> type.getId().equals(id)).findFirst().orElse(null);
    }

    public static Cosmetic getCosmetic(CosmeticType type, String id) {
        return switch (type) {
            case BLOCK -> BlockCosmetics.getFromIdNullable(id);
            case KILL_EFFECT -> KillCosmetics.getFromIdNullable(id);
            case SOUND_EFFECT -> SoundCosmetics.getFromIdNullable(id);
            case SWORD -> SwordCosmetics.getFromIdNullable(id);
        };
    }

    public static Cosmetic getCosmetic(String type, String id) {
        return switch (fromName(type)) {
            case BLOCK -> BlockCosmetics.getFromIdNullable(id);
            case KILL_EFFECT -> KillCosmetics.getFromIdNullable(id);
            case SOUND_EFFECT -> SoundCosmetics.getFromIdNullable(id);
            case SWORD -> SwordCosmetics.getFromIdNullable(id);
        };
    }

}