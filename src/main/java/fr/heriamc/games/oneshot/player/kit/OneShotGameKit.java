package fr.heriamc.games.oneshot.player.kit;

import fr.heriamc.bukkit.utils.ItemBuilder;
import fr.heriamc.games.engine.kit.GamePlayerKit;
import fr.heriamc.games.oneshot.cosmetic.CosmeticType;
import fr.heriamc.games.oneshot.cosmetic.block.BlockCosmetic;
import fr.heriamc.games.oneshot.cosmetic.sword.SwordCosmetic;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.Arrays;
import java.util.Objects;

public class OneShotGameKit extends GamePlayerKit<OneShotPlayer> {

    private final SwordCosmetic swordCosmetic;
    private final BlockCosmetic blockCosmetic;

    public OneShotGameKit(OneShotPlayer gamePlayer) {
        super(gamePlayer);
        this.swordCosmetic = (SwordCosmetic) gamePlayer.getSelectedCosmetics().get(CosmeticType.SWORD);
        this.blockCosmetic = (BlockCosmetic) gamePlayer.getSelectedCosmetics().get(CosmeticType.BLOCK);
        setup();
    }

    @Override
    public void setup() {
        setItem(0, new ItemBuilder(swordCosmetic.getSword()).setName("§6Épée §8(§73 coups§8)")
                .setInfinityDurability().allFlags().build());

        setItem(1, new ItemBuilder(Material.BOW)
                .setName("§6Arc §8(§71 coup§8)").setInfinityDurability().allFlags().build());

        setItem(2, new ItemBuilder(Material.DIAMOND_PICKAXE)
                .setName("§6Pioche §8(§7Efficacité 4§8)")
                .addEnchant(Enchantment.DIG_SPEED, 4)
                .setInfinityDurability().flag(ItemFlag.HIDE_ATTRIBUTES)
                .flag(ItemFlag.HIDE_ENCHANTS).flag(ItemFlag.HIDE_UNBREAKABLE).build());

        setItem(3, new ItemBuilder(blockCosmetic.getMaterial(), 64).setName("§6Blocs §8(§7Illimité§8)").build());

        setItem(9, new ItemBuilder(Material.ARROW).setName("§6Flèche").build());
    }

    public void changeBlock(Material material) {
        setItem(3, new ItemBuilder(material, 64).setName("§6Blocs §8(§7Illimité§8)").build());
    }

    public void changeSword(Material material) {
        setItem(0, new ItemBuilder(material).setName("§6Épée §8(§73 coups§8)").setInfinityDurability().allFlags().build());
    }

    // REMAKE THIS
    public void addArrow() {
        if (!getGamePlayer().getPlayer().getInventory().contains(Material.ARROW))
            getInventory().entrySet().stream()
                    .filter(entry -> entry.getValue().getType().equals(Material.ARROW)).findFirst()
                    .ifPresent(entry -> getGamePlayer().getPlayer().getInventory().setItem(entry.getKey(), entry.getValue()));
        else
            Arrays.stream(getGamePlayer().getPlayer().getInventory().getContents())
                .filter(Objects::nonNull)
                .filter(itemStack -> itemStack.getType().equals(Material.ARROW))
                .findFirst().ifPresent(itemStack -> itemStack.setAmount((itemStack.getAmount() + 1) != 64 ? itemStack.getAmount() + 1 : 64));
    }

}