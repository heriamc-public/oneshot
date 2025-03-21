package fr.heriamc.games.oneshot.gui.cosmetic;

import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.bukkit.utils.ItemBuilder;
import fr.heriamc.games.engine.utils.gui.BaseGameGui;
import fr.heriamc.games.oneshot.OneShotGame;
import fr.heriamc.games.oneshot.gui.cosmetic.sub.BlockGui;
import fr.heriamc.games.oneshot.gui.cosmetic.sub.KillEffectGui;
import fr.heriamc.games.oneshot.gui.cosmetic.sub.KillSoundGui;
import fr.heriamc.games.oneshot.gui.cosmetic.sub.SwordGui;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class CosmeticGui extends BaseGameGui<OneShotGame, OneShotPlayer> {

    public CosmeticGui(OneShotGame game, OneShotPlayer gamePlayer, HeriaMenu beforeMenu) {
        super(game, gamePlayer, beforeMenu, "Cosmétiques", 54, false);
    }

    @Override
    public void contents(Inventory inventory) {
        setBorder(inventory, DyeColor.YELLOW.getWoolData());

        inventory.setItem(4, new ItemBuilder(Material.CHEST).setName("§7» §eCosmétiques").build());

        insertInteractItem(inventory, 22, new ItemBuilder(Material.FIREWORK).setName("§7» §6Effet de kill")
                .onClick(event -> openGui(new KillEffectGui(game, gamePlayer, this))));

        insertInteractItem(inventory, 30, new ItemBuilder(Material.NOTE_BLOCK).setName("§7» §6Son de kill")
                .onClick(event -> openGui(new KillSoundGui(game, gamePlayer, this))));

        insertInteractItem(inventory, 31, new ItemBuilder(Material.STAINED_CLAY).setName("§7» §6Blocs")
                .onClick(event -> openGui(new BlockGui(game, gamePlayer, this))));

        insertInteractItem(inventory, 32, new ItemBuilder(Material.DIAMOND_SWORD).setName("§7» §6Épée").allFlags()
                .onClick(event -> openGui(new SwordGui(game, gamePlayer, this))));

        insertInteractItem(inventory, 49, new ItemBuilder(Material.DARK_OAK_DOOR_ITEM).setName("§c→ Fermer le menu")
                .onClick(this::closeOrOpenBefore));
    }

}