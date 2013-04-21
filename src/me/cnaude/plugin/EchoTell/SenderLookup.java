package me.cnaude.plugin.EchoTell;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Starbuck Johnson
 * Date: 4/21/13
 * Time: 3:39 PM
 */

/**
 * A mapping class for a tell last sender command.
 */
public class SenderLookup {

    // Sender -> receiver and receiver -> sender mapping.
    private Map<String, String> senderMap;

    public SenderLookup() {
        senderMap = new HashMap<String, String>();
    }

    public void pairSender(CommandSender sender, Player receiver) {
        senderMap.put(sender.getName(), receiver.getName());
        senderMap.put(receiver.getName(), sender.getName());
    }

    public String getLastSender(CommandSender receiver) {
        if (senderMap.containsKey(receiver.getName())) {
            return senderMap.get(receiver.getName());
        }

        return null;
    }
}
