package fr.heriamc.games.oneshot.listener;

import fr.heriamc.api.HeriaAPI;
import fr.heriamc.games.engine.event.player.GamePlayerLeaveEvent;
import fr.heriamc.games.engine.utils.concurrent.VirtualThreading;
import fr.heriamc.games.oneshot.OneShotGame;
import fr.heriamc.games.oneshot.data.OneShotDataManager;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import fr.heriamc.games.oneshot.setting.message.OneShotMessages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public record PlayerConnectionListener(HeriaAPI heriaAPI, OneShotDataManager dataManager) implements Listener {

    @EventHandler
    public void onGamePlayerLeave(GamePlayerLeaveEvent<OneShotGame, OneShotPlayer> event) {
        var game = event.getGame();
        var gamePlayer = event.getGamePlayer();

        lastAttacker(game, gamePlayer);
        savePlayerData(gamePlayer);
    }

    private void lastAttacker(OneShotGame game, OneShotPlayer gamePlayer) {
        var lastAttacker = gamePlayer.getLastAttacker();

        if (lastAttacker == null) return;

        var attacker = game.getNullablePlayer(lastAttacker.getUuid());

        gamePlayer.onDisconnect();

        if (attacker == null) {
            VirtualThreading.execute(() -> {
                var gamePlayerData = dataManager.createOrLoad(lastAttacker.getUuid());

                gamePlayerData.updateStatsOnDisconnect();
                dataManager.save(gamePlayerData);
            });
            return;
        }

        attacker.sendMessage(OneShotMessages.KILL_KILLER_MESSAGE.getMessage(gamePlayer.getName(), "§7(Déconnexion)"));
        attacker.onKill();
    }

    private void savePlayerData(OneShotPlayer gamePlayer) {
        VirtualThreading.execute(() -> {
            var gamePlayerData = dataManager.createOrLoad(gamePlayer.getUuid());

            gamePlayerData.updateStats(gamePlayer);
            heriaAPI.getUnlockableManager().save(gamePlayer.getUnlockedCosmetics());
            dataManager.save(gamePlayerData);
            dataManager.saveInPersistant(gamePlayerData);
        });
    }

}