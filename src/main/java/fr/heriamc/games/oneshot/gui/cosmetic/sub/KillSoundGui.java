package fr.heriamc.games.oneshot.gui.cosmetic.sub;

import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.games.oneshot.OneShotGame;
import fr.heriamc.games.oneshot.cosmetic.sound.SoundCosmetics;
import fr.heriamc.games.oneshot.gui.cosmetic.SubCosmeticGui;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class KillSoundGui extends SubCosmeticGui<SoundCosmetics> {

    public KillSoundGui(OneShotGame game, OneShotPlayer gamePlayer, HeriaMenu beforeMenu) {
        super(game, gamePlayer, SoundCosmetics.class, "Son de kill", 54, false, beforeMenu);
    }

    @Override
    public void setup(Inventory inventory) {
        setBorder(inventory, DyeColor.MAGENTA.getWoolData());
        insertCategoryIcon(inventory, Material.NOTE_BLOCK, "§7» §6Son de kill");

        insertFilterButton(inventory, 48);
        insertCloseButton(inventory, 47);
    }

}