package me.kevin.uhc.commands;

import me.kevin.uhc.UltraHardcore;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TeamCommand implements CommandExecutor {
    UltraHardcore main;

    public TeamCommand(UltraHardcore main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        for (Team team : main.scoreboard.getTeams()) {
            if (team.getPlayers().isEmpty()) {
                team.unregister();
            }
        }
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (player.isOp() || player.hasPermission("uhc.team")) {
                if (main.scattered) {
                    if (args.length > 2) {
                        String teamName = args[0];
                        String playerName = args[2];
                        if (args[1].equalsIgnoreCase("add")) {
                            Team team = main.scoreboard.getTeam(teamName);
                            if (team == null) {
                                team = main.scoreboard.registerNewTeam(teamName);
                            }

                            Player pl = main.getServer().getPlayer(playerName);

                            if (pl != null) {
                                team.addPlayer(pl);
                            } else {
                                player.sendMessage("Did not find the player " + playerName);
                            }

                        } else if (args[1].equalsIgnoreCase("remove")) {
                            Team team = main.scoreboard.getTeam(teamName);
                            if (team == null) {
                                String teams = "";
                                for (Team team1 : main.scoreboard.getTeams()) {
                                    if (teams.isEmpty()) {
                                        teams = team1.getName();
                                    } else {
                                        teams += ", " + team1.getName();
                                    }
                                }
                                player.sendMessage("Team not found, teams:");
                                player.sendMessage(teams);
                                return false;
                            }
                            Player pl = main.getServer().getPlayer(playerName);
                            if (pl != null) {
                                if (team.getPlayers().contains(pl)) {
                                    team.removePlayer(pl);
                                } else {
                                    player.sendMessage("The player is not in this team.");
                                }
                            } else {
                                player.sendMessage("Did not find the player " + playerName + ".");
                            }
                        }
                    } else if (args.length > 1) {
                        if (args[0].equalsIgnoreCase("randomize")) {
                            int playersPerTeam = 0;
                            if (args[1].equalsIgnoreCase("random")) {
                                playersPerTeam = new Random(3).nextInt() + 2;
                            } else {
                                try {
                                    playersPerTeam = Integer.parseInt(args[1]);
                                } catch (NumberFormatException e) {
                                    player.sendMessage("Unable to parse " + args[1] + " into a number.");
                                    return false;
                                }
                            }
                            if (playersPerTeam < 2) {
                                player.sendMessage("team must have more than 2 players.");
                                return false;
                            }
                            int teamIndex = 1;
                            int playerIndex = 0;

                            for (Team team : main.scoreboard.getTeams()) {
                                team.unregister();
                            }

                            ArrayList<Player> players = new ArrayList<Player>(Arrays.asList(main.getServer().getOnlinePlayers()));
                            Random random = new Random();

                            while (!players.isEmpty()) {
                                Player rand = players.get(random.nextInt(players.size()));
                                players.remove(rand);
                                String teamName = "Team" + teamIndex;
                                Team team = main.scoreboard.getTeam(teamName);
                                if (team == null) {
                                    team = main.scoreboard.registerNewTeam(teamName);
                                }
                                if (team.getPlayers().size() >= playersPerTeam) {
                                    teamIndex ++;
                                    teamName = "Team" + teamIndex;
                                    team = main.scoreboard.registerNewTeam(teamName);
                                }
                                team.addPlayer(rand);
                            }
                        }
                    } else if (args.length > 0) {
                        if (args[0].equalsIgnoreCase("list")) {
                            for (Team team : main.scoreboard.getTeams()) {
                                String players = "";
                                for (OfflinePlayer oPlayer : team.getPlayers()) {
                                    if (players.isEmpty()) {
                                        players = oPlayer.getName();
                                    } else {
                                        players += ", " + oPlayer.getName();
                                    }
                                }
                                player.sendMessage(team.getName() + ": " + players);
                            }
                        } else if (args[0].equalsIgnoreCase("clear")) {
                            for (Team team : main.scoreboard.getTeams()) {
                                team.unregister();
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
