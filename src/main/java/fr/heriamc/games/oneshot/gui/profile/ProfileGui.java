package fr.heriamc.games.oneshot.gui.profile;

import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.bukkit.utils.ItemBuilder;
import fr.heriamc.games.engine.utils.Utils;
import fr.heriamc.games.engine.utils.gui.BaseGameGui;
import fr.heriamc.games.oneshot.OneShotGame;
import fr.heriamc.games.oneshot.cosmetic.Cosmetic;
import fr.heriamc.games.oneshot.cosmetic.CosmeticType;
import fr.heriamc.games.oneshot.gui.cosmetic.CosmeticGui;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import fr.heriamc.games.oneshot.player.wallet.OneShotCoins;
import fr.heriamc.games.oneshot.setting.message.OneShotMessages;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileGui extends BaseGameGui<OneShotGame, OneShotPlayer> {

    private final OneShotPlayer target;

    public ProfileGui(OneShotGame game, OneShotPlayer gamePlayer, OneShotPlayer target, HeriaMenu beforeMenu) {
        super(game, gamePlayer, beforeMenu, "Profile " + (target == null ? "" : "de " + target.getName()), 54, false);
        this.target = target;
    }

    public ProfileGui(OneShotGame game, OneShotPlayer gamePlayer, HeriaMenu beforeMenu) {
        this(game, gamePlayer, null, beforeMenu);
    }

    @Override
    public void contents(Inventory inventory) {
        setBorder(inventory, DyeColor.PINK.getWoolData());

        inventory.setItem(4, target == null
                ? new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner(gamePlayer.getName()).setName("§7» §6Profile").build()
                : new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner(target.getName()).setName("§7» §6Profile de " + target.getName()).build());

        inventory.setItem(21, new ItemBuilder(Material.DIAMOND_SWORD)
                .setName("§6Statistiques")
                .setLoreWithList(
                        " ",
                        "§8» §7Kills: §c" + getKills() + " ⚔",
                        "§8» §7Morts: §c" + getDeaths() + " ☠",
                        " ",
                        "§8» §7KS actuel: §e" + getKillStreak(),
                        "§8» §7Meilleur KS: §a" + getBestKillStreak(),
                        " ",
                        "§8» §7Ratio: §d" + getRatio())
                .allFlags().build());

        insertPointsButton(inventory, 22);
        insertCosmeticButton(inventory, 23);

        inventory.setItem(31, new ItemBuilder(Material.PAINTING)
                .setName("§6Classement")
                .setLoreWithList(
                        " ",
                        "§8» §7Kills: §e#20",
                        "§8» §7KS: §e#15",
                        "§8» §7Points: §e#1")
                .build());

        insertInteractItem(inventory, 49, new ItemBuilder(Material.DARK_OAK_DOOR_ITEM)
                .setName("§c→ Fermer le menu")
                .onClick(this::closeOrOpenBefore));
    }

    private void insertPointsButton(Inventory inventory, int slot) {
        var icon = new ItemBuilder(Material.GOLD_INGOT);
        List<String> lore = new ArrayList<>(7);

        lore.add(" ");
        lore.add("§8» §7Points: §6" + getPoints().getWalletFormated());
        lore.add("§8» §7Solde exact: §6" + getPoints().getWalletFormated(Utils.complexDecimalFormat));

        if (target != null) {
            lore.add(" ");
            lore.add("§6§l❱ §eClique pour lui envoyer de l'argent");
        }

        if (hasRequiredRank()) {
            if (target == null) lore.add(" ");
            lore.add("§6§l❱ §eJeter pour modifier");
        }

        insertInteractItem(inventory, slot, icon.setName("§6Points")
                .setLoreWithList(lore)
                .onClick(event -> {
                    if (event.getAction().name().contains("DROP") && hasRequiredRank()) {
                        openGui(new ProfileEditCoinsGui(game, gamePlayer, target, this));
                        return;
                    }

                    if (target != null) {
                        gamePlayer.closeInventory();
                        gamePlayer.sendMessage(OneShotMessages.UNAVAILABLE_FUNCTIONALITY.getMessage());
                    }
                }));
    }

    private void insertCosmeticButton(Inventory inventory, int slot) {
        var icon = new ItemBuilder(Material.CHEST);
        List<String> lore = new ArrayList<>(7);

        lore.add(" ");

        for (CosmeticType type : CosmeticType.types) {
            var cosmetic = getSelectedCosmetics().get(type);

            lore.add("§8» §7" + type.getDisplayName() + ": " + (cosmetic == null ? "§cN/A" : cosmetic.getName()));
        }

        lore.add(" ");
        lore.add(target == null
                ? "§6§l❱ §eClique pour changer"
                : "§6§l❱ §eClique pour recopier");

        insertInteractItem(inventory, slot, icon.setName("§6Cosmétiques").setLoreWithList(lore)
                .onClick(event -> {
                    if (target == null)
                        openGui(new CosmeticGui(game, gamePlayer, this));
                    else {
                        gamePlayer.closeInventory();
                        gamePlayer.sendMessage(OneShotMessages.UNAVAILABLE_FUNCTIONALITY.getMessage());
                    }
                }));
    }

    private int getKills() {
        return target == null ? gamePlayer.getKills() : target.getKills();
    }

    private int getDeaths() {
        return target == null ? gamePlayer.getDeaths() : target.getDeaths();
    }

    private int getKillStreak() {
        return target == null ? gamePlayer.getKillStreak() : target.getKillStreak();
    }

    private int getBestKillStreak() {
        return target == null ? gamePlayer.getBestKillStreak() : target.getBestKillStreak();
    }

    private String getRatio() {
        return target == null ? gamePlayer.getRatio() : target.getRatio();
    }

    private OneShotCoins getPoints() {
        return target == null ? gamePlayer.getPoints() : target.getPoints();
    }

    private Map<CosmeticType, Cosmetic> getSelectedCosmetics() {
        return target == null ? gamePlayer.getSelectedCosmetics() : target.getSelectedCosmetics();
    }

    private boolean hasRequiredRank() {
        return gamePlayer.getHeriaPlayer().getRank().getPower() >= HeriaRank.ADMIN.getPower();
    }

}