package engineering.oddsystems.cinema.reservation.domain;

import static engineering.oddsystems.cinema.reservation.domain.DomainGenerators.randomPrice;
import static engineering.oddsystems.cinema.reservation.domain.DomainGenerators.randomShowId;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

public class ShowBuilder {

  private ShowId id = randomShowId();
  private String title = "Random title";
  private Map<SeatNumber, Seat> seats = HashMap.empty();

  public static ShowBuilder showBuilder() {
    return new ShowBuilder();
  }

  public ShowBuilder withRandomSeats() {
    seats = SeatsCreator.createSeats(randomPrice());
    return this;
  }

  public ShowBuilder withSeat(Seat seat) {
    seats = seats.put(seat.number(), seat);
    return this;
  }

  public Show build() {
    return new Show(id, title, seats);
  }

}
