package fr.heriamc.games.oneshot.data;

import fr.heriamc.api.HeriaAPI;
import fr.heriamc.api.data.PersistentDataManager;
import fr.heriamc.api.user.unlock.HeriaUnlockable;
import fr.heriamc.games.oneshot.cosmetic.Cosmetic;
import fr.heriamc.games.oneshot.cosmetic.CosmeticType;
import fr.heriamc.games.oneshot.cosmetic.block.BlockCosmetics;
import fr.heriamc.games.oneshot.cosmetic.kill.KillCosmetics;
import fr.heriamc.games.oneshot.cosmetic.sound.SoundCosmetics;
import fr.heriamc.games.oneshot.cosmetic.sword.SwordCosmetics;
import fr.heriamc.games.oneshot.player.OneShotPlayerTime;
import fr.heriamc.games.oneshot.player.data.OneShotPlayerData;
import lombok.Getter;

import java.util.*;

@Getter
public class OneShotDataManager extends PersistentDataManager<UUID, OneShotPlayerData> {

    private final HeriaAPI heriaAPI;
    private final Map<CosmeticType, String> defaultCosmetics;

    public OneShotDataManager(HeriaAPI heriaAPI) {
        super(heriaAPI.getRedisConnection(), heriaAPI.getMongoConnection(), "oneshot", "oneshot");
        this.heriaAPI = heriaAPI;
        this.defaultCosmetics = Map.of(
                CosmeticType.BLOCK, BlockCosmetics.CLAY.getId(),
                CosmeticType.KILL_EFFECT, KillCosmetics.NONE.getId(),
                CosmeticType.SOUND_EFFECT, SoundCosmetics.NONE.getId(),
                CosmeticType.SWORD, SwordCosmetics.WOOD.getId());
    }

    @Override
    public OneShotPlayerData getDefault() {
        return new OneShotPlayerData(null, 0, 0, 0, 0, 0, defaultCosmetics, OneShotPlayerTime.DAY);
    }

    public HeriaUnlockable getUnlockable(UUID uuid) {
        return heriaAPI.getUnlockableManager().get(uuid);
    }

    public void unlockDefault(UUID uuid) {
        var unlockable = heriaAPI.getUnlockableManager().get(uuid);

        if (!unlockable.getUnlockableData().containsKey(BlockCosmetics.CLAY.getId()))
            defaultCosmetics.values().forEach(unlockable::unlock);
    }

    public Map<CosmeticType, Set<Cosmetic>> getCosmetics(UUID uuid) {
        Map<CosmeticType, Set<Cosmetic>> unlockedCosmetics = new HashMap<>(4);
        var unlockable = heriaAPI.getUnlockableManager().get(uuid);

        for (CosmeticType type : CosmeticType.types) {
            unlockedCosmetics.put(type, new HashSet<>());
        }

        unlockable.getUnlockableData().entrySet().stream()
                .filter(entry -> entry.getKey() instanceof String id
                        && id.startsWith("oneshot")
                        && entry.getValue())
                .forEach(entry -> {
                    final var id = (String) entry.getKey();

                    switch (CosmeticType.fromId(id)) {
                        case BLOCK -> unlockedCosmetics.get(CosmeticType.BLOCK).add(BlockCosmetics.getFromId(id));
                        case KILL_EFFECT -> unlockedCosmetics.get(CosmeticType.KILL_EFFECT).add(BlockCosmetics.getFromId(id));
                        case SOUND_EFFECT -> unlockedCosmetics.get(CosmeticType.SOUND_EFFECT).add(BlockCosmetics.getFromId(id));
                        case SWORD -> unlockedCosmetics.get(CosmeticType.SWORD).add(BlockCosmetics.getFromId(id));
                    }
                });

        return unlockedCosmetics;
    }
    
}