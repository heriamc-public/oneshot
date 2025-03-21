package fr.heriamc.games.oneshot.gui.profile;

import fr.heriamc.api.utils.HeriaSkull;
import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.bukkit.utils.ItemBuilder;
import fr.heriamc.games.engine.utils.Utils;
import fr.heriamc.games.engine.utils.gui.BaseGameGui;
import fr.heriamc.games.oneshot.OneShotGame;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import fr.heriamc.games.oneshot.player.wallet.OneShotCoins;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;

public class ProfileEditCoinsGui extends BaseGameGui<OneShotGame, OneShotPlayer> {

    private final OneShotPlayer target;
    private double value;

    /*
        NEED TO MAKE AN SUPPORT FOR OFFLINE PLAYERS
     */

    public ProfileEditCoinsGui(OneShotGame game, OneShotPlayer gamePlayer, OneShotPlayer target, HeriaMenu beforeMenu) {
        super(game, gamePlayer, beforeMenu, "Modification", 45, false);
        this.target = target;
        this.value = target == null ? gamePlayer.getPoints().getWallet() : target.getPoints().getWallet();
    }

    public ProfileEditCoinsGui(OneShotGame game, OneShotPlayer gamePlayer, HeriaMenu beforeMenu) {
        this(game, gamePlayer, null, beforeMenu);
    }

    @Override
    public void contents(Inventory inventory) {
        setBorder(inventory, DyeColor.ORANGE.getWoolData());

        insertResetButton(inventory, 4);

        insertDecreaseButton(inventory, 19, 50);
        insertDecreaseButton(inventory, 20, 10);
        insertDecreaseButton(inventory, 21, 1);

        insertSendButton(inventory, 22);

        insertIncreaseButton(inventory, 23, 1);
        insertIncreaseButton(inventory, 24, 10);
        insertIncreaseButton(inventory, 25, 50);

        insertInteractItem(inventory, 40, new ItemBuilder(Material.DARK_OAK_DOOR_ITEM)
                .setName("§c→ Fermer le menu")
                .onClick(this::closeOrOpenBefore));
    }

    private OneShotCoins getPoints() {
        return target == null ? gamePlayer.getPoints() : target.getPoints();
    }

    private void insertResetButton(Inventory inventory, int slot) {
        insertInteractItem(inventory, slot, new ItemBuilder(Material.ANVIL).setName("§7» §6Réinitialiser")
                .setLoreWithList(
                        " ",
                        "§7Permet de remettre à",
                        "§7zéro le solde de point",
                        " ",
                        "§6§l❱ §eClique pour procéder")
                .onClick(event -> {
                    if (this.value == 0) return;

                    gamePlayer.playSound(Sound.CLICK, 1f, 1f);
                    this.value = 0;
                    updateMenu();
                }));
    }

    private void insertSendButton(Inventory inventory, int slot) {
        insertInteractItem(inventory, slot, new ItemBuilder(Material.GOLD_INGOT, (int) value)
                .setName("§6Points " + (target != null ? "de " + target.getName() : ""))
                .setLoreWithList(
                        " ",
                        "§8» §7Solde actuel: §6" + getPoints().getWalletFormated(Utils.complexDecimalFormat),
                        "§8» §7Nouveaux solde: §6" + Utils.complexDecimalFormat.format(value) + " ⛃",
                        " ",
                        "§6§l❱ §eClique pour valider")
                .onClick(event -> {
                    if (getPoints() == null) {
                        gamePlayer.closeInventory();
                        gamePlayer.sendMessage("Un problème est survenue");
                        return;
                    }

                    gamePlayer.closeInventory();
                    gamePlayer.playSound(Sound.LEVEL_UP, 1f, 1f);
                    getPoints().setWallet(value);
                }));
    }

    private void insertDecreaseButton(Inventory inventory, int slot, double value) {
        insertInteractItem(inventory, slot, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .setName("§7» §cDiminuer de " + (int) value)
                .setSkullURL(HeriaSkull.RED_MINUS.getURL())
                .onClick(event -> {
                    // SECURITY CHECK XD
                    if (this.value == 0) return;

                    gamePlayer.playSound(Sound.CLICK, 1f, 1f);
                    this.value = Math.max(this.value - value, 0.0);
                    updateMenu();
                }));
    }

    private void insertIncreaseButton(Inventory inventory, int slot, double value) {
        insertInteractItem(inventory, slot, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .setName("§7» §aAugmenter de " + (int) value)
                .setSkullURL(HeriaSkull.GREEN_PLUS.getURL())
                .onClick(event -> {
                    // SECURITY CHECK XD
                    if (this.value == Double.MAX_VALUE) return;

                    gamePlayer.playSound(Sound.CLICK, 1f, 1f);
                    this.value = this.value + value;
                    updateMenu();
                }));
    }

}