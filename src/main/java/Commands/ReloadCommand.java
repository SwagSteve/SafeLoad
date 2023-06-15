package Commands;

import Utils.Utils;
import com.swagsteve.safeload.SafeLoad;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (p.isOp() || p.hasPermission("safeload.commands.reload")) {
                SafeLoad.getInstance().reloadConfig();
                SafeLoad.cacheConfig();
                p.sendMessage(Utils.Color("[&aSL&r] &a&lConfig successfully reloaded!"));

                for (Player all : Bukkit.getOnlinePlayers()) {
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        all.showPlayer(SafeLoad.getInstance(), online);
                    }
                }
            } else {
                p.sendMessage(Utils.Color("[&aSL&r] &c&lYou don't have permission to use this command!"));
            }
        }
        return false;
    }
}