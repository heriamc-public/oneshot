package fr.heriamc.games.oneshot.listener;

import fr.heriamc.bukkit.chat.event.HeriaChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChatListener implements Listener {

    // NEED TO ADD AN HOVER WHO MAKE EXECUTE TO THE PLAYER '/profile <name>' WHEN HE CLICK

    @EventHandler
    public void onChat(HeriaChatEvent event) {
        var player = event.getPlayer();

        //event.setCancelled(true);

    }

}