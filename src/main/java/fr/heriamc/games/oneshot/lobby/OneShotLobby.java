package fr.heriamc.games.oneshot.lobby;

import fr.heriamc.games.engine.ffa.lobby.FFAGameLobby;
import fr.heriamc.games.engine.utils.NameTag;
import fr.heriamc.games.oneshot.OneShotGame;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import fr.heriamc.games.oneshot.setting.message.OneShotMessages;
import org.bukkit.GameMode;

public class OneShotLobby extends FFAGameLobby<OneShotGame, OneShotPlayer, OneShotLobbyItems> {

    public OneShotLobby() {
        super(OneShotLobbyItems.class);
    }

    // FIRST JOIN
    @Override
    protected void processJoin(OneShotGame game, OneShotPlayer gamePlayer) {
        var rank = gamePlayer.getHeriaPlayer().getRank();

        gamePlayer.setGameMode(GameMode.ADVENTURE);
        gamePlayer.setTime(gamePlayer.getTime());

        NameTag.setNameTag(gamePlayer.getPlayer(), rank.getPrefix(), " ", rank.getTabPriority());
        OneShotMessages.WELCOME_TITLE.sendAsTitle(gamePlayer, 20, 30, 20);
        gamePlayer.sendMessage(OneShotMessages.WELCOME_MESSAGE.getMessages());
    }

    // WHEN PLAYER DIE AND RETURN TO THE LOBBY
    @Override
    protected void processSetup(OneShotGame game, OneShotPlayer gamePlayer) {}

    // WHEN PLAYER ENTER THE ARENA
    @Override
    protected void processPlay(OneShotGame game, OneShotPlayer gamePlayer) {
        gamePlayer.setGameMode(GameMode.SURVIVAL);
        gamePlayer.cleanUp();
        gamePlayer.getKit().giveKit();

        game.getSettings().getGameMapManager().getSpawnPoints().randomTeleport(gamePlayer);
    }

}