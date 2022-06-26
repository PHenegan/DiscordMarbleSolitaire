package discordmarblesolitaire.controller;

import cs3500.marblesolitaire.controller.MarbleSolitaireController;

/**
 * Represents an Asynchronous controller that will check user messages
 * until a valid command is found.
 */
public interface MarbleSolitaireCommandController extends MarbleSolitaireController {
  /**
   * Processes a message input, determines if it is a command, then does the appropriate action for
   * that command
   * @param message the message
   */
  void processTextCommand(String message);
}
