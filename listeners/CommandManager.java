package listenenrs;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.LocalDateTime;

import java.util.*;
import java.util.List;

public class CommandManager extends ListenerAdapter {

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {

        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("rape", "moves the target to every channel and back to the previous channel")
                .addOption(OptionType.USER, "member", "Mention member", true));

        commandData.add(Commands.slash("kick", "kicks a member")
                        .addOption(OptionType.USER, "target", "who to kick?",true)
                        .addOption(OptionType.STRING, "reason", "reason", false));

        commandData.add(Commands.slash("msg-all", "Message all members")
                .addOption(OptionType.STRING, "message", "Message", true));

        commandData.add(Commands.slash("announce", "Send a message to a channel with @here mention"));
        event.getJDA().updateCommands().addCommands(commandData).queue();

        LocalDateTime dateTime = LocalDateTime.now();
            TimerTask task;
                task = new TimerTask() {
                    @Override
                    public void run() {
                        if (dateTime.toLocalTime().getHour() == 18)
                            event.getGuild().getTextChannelById(992056741850255481L)
                                    .sendMessage("ITS 6PM").queue();
                    }
                };
            Timer timer = new Timer();
            timer.schedule(task, 1000, 1000);
        }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Role msgAllRole = event.getGuild().getRoleById(998947361483149463L);
        String command = event.getName();

        if (command.equals("msg-all")) {
            String message = event.getOption("message").getAsString();
            if(event.getMember().getRoles().contains(msgAllRole))
                msgAllCommand(event, message);
            else
                event.reply("not enough roles").setEphemeral(true).queue();
        }
        if (command.equals("rape")) {
            OptionMapping opt = event.getOption("member");
            Member targetRape = opt.getAsMember();
            rapeCommand(event,targetRape);
        }
        if (command.equals("announce")) {
            announceCommand(event);
        }
        if (command.equals("kick")){
            OptionMapping opt = event.getOption("target");
            Member targetKick = opt.getAsMember();
            String reason = event.getOption("reason").getAsString();
            kickCommand(event, targetKick, reason);
        }

    }
    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equals("sup-modal")) {
            Color purple = new Color(193,0,193);
            String message = event.getValue("sup-message").getAsString() + "\n\n||@here||";
            EmbedBuilder embedMessage = new EmbedBuilder()
                    .setDescription(message)
                    .setColor(purple);
            System.out.println("test");
            event.getGuild().getTextChannelById(992056741850255481L).sendMessageEmbeds(embedMessage.build()).queue();
        }
    }

    public void kickCommand(SlashCommandInteractionEvent event,Member member, String reason){
        event.getGuild().kick(member,reason).queue();
    }

    public void rapeCommand(SlashCommandInteractionEvent event, Member member){
        List<VoiceChannel> voiceChannels = Objects.requireNonNull(event.getGuild()).getVoiceChannels();
        if (member == null) {
            event.deferReply().queue();
            event.getHook().sendMessage("not a valid user.").setEphemeral(true).queue();
        }
        Role rapeRole = event.getGuild().getRoleById(991610959930064958L);
        assert member != null;
        AudioChannel prevChan = Objects.requireNonNull(member.getVoiceState()).getChannel();
        if (prevChan == null) {
            event.deferReply().queue();
            event.getHook().sendMessage("User not in a voice channel").setEphemeral(true).queue();
            return;
        } else if (!event.getMember().getRoles().contains(rapeRole)) {
            event.deferReply().queue();
            event.getHook().sendMessage("You don't have /rape Role.").setEphemeral(true).queue();
            return;
        } else {
            event.deferReply().queue();
            event.getHook().sendMessage("Moving " + member.getEffectiveName() + ".").setEphemeral(true).queue();
        }
        for (AudioChannel voiceChannel : voiceChannels) {
            event.getGuild().moveVoiceMember(member, voiceChannel).queue();
            event.getGuild().moveVoiceMember(member, prevChan).queue();
        }
    }
    public void msgAllCommand(SlashCommandInteractionEvent event,String message){
        Member[] members = event.getGuild().getMembers().toArray(new Member[0]);
        for (Member member : members) {
            member.getUser().openPrivateChannel().flatMap(channel -> channel.sendMessage(message)).queue();
        }
    }
    public void announceCommand(SlashCommandInteractionEvent event){
        TextInput messageAnnounce = TextInput.create("sup-message", "Message", TextInputStyle.PARAGRAPH)
                .setMinLength(1)
                .setRequired(true)
                .setPlaceholder("Enter announce message")
                .build();
        Modal modal = Modal.create("sup-modal", "announce message to channel")
                .addActionRows(ActionRow.of(messageAnnounce))
                .build();
        Role announceRole = event.getGuild().getRoleById(992045339748089998L);
        if (!event.getMember().getRoles().contains(announceRole)) {
            event.reply("You don't have /announce role.").setEphemeral(true).queue();
            return;
        }
        event.replyModal(modal).queue();
    }
}
