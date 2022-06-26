package discordmarblesolitaire.view;

import discord4j.core.object.entity.channel.MessageChannel;

/**
 * <p>
 *   A {@code SolitaireMessageChannelView} represents a view that can send text messages on a chat
 *   server. This requires being able to change the current text channel, because otherwise
 *   it would be
 * </p>
 */
public interface TextChannelView {
  /**
   * Changes the text channel to send message to
   * @param channel the channel in which messages should be sent in the future.
   */
  void setChannel(MessageChannel channel);

  /**
   * Renders the given message into the given text channel.
   * @param message the message to send
   * @param channel the text channel to send the message in
   */
  void sendMessage(String message, MessageChannel channel);

  /**
   * Sends the given message to the text server.
   * @param message the message to send
   */
  void sendMessage(String message);
}