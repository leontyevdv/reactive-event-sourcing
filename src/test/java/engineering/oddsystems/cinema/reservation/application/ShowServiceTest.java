package engineering.oddsystems.cinema.reservation.application;

import static engineering.oddsystems.cinema.reservation.domain.DomainGenerators.randomSeatNumber;
import static org.assertj.core.api.Assertions.assertThat;

import akka.actor.ActorSystem;
import akka.actor.typed.javadsl.Adapter;
import akka.cluster.sharding.typed.javadsl.ClusterSharding;
import akka.persistence.testkit.PersistenceTestKitPlugin;
import akka.testkit.javadsl.TestKit;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import engineering.oddsystems.cinema.base.domain.Clock;
import engineering.oddsystems.cinema.reservation.domain.ShowId;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

class ShowServiceTest {

  private static final Config config = PersistenceTestKitPlugin.config()
      .withFallback(ConfigFactory.load());
  private static final ActorSystem system = ActorSystem.create("oddsystems-cinema", config);
  private final ClusterSharding sharding = ClusterSharding.get(Adapter.toTyped(system));
  private final Clock clock = new Clock.UtcClock();
  private final ShowService showService = new ShowService(sharding, clock);

  @AfterAll
  public static void cleanUp() {
    TestKit.shutdownActorSystem(system);
  }

  @Test
  public void shouldReserveSeat() throws ExecutionException, InterruptedException {
    //given
    var showId = ShowId.of();
    var seatNumber = randomSeatNumber();

    //when
    var result = showService.reserveSeat(showId, seatNumber).toCompletableFuture().get();

    //then
    assertThat(result).isInstanceOf(ShowEntityResponse.CommandProcessed.class);
  }

  @Test
  public void shouldCancelReservation() throws ExecutionException, InterruptedException {
    //given
    var showId = ShowId.of();
    var seatNumber = randomSeatNumber();

    //when
    var reservationResult = showService.reserveSeat(showId, seatNumber).toCompletableFuture().get();

    //then
    assertThat(reservationResult).isInstanceOf(ShowEntityResponse.CommandProcessed.class);

    //when
    var cancellationResult = showService.cancelReservation(showId, seatNumber).toCompletableFuture()
        .get();

    //then
    assertThat(cancellationResult).isInstanceOf(ShowEntityResponse.CommandProcessed.class);
  }

  @Test
  public void shouldFindShowById() throws ExecutionException, InterruptedException {
    //given
    var showId = ShowId.of();

    //when
    var show = showService.findShowBy(showId).toCompletableFuture().get();

    //then
    assertThat(show.id()).isEqualTo(showId);
  }


}