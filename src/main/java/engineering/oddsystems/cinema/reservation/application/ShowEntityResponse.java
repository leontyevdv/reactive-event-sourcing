package engineering.oddsystems.cinema.reservation.application;

import engineering.oddsystems.cinema.reservation.domain.ShowCommandError;
import java.io.Serializable;

public sealed interface ShowEntityResponse extends Serializable {

  final class CommandProcessed implements ShowEntityResponse {

  }

  record CommandRejected(ShowCommandError error) implements ShowEntityResponse {

  }
}
