package discordmarblesolitaire;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discordmarblesolitaire.controller.DiscordCommandController;

/**
 * Class containing the main method which will run the bot
 */
public class DiscordBot {
  public static void main(String[] args) {
    String token = "";
    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "--token":
        case "-t":
          if (args.length > i + 1) {
            token = args[++i];
          }
          else {
            closeError("'-t' flag requires an argument representing the token");
          }
          break;
        default:
          closeError("Illegal argument '" + args[i] + "'");
          break;
      }
    }

    GatewayDiscordClient client = DiscordClientBuilder.create(token)
        .build()
        .login()
        .block();

    DiscordCommandController controller = new DiscordCommandController(client);
    controller.playGame();

    client.onDisconnect().block();
  }

  private static void closeError(String msg) {
    System.out.println(msg);
    System.exit(1);
  }
}
