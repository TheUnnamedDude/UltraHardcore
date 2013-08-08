package me.kevin.uhc.commands;

import me.kevin.uhc.UltraHardcore;
import me.kevin.uhc.tasks.TeleportAfterChunkloadTask;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ScatterCommand implements CommandExecutor {
    UltraHardcore main;

    public ScatterCommand(UltraHardcore main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (player.hasPermission("uhc.scatter") || player.isOp()) {
                if (main.scattered) {
                    HashMap<OfflinePlayer, Location> players = new HashMap<OfflinePlayer, Location>();
                    if (args.length > 0) {
                        try {
                            main.radius = Integer.parseInt(args[0]);
                        } catch (NumberFormatException e) {
                            player.sendMessage("Unable to parse " + args[0] + " to a number.");
                            return false;
                        }
                    }
                    World world = player.getWorld();
                    if (main.scoreboard.getTeams().isEmpty()) {
                        for (Player pl : main.getServer().getOnlinePlayers()) {
                            Team team = main.scoreboard.registerNewTeam(pl.getName());
                            team.addPlayer(pl);
                        }
                    }
                    //Lets scatter this shiet!
                    for (Team team : main.scoreboard.getTeams()) {
                        Random random = new Random();
                        int randomX = (random.nextInt(main.radius - 200) + 200);
                        int randomZ = (random.nextInt(main.radius - 200) + 200);
                        if (random.nextBoolean()) {
                            randomX = -randomX;
                        }
                        if (random.nextBoolean()) {
                            randomZ = -randomZ;
                        }

                        //Might add checking for nearby players
                        int Y = world.getHighestBlockYAt(randomX, randomZ);
                        Location loc = new Location(world, randomX, Y, randomZ);
                        for (OfflinePlayer offlinePlayer : new ArrayList<OfflinePlayer>(team.getPlayers())) {
                            if (offlinePlayer.isOnline()) {
                                loc.getChunk().load();
                                players.put(offlinePlayer, loc);
                            } else {
                                team.removePlayer(offlinePlayer);
                            }
                            if (team.getPlayers().isEmpty()) {
                                team.unregister();
                            }
                        }
                    }
                    main.getServer().getScheduler().scheduleSyncDelayedTask(main, new TeleportAfterChunkloadTask(main, players), 20*30);
                    main.scattered = true;
                }
            }
        }
        return false;
    }
}
