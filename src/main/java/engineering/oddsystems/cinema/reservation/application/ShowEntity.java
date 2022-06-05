package engineering.oddsystems.cinema.reservation.application;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.cluster.sharding.typed.javadsl.EntityTypeKey;
import akka.persistence.typed.PersistenceId;
import akka.persistence.typed.javadsl.CommandHandlerWithReply;
import akka.persistence.typed.javadsl.EventHandler;
import akka.persistence.typed.javadsl.EventSourcedBehaviorWithEnforcedReplies;
import akka.persistence.typed.javadsl.ReplyEffect;
import engineering.oddsystems.cinema.base.domain.Clock;
import engineering.oddsystems.cinema.reservation.application.ShowEntityResponse.CommandProcessed;
import engineering.oddsystems.cinema.reservation.application.ShowEntityResponse.CommandRejected;
import engineering.oddsystems.cinema.reservation.domain.Show;
import engineering.oddsystems.cinema.reservation.domain.ShowCommand;
import engineering.oddsystems.cinema.reservation.domain.ShowEvent;
import engineering.oddsystems.cinema.reservation.domain.ShowId;

public class ShowEntity extends
    EventSourcedBehaviorWithEnforcedReplies<ShowEntityCommand, ShowEvent, Show> {

  public static final EntityTypeKey<ShowEntityCommand> SHOW_ENTITY_TYPE_KEY =
      EntityTypeKey.create(ShowEntityCommand.class, "Show");
  private final ShowId showId;
  private final Clock clock;
  private final ActorContext<ShowEntityCommand> context;

  private ShowEntity(PersistenceId persistenceId, ShowId showId, Clock clock,
      ActorContext<ShowEntityCommand> context) {
    super(persistenceId);
    this.showId = showId;
    this.clock = clock;
    this.context = context;
  }

  public static Behavior<ShowEntityCommand> create(ShowId showId, Clock clock) {
    return Behaviors.setup(context -> {
      PersistenceId persistenceId = PersistenceId.of("Show", showId.id().toString());
      context.getLog().info("ShowEntity {} initialization started", showId);
      return new ShowEntity(persistenceId, showId, clock, context);
    });
  }

  @Override
  public Show emptyState() {
    return Show.create(showId);
  }

  @Override
  public CommandHandlerWithReply<ShowEntityCommand, ShowEvent, Show> commandHandler() {
    return newCommandHandlerWithReplyBuilder().forStateType(Show.class)
        .onCommand(ShowEntityCommand.GetShow.class, this::returnState)
        .onCommand(ShowEntityCommand.ShowCommandEnvelope.class, this::handleShowCommand)
        .build();
  }

  private ReplyEffect<ShowEvent, Show> handleShowCommand(Show show,
      ShowEntityCommand.ShowCommandEnvelope envelope) {
    ShowCommand command = envelope.command();
    return show.process(command, clock).fold(
        error -> {
          context.getLog().info("Command rejected: {} with {}", command, error);
          return Effect().reply(envelope.replyTo(), new CommandRejected(error));
        },
        events -> {
          context.getLog().debug("Command handled: {}", command);
          return Effect().persist(events.toJavaList())
              .thenReply(envelope.replyTo(), s -> new CommandProcessed());
        }
    );
  }

  private ReplyEffect<ShowEvent, Show> returnState(Show show, ShowEntityCommand.GetShow getShow) {
    return Effect().reply(getShow.replyTo(), show);
  }

  @Override
  public EventHandler<Show, ShowEvent> eventHandler() {
    return newEventHandlerBuilder()
        .forStateType(Show.class)
        .onAnyEvent(Show::apply);
  }

}
