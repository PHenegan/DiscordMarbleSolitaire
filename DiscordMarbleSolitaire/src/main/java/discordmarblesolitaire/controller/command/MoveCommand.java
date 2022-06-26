package discordmarblesolitaire.controller.command;

import java.util.Objects;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireView;
import discordmarblesolitaire.view.TextChannelView;

/**
 * A command which moves a marble from a given slot to another given slot using the given model
 */
public class MoveCommand implements Runnable {

  private final MarbleSolitaireModel model;
  private final MarbleSolitaireView modelView;
  private final TextChannelView view;
  private final int fromRow, fromCol;
  private final int toRow, toCol;

  /**
   * Creates a move command which will attempt to move between the given slots on the model,
   * outputting to the view if anything goes wrong.
   *
   * @param model       the model to be used for the move
   * @param modelView   a view used to render the model as text
   * @param view        the view used to display a message
   * @param fromRow     the row (zero-indexed) whose marble is being moved
   * @param fromCol     the column (zero-indexed) whose marble is being moved
   * @param toRow       the row (zero-indexed) to which the marble will move
   * @param toCol       the column (zero-indexed) to which the marble will move
   * @throws NullPointerException if the model or view are null
   */
  public MoveCommand(MarbleSolitaireModel model, MarbleSolitaireView modelView,
                     TextChannelView view,
                     int fromRow, int fromCol, int toRow, int toCol) throws NullPointerException {
    this.model = Objects.requireNonNull(model);
    this.modelView = Objects.requireNonNull(modelView);
    this.view = Objects.requireNonNull(view);
    this.fromRow = fromRow;
    this.fromCol = fromCol;
    this.toRow = toRow;
    this.toCol = toCol;
  }

  /**
   * Attempts to make the move, displaying a message otherwise
   *
   * @throws IllegalStateException if an error message cannot be sent to the view
   */
  @Override
  public void run() throws IllegalStateException {
    try {
      this.model.move(this.fromRow - 1, this.fromCol - 1, this.toRow - 1, this.toCol - 1);
      this.view.sendMessage(
          String.format("```%s\n```Score: %d\n", this.modelView.toString(), this.model.getScore()));
    }
    catch (IllegalArgumentException iae) {
      throw new IllegalArgumentException(String.format(
          "Could not make move from (%d, %d) to (%d, %d).",
          this.fromRow, this.fromCol, this.toRow, this.toCol));
    }
  }
}

