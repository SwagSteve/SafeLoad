package Listeners;

import Utils.Utils;
import com.swagsteve.safeload.SafeLoad;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Events implements Listener {

    // Pack load event
    @EventHandler
    public void onPack(PlayerResourcePackStatusEvent e) {
        Player p = e.getPlayer();

        if (SafeLoad.kick_if_rejected) {
            if (e.getStatus().equals(PlayerResourcePackStatusEvent.Status.DECLINED) || e.getStatus().equals(PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD)) {
                p.kickPlayer(Utils.Color(SafeLoad.kick_message).replace("%name%", p.getName()));
            }
        }

        if (e.getStatus().equals(PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED)) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(SafeLoad.getInstance(), new Runnable() {
                @Override
                public void run() {

                    if (SafeLoad.suppress_join_message) {
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            all.sendMessage(Utils.Color(SafeLoad.delayed_join_message).replace("%name%", p.getName()));
                        }
                    }

                    SafeLoad.packLoaded.add(p);

                    if (SafeLoad.blindness) {
                        p.removePotionEffect(PotionEffectType.BLINDNESS);
                    }

                    if (SafeLoad.invisibility) {
                        p.removePotionEffect(PotionEffectType.INVISIBILITY);
                    }

                }
            }, SafeLoad.event_cancel_delay);
        } else if (e.getStatus().equals(PlayerResourcePackStatusEvent.Status.DECLINED) || e.getStatus().equals(PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD)) {
            if (!SafeLoad.kick_if_rejected) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(SafeLoad.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        SafeLoad.packLoaded.remove(p);
                    }
                }, 30L);
            }
        }
    }

    // Inventory Events
    @EventHandler
    public void inv1(InventoryClickEvent e) {
        if (!SafeLoad.packLoaded.contains(e.getWhoClicked())) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void inv2(InventoryDragEvent e) {
        if (!SafeLoad.packLoaded.contains(e.getWhoClicked())) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void inv3(PlayerSwapHandItemsEvent e) {
        if (!SafeLoad.packLoaded.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void inv4(PlayerPickupArrowEvent e) {
        if (!SafeLoad.packLoaded.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void inv5(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (!SafeLoad.packLoaded.contains(p)) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void inv6(InventoryOpenEvent e) {
        Player p = (Player) e.getPlayer();
        if (!SafeLoad.packLoaded.contains(p)) {
            e.setCancelled(true);
        }
    }

    // Join & quit events
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (SafeLoad.invisibility) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999, 255, true, false));
        }

        if (SafeLoad.blindness) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 9999, 255, true, false));
        }

        if (SafeLoad.suppress_join_message) {
            e.setJoinMessage("");
        }

        if (SafeLoad.packLoaded.contains(p)) {
            SafeLoad.packLoaded.remove(p);
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (!SafeLoad.packLoaded.contains(e.getPlayer())) {
            if (SafeLoad.suppress_quit_message) {
                e.setQuitMessage("");
            }
        }
    }

    // Mobility events
    @EventHandler
    public void onDamaged(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (!SafeLoad.packLoaded.contains(p)) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onDamageOthers(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            if (!SafeLoad.packLoaded.contains(p)) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!SafeLoad.packLoaded.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
}