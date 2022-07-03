package discordmarblesolitaire.view;

import java.util.Objects;

import discord4j.core.object.entity.channel.MessageChannel;

public class DiscordSolitaireView implements TextChannelView {
  private MessageChannel channel;

  public DiscordSolitaireView(MessageChannel channel) {
    this.channel = Objects.requireNonNull(channel);
  }

  public DiscordSolitaireView() {
    this.channel = null;
  }


  @Override
  public void setChannel(MessageChannel channel) throws NullPointerException {
    this.channel = Objects.requireNonNull(channel);
  }

  @Override
  public void sendMessage(String message, MessageChannel channel) {
    Objects.requireNonNull(message);
    Objects.requireNonNull(channel);

    if (message.length() >= 2000) {
      channel.createMessage("Error: this message cannot be displayed due to "
          + "the Discord character limit.");
    }
    else {
      channel.createMessage(message).block();
    }
  }

  @Override
  public void sendMessage(String message) throws IllegalStateException {
    if (this.channel == null) {
      throw new IllegalStateException("Cannot send message before channel is set");
    }

    this.sendMessage(message, this.channel);
  }
}
