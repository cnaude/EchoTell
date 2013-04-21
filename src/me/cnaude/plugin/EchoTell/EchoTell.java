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
    String replyUsageMessage = "/r <message>";

    private SenderLookup senderLookup;

    @Override 
    public void onEnable() {
        senderLookup = new SenderLookup();
        registerCommand("t");
        registerCommand("r");
    }    
    
    @Override
     public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            if (!sender.hasPermission("echotell")) {
                return false;
            }
        }

        if (cmd.getName().equalsIgnoreCase("r")) {
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Usage: " + replyUsageMessage);
                return true;
            }

            String receiverName = senderLookup.getLastSender(sender);

            if (receiverName == null) {
                sender.sendMessage(ChatColor.RED + "Last sender could not be found.");
                return true;
            }

            Player playerReceiver = getServer().getPlayerExact(receiverName);

            if (playerReceiver == null) {
                sender.sendMessage(ChatColor.RED + "Sorry player " + receiverName + " could not be found or is not a valid player.");
                return true;
            }

            sendWhisper(sender, playerReceiver, args, 0);

        } else if (cmd.getName().equalsIgnoreCase("t")) {
            if (args.length < 2)  {
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return true;
            }

            Player player = getServer().getPlayerExact(args[0]);

            sendWhisper(sender, player, args, 1);
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

    private void sendWhisper(CommandSender sender, Player player, String args[], int startingArg) {
        // If a player is hidden from the sender pretend they are offline
        if (player == null || (sender instanceof Player && !((Player) sender).canSee(player))) {
            sender.sendMessage("There's no player by that name online.");
        } else {
            StringBuilder message = new StringBuilder();

            for (int i = startingArg; i < args.length; i++) {
                if (i > startingArg) {
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
            senderLookup.pairSender(sender, player);
        }
    }
}
