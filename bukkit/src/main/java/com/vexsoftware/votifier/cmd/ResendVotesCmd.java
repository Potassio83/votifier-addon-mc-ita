package com.vexsoftware.votifier.cmd;

import com.vexsoftware.votifier.NuVotifierBukkit;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.net.VotifierSession;
import com.vexsoftware.votifier.util.ArgsToVote;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.JSONException;

import java.io.IOException;

public class ResendVotesCmd implements CommandExecutor {
    private final NuVotifierBukkit plugin;

    public ResendVotesCmd(NuVotifierBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("nuvotifier.resendvotes")) {
            plugin.getLogger().info("Sto recuperando i voti da mc italia!");
            try {
                plugin.getUsers(plugin.url).forEach(s -> plugin.onVoteReceived(plugin.voteBuilder(s), VotifierSession.ProtocolVersion.TWO, "Minecraft-italia.it"));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        return true;

    }
}
