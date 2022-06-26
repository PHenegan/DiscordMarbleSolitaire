package discordmarblesolitaire.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import cs3500.marblesolitaire.factory.EnglishSolitaireFactory;
import cs3500.marblesolitaire.factory.EuropeanSolitaireFactory;
import cs3500.marblesolitaire.factory.MarbleSolitaireAbstractFactory;
import cs3500.marblesolitaire.factory.TriangleSolitaireFactory;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireView;
import discordmarblesolitaire.controller.command.MoveCommand;

/**
 * A controller which plays a game of Marble Solitaire using text input from a Discord server.
 */
public class DiscordCommandController implements MarbleSolitaireCommandController {

  private final Map<String, Function<String[], Runnable>> commands;
  private final MarbleSolitaireView mainView;

  private MarbleSolitaireModel model;
  private MarbleSolitaireView textView;

  /**
   * Creates a controller which handles processing of text commands, as well as what
   * gets outputted to a view.
   * @throws IllegalStateException if a view cannot be displayed to
   */
  public DiscordCommandController(MarbleSolitaireView mainView) throws IllegalStateException {
    this.mainView = Objects.requireNonNull(mainView);


    commands = new HashMap<>();
    commands.put("!game", NewGameCommand::new);
    commands.put("!move", (args) -> new MoveCommand(this.model, this.mainView,
        Integer.parseInt(args[0]), Integer.parseInt(args[1]),
        Integer.parseInt(args[2]), Integer.parseInt(args[3])));
    commands.put("!score", (args) ->
        () -> this.tryRender(Integer.toString(this.model.getScore())));
    commands.put("!display", (args) -> () -> {
      this.tryRenderBoard();
      this.tryRender(String.format("\nScore: %d\n", this.model.getScore()));
    });
  }

  @Override
  public void processTextCommand(String message) {
    String[] cmdArgs = message.split(" ");

    // The message cannot be blank
    if (cmdArgs.length == 0) {
      return;
    }
    String command = cmdArgs[0];

    Function<String[], Runnable> runCmd = this.commands.getOrDefault(command, null);

    // If the first word is not a command, nothing has to be done.
    if (runCmd == null) {
      return;
    }

    try {
      // Separates the command from its arguments in order to create the run command,
      // then attempts to run that.
      runCmd.apply(cmdArgs).run();
    }
    catch (IllegalArgumentException e) {
      this.tryRender("Error running command: " + e.getMessage() + "\n");
    }
  }

  @Override
  public void playGame() throws IllegalStateException {

  }

  // throws an IllegalStateException if the message cannot be rendered
  private void tryRender(String message) throws IllegalStateException {
    try {
      this.mainView.renderMessage(message);
    }
    catch (IOException e) {
      throw new IllegalStateException("Could not display to view");
    }
  }

  // throws an IllegalStateException if the board cannot be rendered
  private void tryRenderBoard() throws IllegalStateException {
    try {
      this.mainView.renderMessage(this.textView.toString() + "\n");
    }
    catch (IOException e) {
      throw new IllegalStateException("Could not display to view");
    }
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

      Function<String, Boolean> isNumber = (s) -> s.matches("-?0*\\d*");

      factory = new EnglishSolitaireFactory();
      for (int i = 0; i < args.length; i++) {
        switch (args[i]) {
          case "--type":
          case "-t":
            if (args.length > i + 1) {
              this.factory = getFactory(args[i + 1]);
            }
            else {
              throw new IllegalArgumentException("type modifier must take a marble solitaire type");
            }
            break;
          case "--size":
          case "-s":
            if (args.length > i + 1 && isNumber.apply(args[i + 1])) {
              this.factory.setSize(Integer.parseInt(args[i + 1]));
            }
            else {
              throw new IllegalArgumentException("size modifier must take in an integer");
            }
            break;
          case "--hole":
          case "-h":
            if (args.length > i + 2
                && isNumber.apply(args[i + 1]) && isNumber.apply(args[i + 2])) {
              this.factory.setEmptySlot(
                  Integer.parseInt(args[i + 1]) - 1,
                  Integer.parseInt(args[i + 2]) - 1);
            }
            else {
              throw new IllegalArgumentException("hole modifier must take 2 integer arguments");
            }
            break;
          default:
            throw new IllegalArgumentException("Invalid command argument");
        }
      }
    }

    /**
     * Creates a model and a corresponding text view
     * @throws IllegalArgumentException if the model or view cannot be created.
     */
    @Override
    public void run() throws IllegalStateException {
      DiscordCommandController.this.model = this.factory.createModel();
      DiscordCommandController.this.textView = this.factory.createView();
    }

    private MarbleSolitaireAbstractFactory getFactory(String type)
        throws IllegalArgumentException {
      MarbleSolitaireAbstractFactory factory;
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
      return factory;
    }
  }


}
