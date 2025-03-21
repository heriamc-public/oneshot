package fr.heriamc.games.oneshot;

import fr.heriamc.api.game.size.GameSizeTemplate;
import fr.heriamc.games.engine.ffa.FFAGame;
import fr.heriamc.games.oneshot.cosmetic.Cosmetic;
import fr.heriamc.games.oneshot.cosmetic.CosmeticType;
import fr.heriamc.games.oneshot.cosmetic.block.BlockCosmetics;
import fr.heriamc.games.oneshot.cosmetic.kill.KillCosmetics;
import fr.heriamc.games.oneshot.cosmetic.sound.SoundCosmetics;
import fr.heriamc.games.oneshot.cosmetic.sword.SwordCosmetics;
import fr.heriamc.games.oneshot.data.OneShotDataManager;
import fr.heriamc.games.oneshot.lobby.OneShotLobby;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import fr.heriamc.games.oneshot.setting.OneShotGameSettings;
import fr.heriamc.games.oneshot.setting.OneShotMapManager;
import fr.heriamc.games.oneshot.setting.message.OneShotMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class OneShotGame extends FFAGame<OneShotPlayer, OneShotGameSettings, OneShotDataManager> {

    private final OneShotMessage message;

    public OneShotGame(OneShotDataManager dataManager) {
        super("oneshot", new OneShotGameSettings(GameSizeTemplate.FFA.toGameSize()), dataManager, new OneShotLobby());
        this.settings.setGameMapManager(new OneShotMapManager(this));
        this.message = new OneShotMessage(this);
    }

    @Override
    public void load() {
        settings.getGameMapManager().setup();
        message.setup();
    }

    @Override
    public OneShotPlayer defaultGamePlayer(UUID uuid, boolean spectator) {
        var data = dataManager.createOrLoad(uuid);
        Map<CosmeticType, Cosmetic> selectedCosmetics = new HashMap<>();

        dataManager.getUnlockable(uuid);
        dataManager.unlockDefault(uuid);

        data.getSelectedCosmetics().forEach((type, id) -> selectedCosmetics.put(type, switch (type) {
            case BLOCK -> BlockCosmetics.getFromId(id);
            case KILL_EFFECT -> KillCosmetics.getFromId(id);
            case SOUND_EFFECT -> SoundCosmetics.getFromId(id);
            case SWORD -> SwordCosmetics.getFromId(id);
        }));

        return new OneShotPlayer(uuid,
                data.getKills(), data.getDeaths(),
                data.getKillStreak(), data.getBestKillStreak(),
                data.getPoints(),
                dataManager.getUnlockable(uuid), selectedCosmetics,
                data.getTime(),
                spectator);
    }

}