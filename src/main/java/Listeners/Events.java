package Listeners;

import Utils.Utils;
import com.swagsteve.safeload.SafeLoad;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
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
                        for (Player online : Bukkit.getOnlinePlayers()) {
                            online.showPlayer(SafeLoad.getInstance(), p);
                        }
                    }

                    // Stop allowing flight
                    if (SafeLoad.fly_kick_bypass) {
                        if (!p.getGameMode().equals(GameMode.CREATIVE)) {
                            p.setAllowFlight(false);
                        }
                    }
                }
            }, SafeLoad.event_cancel_delay);
        } else if (e.getStatus().equals(PlayerResourcePackStatusEvent.Status.DECLINED) || e.getStatus().equals(PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD)) {
            if (!SafeLoad.kick_if_rejected) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(SafeLoad.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        SafeLoad.packLoaded.add(p);

                        if (SafeLoad.blindness) {
                            p.removePotionEffect(PotionEffectType.BLINDNESS);
                        }

                        if (SafeLoad.invisibility) {
                            for (Player online : Bukkit.getOnlinePlayers()) {
                                online.showPlayer(SafeLoad.getInstance(), p);
                            }
                        }

                        // Stop allowing flight
                        if (SafeLoad.fly_kick_bypass) {
                            if (!p.getGameMode().equals(GameMode.CREATIVE)) {
                                p.setAllowFlight(false);
                            }
                        }
                    }
                }, SafeLoad.event_cancel_delay);
            }
        }
    }

    // Inventory Events
    @EventHandler
    public void invClick(InventoryClickEvent e) {
        if (!SafeLoad.packLoaded.contains(e.getWhoClicked())) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void invDrag(InventoryDragEvent e) {
        if (!SafeLoad.packLoaded.contains(e.getWhoClicked())) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void invSwapHands(PlayerSwapHandItemsEvent e) {
        if (!SafeLoad.packLoaded.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void invPickupArrow(PlayerPickupArrowEvent e) {
        if (!SafeLoad.packLoaded.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void invPickupItem(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (!SafeLoad.packLoaded.contains(p)) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void invOpen(InventoryOpenEvent e) {
        Player p = (Player) e.getPlayer();
        if (!SafeLoad.packLoaded.contains(p)) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void invDropItem(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (!SafeLoad.packLoaded.contains(p)) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void invInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!SafeLoad.packLoaded.contains(p)) {
            e.setCancelled(true);
        }
    }

    // Join & quit events
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        // Bypass fly kick
        if (SafeLoad.fly_kick_bypass) {
            p.setAllowFlight(true);
        }

        if (SafeLoad.invisibility) {
            for (Player loaded : Bukkit.getOnlinePlayers()) {
                loaded.hidePlayer(SafeLoad.getInstance(), p);
            }
        }

        if (SafeLoad.blindness) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 9999, 255, true, false));
        }

        if (SafeLoad.suppress_join_message) {
            e.setJoinMessage("");
        }

        SafeLoad.packLoaded.remove(p);
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

    // Chat events
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (SafeLoad.disable_chat) {
            Player p = e.getPlayer();
            if (!SafeLoad.packLoaded.contains(p)) {
                e.setCancelled(true);
                e.getRecipients().remove(p);
            }

            e.getRecipients().removeIf(pl -> !SafeLoad.packLoaded.contains(pl));
        }
    }

    // Entity targeting
    @EventHandler
    public void onTarget(EntityTargetEvent e) {
        if (e.getTarget() instanceof Player) {
            Player target = (Player) e.getTarget();

            if (!SafeLoad.packLoaded.contains(target)) {
                e.setTarget(null);
            }
        }
    }

    // Health regen
    @EventHandler
    public void onRegen(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if (!SafeLoad.packLoaded.contains(p)) {
                e.setCancelled(true);
            }
        }
    }
}