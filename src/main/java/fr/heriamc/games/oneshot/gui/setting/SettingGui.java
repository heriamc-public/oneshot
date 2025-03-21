package fr.heriamc.games.oneshot.gui.setting;

import fr.heriamc.bukkit.utils.ItemBuilder;
import fr.heriamc.games.engine.utils.gui.BaseGameGui;
import fr.heriamc.games.oneshot.OneShotGame;
import fr.heriamc.games.oneshot.gui.profile.ProfileGui;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import fr.heriamc.games.oneshot.setting.message.OneShotMessages;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;

public class SettingGui extends BaseGameGui<OneShotGame, OneShotPlayer> {

    public SettingGui(OneShotGame game, OneShotPlayer gamePlayer) {
        super(game, gamePlayer, null, "Paramètres", 54, false);
    }

    @Override
    public void contents(Inventory inventory) {
        setBorder(inventory, DyeColor.YELLOW.getWoolData());

        inventory.setItem(4, new ItemBuilder(Material.REDSTONE_COMPARATOR).setName("§7» §cParamètres").build());

        insertResetStatsButton(inventory, 100, 21);
        insertTimeChangerButton(inventory, 22);
        insertEditKitButton(inventory, 23);

        insertInteractItem(inventory, 31, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner(gamePlayer.getName()).setName("§7» §6Profile")
                .setLoreWithList(
                        "",
                        "§7Visualiser vos statistiques",
                        "§7et autres informations",
                        "",
                        "§6§l❱ §eClique pour accéder"
                )
                .onClick(event -> openGui(new ProfileGui(game, gamePlayer, this))));

        insertInteractItem(inventory, 49, new ItemBuilder(Material.DARK_OAK_DOOR_ITEM).setName("§c→ Fermer le menu")
                .onClick(event -> gamePlayer.getPlayer().closeInventory()));
    }

    private void insertEditKitButton(Inventory inventory, int slot) {
        insertInteractItem(inventory, slot, new ItemBuilder(Material.DIAMOND_SWORD).setName("§7» §6Éditer votre kit")
                .allFlags()
                .setLoreWithList(
                        " ",
                        "§7Vous pouvez choisir l'emplacement",
                        "§7de vos items lors de votre",
                        "§7prochaine apparition",
                        "",
                        "§6§l❱ §eClique pour éditer")
                .onClick(event -> event.getWhoClicked().sendMessage(OneShotMessages.UNAVAILABLE_FUNCTIONALITY.getMessage())));
    }

    private void insertResetStatsButton(Inventory inventory, double price, int slot) {
        insertInteractItem(inventory, slot, new ItemBuilder(Material.PAPER).setName("§7» §6Réinitialisation des statistiques")
                .setLoreWithList(
                        " ",
                        "§7Vous perdrais toutes vos §emorts§7, vos",
                        "§ckills §7et votre record de §bkill streak",
                        "§7mais vous garderez vos §6points",
                        " ",
                        "§8» §7Prix: §6" + (int) price + " ⛃",
                        " ",
                        "§6§l❱ §eClique pour réinitialiser")
                .onClick(event -> {
                    if (gamePlayer.getPoints().has(price)) {
                        openGui(new ConfirmStatsResetGui(gamePlayer, price, this));
                    }
                }));
    }

    private void insertTimeChangerButton(Inventory inventory, int slot) {
        var currentTime = gamePlayer.getTime();
        var nextTime = currentTime.getNext();
        var previousTime = currentTime.getPrevious();

        insertInteractItem(inventory, slot, new ItemBuilder(Material.WATCH).setName("§7» §6Temps")
                .setLoreWithList(
                        " ",
                        "§7Option inutile si vous avez un",
                        "§7mod pour changer le temps",
                        " ",
                        "§8▲ Statut: " + nextTime.getDisplayName(),
                        "§a■ Statut: " + currentTime.getDisplayName(),
                        "§8▼ Statut: " + previousTime.getDisplayName(),
                        " ",
                        "§6§l❱ §eClique pour changer le temps"
                )
                .onClick(event -> {
                    gamePlayer.setTime(event.isLeftClick() ? nextTime : previousTime);
                    gamePlayer.playSound(Sound.NOTE_PLING, 10f, 10f);
                    updateMenu();
                }));
    }

}