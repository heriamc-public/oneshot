package fr.heriamc.games.oneshot.gui.setting;

import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.bukkit.utils.ItemBuilder;
import fr.heriamc.games.engine.utils.Utils;
import fr.heriamc.games.oneshot.gui.ConfirmPurchaseGui;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import fr.heriamc.games.oneshot.setting.message.OneShotMessages;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;

public class ConfirmStatsResetGui extends ConfirmPurchaseGui {

    private final double price;

    public ConfirmStatsResetGui(OneShotPlayer gamePlayer, double price, HeriaMenu before) {
        super(gamePlayer, "Confirmer la réinitialisation", before, player -> {
            player.closeInventory();

            player.getPoints().remove(price);
            player.reset();

            player.playSound(Sound.LEVEL_UP, 1f, 1f);
            player.sendMessage(OneShotMessages.SETTINGS_SUCCESSFUL_RESET_STATS.getMessage("§7(-" + (int) price + " ⛃)"));
        });
        this.price = price;
    }

    @Override
    public void inventory(Inventory inventory) {
        setBorder(inventory, DyeColor.ORANGE.getWoolData());

        inventory.setItem(4, new ItemBuilder(Material.PAPER)
                .setName("§6Réinitialisation des statistiques")
                .setLoreWithList(
                        " ",
                        "§8» §7Prix: §6" + (int) price + " ⛃",
                        " ",
                        "§8» §7Solde actuel: §6 " + gamePlayer.getPoints().getWalletFormated(Utils.complexDecimalFormat),
                        "§8» §7Nouveau Solde: §6" + Utils.complexDecimalFormat.format(gamePlayer.getPoints().getWallet() - price) + " ⛃",
                        " ",
                        "§6§l❱ §eConfirmer votre achat")
                .build());
    }

}