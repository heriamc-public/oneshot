package fr.heriamc.games.oneshot;

import fr.heriamc.games.api.addon.GameAddon;
import fr.heriamc.games.oneshot.command.OneShotCommand;
import fr.heriamc.games.oneshot.data.OneShotDataManager;
import fr.heriamc.games.oneshot.listener.*;
import fr.heriamc.games.oneshot.pool.OneShotPool;
import lombok.Getter;

@Getter
public class OneShotAddon extends GameAddon<OneShotPool> {

    private OneShotDataManager dataManager;

    public OneShotAddon() {
        super(new OneShotPool());
    }

    @Override
    public void enable() {
        this.dataManager = new OneShotDataManager(heriaApi);

        pool.setDataManager(dataManager);
        pool.loadDefaultGames();

        registerListener(
                new CancelListener(this, pool.getGamesManager()),
                new PlayerConnectionListener(heriaApi, dataManager),
                new PlayerBlockListener(pool.getGamesManager()),
                new PlayerDamageListener(pool.getGamesManager()),
                new PlayerChatListener()
        );

        registerCommand(new OneShotCommand(this, pool.getGamesManager()));
    }

    @Override
    public void disable() {

    }

}