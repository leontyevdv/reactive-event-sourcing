package engineering.oddsystems.cinema.base;

import akka.actor.typed.ActorSystem;
import akka.cluster.sharding.typed.javadsl.ClusterSharding;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import engineering.oddsystems.cinema.base.application.VoidBehavior;
import engineering.oddsystems.cinema.base.domain.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseConfiguration {

  @Bean
  public Config config() {
    return ConfigFactory.load();
  }

  @Bean(destroyMethod = "terminate")
  public ActorSystem<Void> actorSystem(Config config) {
    return ActorSystem.create(VoidBehavior.create(), "oddsystems-cinema", config);
  }

  @Bean
  public ClusterSharding clusterSharding(ActorSystem<?> actorSystem) {
    return ClusterSharding.get(actorSystem);
  }

  @Bean
  Clock clock() {
    return new Clock.UtcClock();
  }
}
