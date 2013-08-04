package me.kevin.uhc.listeners;

import me.kevin.uhc.UltraHardcore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class EntityListener implements Listener {
    UltraHardcore main;

    public EntityListener(UltraHardcore main) {
        this.main = main;
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player) {
            Player player = (Player)event.getTarget();
            if (!main.ingame || main.getTeamByPlayer(player) == null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (main.ingame) {
            if (event.getEntity() instanceof Player) {
                main.updateHealth((Player)event.getEntity());
            }
            if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
                main.playerListener.damageCounter.put((Player)event.getEntity(), (Player)event.getDamager());
            }
            if (event.getDamager() instanceof Player) {
                Player player = (Player)event.getDamager();
                if (main.getTeamByPlayer(player) == null) {
                    event.setCancelled(true);
                }
            }
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (main.ingame) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player)event.getEntity();
                if (main.getTeamByPlayer(player) == null) {
                    event.setCancelled(true);
                } else {
                    main.updateHealth(player);
                }
            }
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            if (main.getTeamByPlayer(player) == null) {
                event.setFoodLevel(20);
            }
        }
    }
}
