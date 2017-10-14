package ml.bssentials.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import ml.bssentials.main.Bssentials;
import ml.bssentials.main.Perms;

/**
 * The class that controls the /plugins and /pl (also the /plugins ver and /pl
 * ver)
 */
public class CommandLis implements Listener {
    public Bssentials main;
    public CommandLis(Bssentials bss) {
        main = bss;
    }

    @EventHandler
    public void pluginCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String plnoperm = ChatColor.GREEN + "[Bssentials] " + ChatColor.GOLD + "You don't have permission to use that command!";
        if(event.getMessage().equalsIgnoreCase("/plugins") || event.getMessage().equalsIgnoreCase("/pl") || event.getMessage().equalsIgnoreCase("/plugins ver") || event.getMessage().equalsIgnoreCase("/pl ver")) if (player.hasPermission(Perms.PLUGINS.permission)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.GREEN + "[Bssentials] " + ChatColor.GOLD + "Bukkit Plugins:");
            if (event.getMessage().equalsIgnoreCase("/plugins ver") || event.getMessage().equalsIgnoreCase("/pl ver")) player.sendMessage(getPlugins(true));
            else player.sendMessage(getPlugins(false));
        } else {
            event.setCancelled(true);
            player.sendMessage(plnoperm);
        }

        for (String word : main.getConfig().getStringList("commandBlackList")) {
            if (event.getMessage().substring(1).startsWith(word)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "This command is blocked!");
            }
        }
    }

    public static String getPlugins(boolean showVer) {
        StringBuilder pluginList = new StringBuilder();
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        for (Plugin plugin : plugins) {
            if (pluginList.length() > 0) {
                pluginList.append(ChatColor.WHITE);
                pluginList.append(", ");
            }
            pluginList.append(plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED);

            if (!showVer) pluginList.append(plugin.getDescription().getName());
            else pluginList.append(plugin.getDescription().getName() + ChatColor.GRAY + "(" + plugin.getDescription().getVersion() + ")" + ChatColor.RESET);
        }

        return "(" + plugins.length + "): " + pluginList.toString();
    }
}
