package fr.heriamc.games.oneshot.gui.cosmetic;

import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.bukkit.utils.ItemBuilder;
import fr.heriamc.games.engine.utils.Utils;
import fr.heriamc.games.oneshot.cosmetic.Cosmetic;
import fr.heriamc.games.oneshot.gui.ConfirmPurchaseGui;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import fr.heriamc.games.oneshot.setting.message.OneShotMessages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;

public class ConfirmCosmeticPurchaseGui extends ConfirmPurchaseGui {

    private final Cosmetic cosmetic;

    public ConfirmCosmeticPurchaseGui(OneShotPlayer gamePlayer, Cosmetic cosmetic, HeriaMenu before) {
        super(gamePlayer, "Confirmer l'achat", before, player -> {
            gamePlayer.closeInventory();
            cosmetic.buy(gamePlayer);
            player.playSound(Sound.LEVEL_UP, 1f, 1f);

            var clickMessage = new TextComponent("§e§l[CLIQUEZ ICI]");
            clickMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/select " + cosmetic.getType().name() + " " + cosmetic.getId()));
            clickMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§eCliquez ici pour équiper !")));

            var message = new TextComponent(new TextComponent(OneShotMessages.PREFIX.getMessageWithoutPrefix()), clickMessage, new TextComponent(" §apour équiper."));

            player.sendMessage(OneShotMessages.SHOP_SUCCESSFUL_PURCHASE.getMessage(cosmetic.getName()));
            player.getCraftPlayer().spigot().sendMessage(message);
        });
        this.cosmetic = cosmetic;
    }

    @Override
    public void inventory(Inventory inventory) {
        setBorder(inventory, DyeColor.ORANGE.getWoolData());

        inventory.setItem(4, new ItemBuilder(cosmetic.getIcon())
                .setName(cosmetic.getName())
                .allFlags()
                .setLoreWithList(
                        " ",
                        "§8» §7Prix: §6" + cosmetic.getPrice() + " ⛃",
                        " ",
                        "§8» §7Solde actuel: §6 " + gamePlayer.getPoints().getWalletFormated(Utils.complexDecimalFormat),
                        "§8» §7Nouveau Solde: §6" + Utils.complexDecimalFormat.format(gamePlayer.getPoints().getWallet() - cosmetic.getPrice()) + " ⛃",
                        " ",
                        "§6§l❱ §eConfirmer votre achat")
                .build());
    }

}