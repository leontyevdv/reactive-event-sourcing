package engineering.oddsystems.cinema.reservation;

import akka.cluster.sharding.typed.javadsl.ClusterSharding;
import engineering.oddsystems.cinema.base.domain.Clock;
import engineering.oddsystems.cinema.reservation.application.ShowService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReservationConfiguration {

  @Bean
  public ShowService showService(ClusterSharding sharding, Clock clock) {
    return new ShowService(sharding, clock);
  }
}
