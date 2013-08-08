package me.kevin.uhc.tasks;

import me.kevin.uhc.UltraHardcore;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class TeleportAfterChunkloadTask implements Runnable {
    UltraHardcore main;
    HashMap<OfflinePlayer, Location> players = new HashMap<OfflinePlayer, Location>();


    public TeleportAfterChunkloadTask(UltraHardcore main, HashMap<OfflinePlayer, Location> players) {
        this.main = main;
        this.players = players;
    }

    @Override
    public void run() {
        for (OfflinePlayer player : players.keySet()) {
            if (player.isOnline()) {
                Player pl = player.getPlayer();
                pl.teleport(players.get(player));
                pl.setFoodLevel(20);
                pl.setHealth(20);
                pl.setSaturation(10);
                pl.setGameMode(GameMode.SURVIVAL);
                pl.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30 * 20, 10));
                main.updateHealth(pl);
            }
        }

        for (Player pl : main.getServer().getOnlinePlayers()) {
            if (main.getTeamByPlayer(pl) == null) {
                main.setSpectator(pl);
            }
        }
        main.ingame = true;
    }
}
