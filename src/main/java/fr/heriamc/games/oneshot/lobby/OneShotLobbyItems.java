package fr.heriamc.games.oneshot.lobby;

import fr.heriamc.bukkit.utils.ItemBuilder;
import fr.heriamc.games.engine.ffa.lobby.FFAGameLobbyItems;
import fr.heriamc.games.engine.utils.func.TriConsumer;
import fr.heriamc.games.oneshot.OneShotAddon;
import fr.heriamc.games.oneshot.OneShotGame;
import fr.heriamc.games.oneshot.gui.cosmetic.CosmeticGui;
import fr.heriamc.games.oneshot.gui.setting.SettingGui;
import fr.heriamc.games.oneshot.player.OneShotPlayer;
import fr.heriamc.games.oneshot.setting.message.OneShotMessages;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum OneShotLobbyItems implements FFAGameLobbyItems {

    PLAY (0,
            new ItemBuilder(Material.IRON_AXE).setName("§6Jouer§8・§7Clic droit").setInfinityDurability().allFlags().build(),
            (addon, game, gamePlayer) -> game.play(gamePlayer)),

    SPECTATE (1,
            new ItemBuilder(Material.FEATHER).setName("§fSpectateur§8・§7Clic droit").build(),
            (addon, game, gamePlayer) -> gamePlayer.sendMessage(OneShotMessages.UNAVAILABLE_FUNCTIONALITY.getMessage())),

    COSMETICS (4,
            new ItemBuilder(Material.CHEST).setName("§eCosmétiques§8・§7Clic droit").build(),
            (addon, game, gamePlayer) -> addon.openGui(new CosmeticGui(game, gamePlayer, null))),

    SETTINGS (7,
            new ItemBuilder(Material.REDSTONE_COMPARATOR).setName("§cParamètres§8・§7Clic droit").build(),
            (addon, game, gamePlayer) -> addon.openGui(new SettingGui(game, gamePlayer))),

    LEAVE (8, new ItemBuilder(Material.BARRIER).setName("§cQuitter§8・§7Clic droit").build(),
            (addon, game, gamePlayer) -> addon.redirectToHub(gamePlayer));

    private final int slot;
    private final ItemStack itemStack;
    private final TriConsumer<OneShotAddon, OneShotGame, OneShotPlayer> consumer;

    private static final List<OneShotLobbyItems> items = Arrays.asList(values());

    public static Optional<OneShotLobbyItems> getLobbyItem(ItemStack itemStack) {
        return items.stream().filter(lobbyItems -> lobbyItems.getItemStack().equals(itemStack)).findFirst();
    }

}