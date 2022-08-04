import com.yashar.khayemaleali.listenenrs.Listener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import com.yashar.khayemaleali.listenenrs.CommandManager;

import javax.security.auth.login.LoginException;

public class KhayemaleAli {
    private final Dotenv config;
    private final JDA jda;

    public KhayemaleAli() throws LoginException {
        config = Dotenv.configure().load();

        jda = JDABuilder.createDefault(token())
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.watching("members"))
                .addEventListeners(new Listener(), new CommandManager())
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .build();
    }

    public JDA getJda(){
        return jda;
    }

    String token() {
        return config.get("TOKEN");
    }

    public static void main(String[] args) {
        try {
            KhayemaleAli bot = new KhayemaleAli();
        } catch (LoginException e) {
            System.out.println("ERROR: Invalid bot token.");
        }
    }
}
