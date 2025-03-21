package fr.heriamc.games.oneshot.gui.cosmetic.sub;

import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.games.oneshot.OneShotGame;
import fr.heriamc.games.oneshot.cosmetic.block.BlockCosmetics;
import fr.heriamc.games.oneshot.gui.cosmetic.SubCosmeticGui;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class BlockGui extends SubCosmeticGui<BlockCosmetics> {

    public BlockGui(OneShotGame game, OneShotPlayer gamePlayer, HeriaMenu beforeMenu) {
        super(game, gamePlayer, BlockCosmetics.class, "Blocs", 54, false, beforeMenu);
    }

    @Override
    public void setup(Inventory inventory) {
        setBorder(inventory, DyeColor.ORANGE.getWoolData());
        insertCategoryIcon(inventory, Material.STAINED_CLAY, "§7» §6Blocs");

        insertFilterButton(inventory, 48);
        insertCloseButton(inventory, 47);
    }

}