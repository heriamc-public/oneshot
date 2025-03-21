package fr.heriamc.games.oneshot.cosmetic.kill.effect;

import fr.heriamc.games.oneshot.cosmetic.kill.KillEffectTask;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import fr.heriamc.games.oneshot.utils.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.Material;

public class BloodEffect implements KillEffectTask {

    @Override
    public void run(OneShotPlayer gamePlayer, Location location) {
        var loc = location.add(0, 1, 0);
        var redstone = new ParticleEffect.ItemData(Material.REDSTONE, (byte) 0);
        var redstoneBlock = new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte) 0);

        ParticleEffect.ITEM_CRACK.display(redstone, 0.2f, 0.2f, 0.2f, 0.1f, 5, loc, 20.0);
        ParticleEffect.BLOCK_DUST.display(redstoneBlock, 0.2f, 0.2f, 0.2f, 0.1f, 5, loc, 20.0);
        ParticleEffect.ITEM_CRACK.display(redstone, 0.2f, 0.2f, 0.2f, 0.1f, 5, loc, 20.0);
        ParticleEffect.BLOCK_DUST.display(redstoneBlock, 0.2f, 0.2f, 0.2f, 0.1f, 5, loc, 20.0);
        ParticleEffect.ITEM_CRACK.display(redstone, 0.2f, 0.2f, 0.2f, 0.1f, 5, loc, 20.0);
        ParticleEffect.BLOCK_DUST.display(redstoneBlock, 0.2f, 0.2f, 0.2f, 0.1f, 5, loc, 20.0);
        ParticleEffect.ITEM_CRACK.display(redstone, 0.2f, 0.2f, 0.2f, 0.1f, 5, loc, 20.0);
        ParticleEffect.BLOCK_DUST.display(redstoneBlock, 0.2f, 0.2f, 0.2f, 0.1f, 5, loc, 20.0);
        ParticleEffect.ITEM_CRACK.display(redstone, 0.2f, 0.2f, 0.2f, 0.1f, 5, loc, 20.0);
        ParticleEffect.BLOCK_DUST.display(redstoneBlock, 0.2f, 0.2f, 0.2f, 0.1f, 5, loc, 20.0);
        ParticleEffect.ITEM_CRACK.display(redstone, 0.2f, 0.2f, 0.2f, 0.1f, 5, loc, 20.0);
        ParticleEffect.BLOCK_DUST.display(redstoneBlock, 0.2f, 0.2f, 0.2f, 0.1f, 5, loc, 20.0);
        ParticleEffect.ITEM_CRACK.display(redstone, 0.2f, 0.2f, 0.2f, 0.1f, 5, loc, 20.0);
        ParticleEffect.BLOCK_DUST.display(redstoneBlock, 0.2f, 0.2f, 0.2f, 0.1f, 5, loc, 20.0);
    }

}