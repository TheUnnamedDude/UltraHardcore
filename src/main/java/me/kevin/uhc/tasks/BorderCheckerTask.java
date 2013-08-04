package me.kevin.uhc.tasks;

import me.kevin.uhc.UltraHardcore;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class BorderCheckerTask implements Runnable {

    UltraHardcore main;

    public BorderCheckerTask(UltraHardcore main) {
        this.main = main;
    }

    @Override
    public void run() {
        for (Player player : main.getServer().getOnlinePlayers()) {
            if (player.getLocation().getX() < -main.radius) {
                Location target = player.getLocation().clone();
                target.setX(-main.radius);

                if (player.isInsideVehicle()) {
                    Entity vehicle = player.getVehicle();
                    player.leaveVehicle();
                    vehicle.teleport(target);
                    player.teleport(target);
                    vehicle.setPassenger(player);
                } else {
                    player.teleport(target);
                }

                player.sendMessage("You cannot go further.");
            }
            if (player.getLocation().getX() > main.radius) {
                Location target = player.getLocation().clone();
                target.setX(main.radius);

                if (player.isInsideVehicle()) {
                    Entity vehicle = player.getVehicle();
                    player.leaveVehicle();
                    vehicle.teleport(target);
                    player.teleport(target);
                    vehicle.setPassenger(player);
                } else {
                    player.teleport(target);
                }

                player.sendMessage("You cannot go further.");
            }
            if (player.getLocation().getZ() > main.radius) {
                Location target = player.getLocation().clone();
                target.setZ(main.radius);

                if (player.isInsideVehicle()) {
                    Entity vehicle = player.getVehicle();
                    player.leaveVehicle();
                    vehicle.teleport(target);
                    player.teleport(target);
                    vehicle.setPassenger(player);
                } else {
                    player.teleport(target);
                }

                player.sendMessage("You cannot go further.");
            }
            if (player.getLocation().getZ() < -main.radius) {
                Location target = player.getLocation().clone();
                target.setZ(-main.radius);

                if (player.isInsideVehicle()) {
                    Entity vehicle = player.getVehicle();
                    player.leaveVehicle();
                    vehicle.teleport(target);
                    player.teleport(target);
                    vehicle.setPassenger(player);
                } else {
                    player.teleport(target);
                }

                player.sendMessage("You cannot go further.");
            }
        }
    }
}
