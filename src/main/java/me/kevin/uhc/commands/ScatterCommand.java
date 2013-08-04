package me.kevin.uhc.commands;

import me.kevin.uhc.UltraHardcore;
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
                            Player pl = offlinePlayer.getPlayer();
                            pl.teleport(loc);
                            pl.setFoodLevel(20);
                            pl.setHealth(20);
                            pl.setSaturation(10);
                            pl.setGameMode(GameMode.SURVIVAL);
                            pl.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30 * 20, 10));
                            main.updateHealth(pl);
                        } else {
                            team.removePlayer(offlinePlayer);
                        }
                        if (team.getPlayers().isEmpty()) {
                            team.unregister();
                        }
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
        return false;
    }
}
