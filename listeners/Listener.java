package listenenrs;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Listener extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("ONLINE!");
    }


    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        int count = event.getGuild().getMemberCount();
        event.getJDA().getPresence().setActivity(Activity.watching(count + " Members"));

    }
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        TextChannel welChan = event.getGuild().getTextChannelById(935462172199366656L);
        assert welChan != null;
        welChan.sendMessage("Welcome " + event.getUser().getAsMention() + " To **sarzamine ali**.").queue();
    }
}
