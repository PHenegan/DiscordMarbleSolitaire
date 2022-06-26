package discordmarblesolitaire.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import cs3500.marblesolitaire.factory.EnglishSolitaireFactory;
import cs3500.marblesolitaire.factory.EuropeanSolitaireFactory;
import cs3500.marblesolitaire.factory.MarbleSolitaireAbstractFactory;
import cs3500.marblesolitaire.factory.TriangleSolitaireFactory;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireView;

/**
 * A controller which plays a game of Marble Solitaire using text input from a Discord server.
 */
public class DiscordCommandController implements MarbleSolitaireCommandController {

  private final Map<String, Function<String[], Runnable>> commands;
  private final MarbleSolitaireView discordView;

  private MarbleSolitaireModel model;
  private MarbleSolitaireView textView;

  public DiscordCommandController() {
    commands = new HashMap<>();
    commands.put("!newgame", (args) -> new NewGameCommand(args));
  }

  @Override
  public void processTextCommand(String message) {

  }

  @Override
  public void playGame() throws IllegalStateException {

  }

  /**
   * A private helper class for making a new game.
   */
  private class NewGameCommand implements Runnable {
    private MarbleSolitaireAbstractFactory factory;


    /**
     * makes a command which creates the marble solitaire model and text view
     * @param args A list of command line arguments given as part of the command.
     * @throws IllegalArgumentException if any arguments are invalid
     */
    public NewGameCommand(String[] args) throws IllegalArgumentException {

    }

    /**
     * Creates a model and a corresponding text view
     * @throws IllegalArgumentException if the model or view cannot be created.
     */
    @Override
    public void run() throws IllegalArgumentException {
      DiscordCommandController.this.model = factory.createModel();
      DiscordCommandController.this.textView = factory.createView();
    }

    private MarbleSolitaireAbstractFactory getFactory(String type)
        throws IllegalArgumentException {
      switch (type.toLowerCase()) {
        case "english":
          factory = new EnglishSolitaireFactory();
          break;
        case "triangle":
          factory = new TriangleSolitaireFactory();
          break;
        case "european":
          factory = new EuropeanSolitaireFactory();
          break;
        default:
          throw new IllegalArgumentException("Invalid factory type");
      }
    }
  }


}
