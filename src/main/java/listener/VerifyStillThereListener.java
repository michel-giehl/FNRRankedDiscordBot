package listener;

import commands.SendVerifyStillThereCommand;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import util.BotSettings;

public class VerifyStillThereListener extends ListenerAdapter {
    public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent event) {
        User user = event.getUser();
        if(SendVerifyStillThereCommand.verifyMessageIds.containsKey(user)) {
            if(event.getMessageId().equals(SendVerifyStillThereCommand.verifyMessageIds.get(user))) {
                if(event.getReactionEmote().getEmote().equals(BotSettings.fnrEmote)) {
                    // successfully verified
                    SendVerifyStillThereCommand.verifyMessageIds.remove(user);
                    event.getChannel().sendMessage("You've successfully verified that you have time now and during the tournament.").queue();
                }
            }
        }
    }
}
