package fr.heriamc.games.oneshot.gui.cosmetic;

import fr.heriamc.bukkit.menu.HeriaMenu;
import fr.heriamc.bukkit.utils.ItemBuilder;
import fr.heriamc.games.engine.utils.gui.BaseGamePageableGui;
import fr.heriamc.games.oneshot.OneShotGame;
import fr.heriamc.games.oneshot.cosmetic.Cosmetic;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public abstract class SubCosmeticGui<C extends Enum<C> & Cosmetic> extends BaseGamePageableGui<OneShotGame, OneShotPlayer, C> {

    private static final List<Integer> slots = Arrays.asList(20, 21, 22, 23, 24, 29, 30, 31, 32, 33);

    private final Class<C> enumClass;

    protected Filter currentFilter;

    public SubCosmeticGui(OneShotGame game, OneShotPlayer gamePlayer, Class<C> enumClass, String name, int size, boolean update, HeriaMenu beforeMenu) {
        super(game, gamePlayer, beforeMenu, name, size, update, slots, () -> List.of(enumClass.getEnumConstants()));
        this.enumClass = enumClass;
        this.currentFilter = Filter.ALL;
    }

    public abstract void setup(Inventory inventory);

    @Override
    public void inventory(Inventory inventory) {
        switch (currentFilter) {
            case ALL -> updatePagination();
            case OWNED -> updatePagination(cosmetic -> (!cosmetic.isPurchasable() && cosmetic.hasRequiredRank(gamePlayer)) || gamePlayer.getUnlockedCosmetics().isUnlocked(cosmetic.getId()));
            case NOT_OWNED -> updatePagination(cosmetic -> (cosmetic.isPurchasable() || !cosmetic.hasRequiredRank(gamePlayer)) && !gamePlayer.getUnlockedCosmetics().isUnlocked(cosmetic.getId()));
        }

        setup(inventory);
    }

    @Override
    protected ItemBuilder item(C cosmetic, int i, int i1) {
        var icon = new ItemBuilder(cosmetic.getIcon());
        List<String> lore = new ArrayList<>(5);

        if (cosmetic.isSelected(gamePlayer))
            icon.addEnchant(Enchantment.DAMAGE_ALL, 1).allFlags();

        if (!cosmetic.isPurchasable())
            lore.addAll(Arrays.asList(
                    " ",
                    "§8» §7Requis: " + cosmetic.getRequiredRank().getPrefix(),
                    " ",
                    gamePlayer.getHeriaPlayer().getRank().getPower() >= cosmetic.getRequiredRank().getPower()
                            ? "§6§l❱ §eClique pour équiper"
                            : "§6§l❱ §cVous n'avez pas le grade requis"));
        else
            lore.addAll(Arrays.asList(
                    " ",
                    gamePlayer.getUnlockedCosmetics().isUnlocked(cosmetic.getId())
                            ? "§8» §7Prix: §aPosséder"
                            : "§8» §7Prix: §6" + cosmetic.getPrice() + " ⛃",
                    "§8» §7Requis: " + cosmetic.getRequiredRank().getPrefix(),
                    " ",
                    gamePlayer.getUnlockedCosmetics().isUnlocked(cosmetic.getId())
                            ? "§6§l❱ §eClique pour équiper"
                            : "§6§l❱ §eClique pour acheter"));


        return icon.setName(cosmetic.isSelected(gamePlayer) ? cosmetic.getName() + " §a[Sélectionner]" : cosmetic.getName())
                .setLoreWithList(lore)
                .allFlags()
                .onClick(event -> {
                    if (cosmetic.hasRequiredRank(gamePlayer) && !cosmetic.isPurchasable() && !cosmetic.isSelected(gamePlayer)) {
                        cosmetic.select(gamePlayer);
                        gamePlayer.playSound(Sound.CLICK, 10f, 10f);
                        updateMenu();
                        return;
                    }

                    if (cosmetic.canSelect(gamePlayer)) {
                        cosmetic.select(gamePlayer);
                        gamePlayer.playSound(Sound.CLICK, 10f, 10f);
                        updateMenu();
                        return;
                    }

                    if (!cosmetic.has(gamePlayer) && cosmetic.canBuy(gamePlayer))
                        openGui(new ConfirmCosmeticPurchaseGui(gamePlayer, cosmetic, this));
                });
    }

    private void updatePagination() {
        getPagination().clear();
        getPagination().addAll(Arrays.asList(enumClass.getEnumConstants()));
    }

    private void updatePagination(Predicate<C> predicate) {
        getPagination().clear();
        getPagination().addAll(Arrays.stream(enumClass.getEnumConstants()).filter(predicate).toList());
    }

    protected void insertCategoryIcon(Inventory inventory, Material material, String displayName) {
        inventory.setItem(4, new ItemBuilder(material).setName(displayName).allFlags().build());
    }

    protected void insertCloseButton(Inventory inventory, int slot) {
        insertInteractItem(inventory, slot, new ItemBuilder(Material.DARK_OAK_DOOR_ITEM).setName("§c→ Fermer le menu")
                .onClick(this::closeOrOpenBefore));
    }

    protected void insertFilterButton(Inventory inventory, int slot) {
        var nextFilter = currentFilter.getNext();
        var previousFilter = currentFilter.getPrevious();

        List<String> lore = new ArrayList<>(4);

        lore.add(" ");

        for (Filter filter : Filter.filters)
            lore.add((filter == currentFilter ? "§e" : "§7") + "■ Statut: " + filter.getDisplayName());

        insertInteractItem(inventory, slot, new ItemBuilder(Material.HOPPER).setName("§7» §6Filtrer")
                .setLoreWithList(lore)
                .onClick(event -> {
                    this.currentFilter = event.isLeftClick() ? nextFilter : previousFilter;
                    gamePlayer.playSound(Sound.NOTE_PLING, 10f, 10f);
                    updateMenu();
                }));
    }

    @Getter
    @AllArgsConstructor
    protected enum Filter {

        ALL ("Tout"),
        OWNED ("Posséder"),
        NOT_OWNED ("Non posséder");

        private final String displayName;

        private static final Filter[] filters = values();

        public Filter getPrevious() {
            return filters[(ordinal() - 1  + filters.length) % filters.length];
        }

        public Filter getNext() {
            return filters[(ordinal() + 1) % filters.length];
        }

    }

}