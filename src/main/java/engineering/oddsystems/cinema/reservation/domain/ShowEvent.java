package engineering.oddsystems.cinema.reservation.domain;

import java.io.Serializable;
import java.time.Instant;

public sealed interface ShowEvent extends Serializable {

  ShowId showId();

  Instant createdAt();

  record SeatReserved(ShowId showId, Instant createdAt, SeatNumber seatNumber) implements
      ShowEvent {

  }

  record SeatReservationCancelled(ShowId showId, Instant createdAt,
                                  SeatNumber seatNumber) implements ShowEvent {

  }

}
