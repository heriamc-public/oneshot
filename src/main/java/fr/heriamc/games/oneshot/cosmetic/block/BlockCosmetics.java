package fr.heriamc.games.oneshot.cosmetic.block;

import fr.heriamc.api.user.rank.HeriaRank;
import fr.heriamc.games.oneshot.cosmetic.CosmeticType;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum BlockCosmetics implements BlockCosmetic {

    CLAY ("oneshot.blocks.clay", "§7Bloc d'argile", Material.STAINED_CLAY, 0, HeriaRank.PLAYER, false),

    BRICK ("oneshot.blocks.brick", "§cBloc de brique", Material.BRICK, 1000, HeriaRank.PLAYER, true),
    ICE ("oneshot.blocks.ice", "§9Bloc de glace", Material.PACKED_ICE, 100, HeriaRank.PLAYER, true),
    SANDSTONE ("oneshot.blocks.sandstone", "§eBloc de sandstone", Material.SANDSTONE, 100, HeriaRank.PLAYER, true),

    IRON ("oneshot.blocks.iron", "§fBloc de fer", Material.IRON_BLOCK, 1000, HeriaRank.VIP, false),
    GOLD ("oneshot.blocks.gold", "§eBloc d'or", Material.GOLD_BLOCK, 1000, HeriaRank.VIP_PLUS, false),
    EMERALD ("oneshot.blocks.emerald", "§aBloc d'émeraude", Material.EMERALD_BLOCK, 0, HeriaRank.SUPER_VIP, false),
    DIAMOND ("oneshot.blocks.diamond", "§bBloc de diamant", Material.DIAMOND_BLOCK, 0, HeriaRank.SUPREME, false);

    private final String id, name;
    private final Material material;

    private int price;
    private HeriaRank requiredRank;
    private boolean purchasable;

    public static final List<BlockCosmetics> blocks = Arrays.asList(values());

    public static BlockCosmetic getFromId(String id) {
        return blocks.stream().filter(block -> block.getId().equals(id)).findFirst().orElse(CLAY);
    }

    public static BlockCosmetic getFromIdNullable(String id) {
        return blocks.stream().filter(block -> block.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Material getIcon() {
        return material;
    }

    @Override
    public CosmeticType getType() {
        return CosmeticType.BLOCK;
    }

    @Override
    public void select(OneShotPlayer gamePlayer) {
        gamePlayer.getSelectedCosmetics().put(CosmeticType.BLOCK, this);
        gamePlayer.getKit().changeBlock(material);
    }

    @Override
    public void deselect(OneShotPlayer gamePlayer) {
        gamePlayer.getSelectedCosmetics().put(CosmeticType.BLOCK, CLAY);
        gamePlayer.getKit().changeBlock(CLAY.material);
    }

    @Override
    public void buy(OneShotPlayer gamePlayer) {
        gamePlayer.getUnlockedCosmetics().unlock(id);
        gamePlayer.getPoints().remove((double) price);
    }

    @Override
    public boolean has(OneShotPlayer gamePlayer) {
        return gamePlayer.getUnlockedCosmetics().getUnlockableData().getOrDefault(id, false);
    }

    @Override
    public boolean hasRequiredRank(OneShotPlayer gamePlayer) {
        return gamePlayer.getHeriaPlayer().getRank().getPower() >= requiredRank.getPower();
    }

    @Override
    public boolean isSelected(OneShotPlayer gamePlayer) {
        return gamePlayer.hasSelected(CosmeticType.BLOCK, this);
    }

    @Override
    public boolean canSelect(OneShotPlayer gamePlayer) {
        return has(gamePlayer) && !isSelected(gamePlayer);
    }

    @Override
    public boolean canBuy(OneShotPlayer gamePlayer) {
        return purchasable && gamePlayer.getPoints().getWallet() >= price;
    }

    @Override
    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public void setRequiredRank(HeriaRank requiredRank) {
        this.requiredRank = requiredRank;
    }

    @Override
    public void setPurchasable(boolean purchasable) {
        this.purchasable = purchasable;
    }

}