package engineering.oddsystems.cinema.reservation.application;

import akka.actor.typed.ActorRef;
import engineering.oddsystems.cinema.reservation.domain.Show;
import engineering.oddsystems.cinema.reservation.domain.ShowCommand;
import java.io.Serializable;

public sealed interface ShowEntityCommand extends Serializable {

  record ShowCommandEnvelope(ShowCommand command, ActorRef<ShowEntityResponse> replyTo) implements
      ShowEntityCommand {

  }

  record GetShow(ActorRef<Show> replyTo) implements ShowEntityCommand {

  }

}
