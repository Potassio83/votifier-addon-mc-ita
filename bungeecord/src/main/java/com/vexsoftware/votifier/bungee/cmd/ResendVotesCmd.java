package com.vexsoftware.votifier.bungee.cmd;

import com.vexsoftware.votifier.bungee.NuVotifier;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.net.VotifierSession;
import com.vexsoftware.votifier.util.ArgsToVote;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import org.json.JSONException;

import java.io.IOException;

public class ResendVotesCmd extends Command{
    private final NuVotifier plugin;


    public ResendVotesCmd(NuVotifier plugin) {
        super("resendvotes", "nuvotifier.resendvotes");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("nuvotifier.resendvotes")) {
            plugin.getLogger().info("Sto recuperando i voti da mc italia!");
            try {
                plugin.getUsers(plugin.url).forEach(s -> plugin.onVoteReceived(plugin.voteBuilder(s), VotifierSession.ProtocolVersion.TWO, "Minecraft-italia.it"));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
