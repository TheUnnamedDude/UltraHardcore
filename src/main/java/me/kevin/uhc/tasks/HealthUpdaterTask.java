package me.kevin.uhc.tasks;

import me.kevin.uhc.UltraHardcore;
import org.bukkit.entity.Player;

public class HealthUpdaterTask implements Runnable {
    UltraHardcore main;
    public HealthUpdaterTask(UltraHardcore main) {
        this.main = main;
    }

    @Override
    public void run() {
        for (Player player : main.getServer().getOnlinePlayers()) {
            main.updateHealth(player);
        }
    }
}
