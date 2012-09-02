package me.cnaude.plugin.EchoTell;

import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
/**
 *
 * @author cnaude
 */
public class EchoTell extends JavaPlugin implements CommandExecutor {

    String description = "Sends a private message to the given player";
    String usageMessage = "/t <player> <message>";         
     
    @Override 
    public void onEnable() {
        registerCommand("t");
    }    
    
    @Override
     public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            if (!sender.hasPermission("echotell")) {
                return true;
            }
        }
         if (args.length < 2)  {
             sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
             return false;
         }
 
         Player player = Bukkit.getPlayerExact(args[0]);
 
         // If a player is hidden from the sender pretend they are offline
         if (player == null || (sender instanceof Player && !((Player) sender).canSee(player))) {
             sender.sendMessage("There's no player by that name online.");
         } else {
             StringBuilder message = new StringBuilder();
 
             for (int i = 1; i < args.length; i++) {
                 if (i > 1) {
                     message.append(" ");
                 }
                 message.append(args[i]);
             }
 
             String result = ChatColor.YELLOW + sender.getName() + ChatColor.WHITE + " whispers: " + ChatColor.GRAY + message;
 
             if (sender instanceof ConsoleCommandSender) {
                 Bukkit.getLogger().log(Level.INFO, "[{0}->{1}] {2}", new Object[]{sender.getName(), player.getName(), message});
                 Bukkit.getLogger().info(result);
             }
             
             player.sendMessage(result);
             sender.sendMessage(ChatColor.GREEN + "-> " + ChatColor.YELLOW + player.getName() + ChatColor.GREEN + ": " + ChatColor.GRAY + message);
         }
 
         return true;
     }

    private void registerCommand(String command) {
        try {
            getCommand(command).setExecutor(this);
        } catch (Exception ex) {
            System.out.println("Failed to register command '" + command + "'! Is it allready used by some other Plugin? " + ex.getMessage());
        }
    }    
    
}
