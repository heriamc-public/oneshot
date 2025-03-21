package fr.heriamc.games.oneshot.gui.cosmetic.sub;

import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.games.oneshot.OneShotGame;
import fr.heriamc.games.oneshot.cosmetic.kill.KillCosmetics;
import fr.heriamc.games.oneshot.gui.cosmetic.SubCosmeticGui;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class KillEffectGui extends SubCosmeticGui<KillCosmetics> {

    public KillEffectGui(OneShotGame game, OneShotPlayer gamePlayer, HeriaMenu beforeMenu) {
        super(game, gamePlayer, KillCosmetics.class, "Effet de kill", 54, false, beforeMenu);
    }

    @Override
    public void setup(Inventory inventory) {
        setBorder(inventory, DyeColor.CYAN.getWoolData());
        insertCategoryIcon(inventory, Material.FIREWORK, "§7» §6Effet de kill");

        insertFilterButton(inventory, 48);
        insertCloseButton(inventory, 47);
    }

}