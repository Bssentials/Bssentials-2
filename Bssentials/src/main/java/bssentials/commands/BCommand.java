package bssentials.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import bssentials.Bssentials;

public abstract class BCommand implements CommandExecutor {

    public boolean onlyPlayer;
    public CmdInfo i;
    private Bssentials bss;

    public List<String> aliases = new ArrayList<String>();

    public BCommand() {
        this.bss = Bssentials.get();
        CmdInfo i = this.getClass().getAnnotation(CmdInfo.class);
        onlyPlayer = false;
        if (null != i) {
            onlyPlayer = i.onlyPlayer();
            this.i = i;
            for (String s : i.aliases())
                aliases.add(s);

            if (null != i.permission() && i.permission().length() > 3) {
                String perm = i.permission();
                if (!(perm.equalsIgnoreCase("REQUIRES_OP") || perm.equalsIgnoreCase("NONE"))) {
                    Permission p = new Permission(i.permission());
                    p.setDescription("Permission for a command");
                    bss.getLogger().info("Registering permission \"" + i.permission() + "\" ...");
                    Bukkit.getPluginManager().addPermission(p);
                }
            }
        }

        //String name = this.getClass().getName();
        //Bukkit.getPluginManager().addPermission(new Permission(i.permission()));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (onlyPlayer && !(sender instanceof Player)){
            sender.sendMessage("Player only command!");
            return false;
        }
        if (!(hasPerm(sender, cmd))){
            message(sender, ChatColor.RED + "No permission for command.");
            return false;
        }

        return onCommand(sender, cmd, args);
    }

    @Deprecated
    public boolean hasPerm(CommandSender s, Command cmd) {
        if (null != i.permission() && i.permission().length() > 3) {
            String ip = i.permission();
            if (ip.equalsIgnoreCase("RQUIRES_OP")) return s.isOp();
            if (ip.equalsIgnoreCase("NONE")) return true;

            return s.hasPermission(ip);
        }

        String c = cmd.getName().toLowerCase();
        return (s.isOp() || s.hasPermission("bssentials.command." + c) || s.hasPermission("essentials." + c)
                || s.hasPermission("bssentials.command.*"));
    }

    public boolean message(CommandSender cs, String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);

        boolean isPlr = cs instanceof Player;
        cs.sendMessage(isPlr ? message : ChatColor.stripColor(message));
        return isPlr;
    }

    public FileConfiguration getConfig() {
        return bss.getConfig();
    }

    public Bssentials getPlugin() {
        return bss;
    }

    public Player getPlayer(String name) {
        return Bukkit.getPlayer(name);
    }

    public abstract boolean onCommand(CommandSender sender, Command cmd, String[] args);

}