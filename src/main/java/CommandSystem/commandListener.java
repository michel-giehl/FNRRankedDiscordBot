package CommandSystem;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import util.BotSettings;

public class commandListener extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent e) {
        try {
            if (e.getMessage().getContentRaw().startsWith(BotSettings.prefix) && e.getMessage().getAuthor().getId() != e.getJDA().getSelfUser().getId() && !e.getMessage().getAuthor().isBot()) {
                commandHandler.handleCommand(commandHandler.parse.parser(e.getMessage().getContentRaw().toLowerCase(), e));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}