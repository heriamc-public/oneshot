package fr.heriamc.games.oneshot.gui;

import fr.heriamc.bukkit.HeriaBukkit;
import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.bukkit.menu.confirm.ConfirmMenu;
import fr.heriamc.games.oneshot.player.OneShotPlayer;

import java.util.function.Consumer;

public abstract class ConfirmPurchaseGui extends ConfirmMenu {

    protected final OneShotPlayer gamePlayer;

    public ConfirmPurchaseGui(OneShotPlayer gamePlayer, String name, HeriaMenu before, Consumer<OneShotPlayer> confirmAction) {
        super(gamePlayer.getPlayer(), name, HeriaBukkit.get(), before, player -> confirmAction.accept(gamePlayer));
        this.gamePlayer = gamePlayer;
    }

}