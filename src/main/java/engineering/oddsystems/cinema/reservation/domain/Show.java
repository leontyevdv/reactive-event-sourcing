package engineering.oddsystems.cinema.reservation.domain;

import static engineering.oddsystems.cinema.reservation.domain.ShowCommandError.SEAT_NOT_AVAILABLE;
import static engineering.oddsystems.cinema.reservation.domain.ShowCommandError.SEAT_NOT_EXISTS;
import static engineering.oddsystems.cinema.reservation.domain.ShowCommandError.SEAT_NOT_RESERVED;
import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

import engineering.oddsystems.cinema.base.domain.Clock;
import engineering.oddsystems.cinema.reservation.domain.ShowCommand.CancelSeatReservation;
import engineering.oddsystems.cinema.reservation.domain.ShowCommand.ReserveSeat;
import engineering.oddsystems.cinema.reservation.domain.ShowEvent.SeatReservationCancelled;
import engineering.oddsystems.cinema.reservation.domain.ShowEvent.SeatReserved;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Either;
import java.io.Serializable;
import java.math.BigDecimal;

public record Show(ShowId id, String title, Map<SeatNumber, Seat> seats) implements Serializable {

  public static final BigDecimal INITIAL_PRICE = new BigDecimal("100");

  public static Show create(ShowId showId) {
    return new Show(showId, "Show title " + showId.id(), SeatsCreator.createSeats(INITIAL_PRICE));
  }

  public Either<ShowCommandError, List<ShowEvent>> process(ShowCommand command, Clock clock) {
    return switch (command) {
      case ReserveSeat reserveSeat -> handleReservation(reserveSeat, clock);
      case CancelSeatReservation cancelSeatReservation ->
          handleCancellation(cancelSeatReservation, clock);
    };
  }

  private Either<ShowCommandError, List<ShowEvent>> handleReservation(ReserveSeat reserveSeat,
      Clock clock) {
    SeatNumber seatNumber = reserveSeat.seatNumber();
    return seats.get(seatNumber).<Either<ShowCommandError, List<ShowEvent>>>map(seat -> {
      if (seat.isAvailable()) {
        return right(List.of(new SeatReserved(id, clock.now(), seatNumber)));
      } else {
        return left(SEAT_NOT_AVAILABLE);
      }
    }).getOrElse(left(SEAT_NOT_EXISTS));
  }

  private Either<ShowCommandError, List<ShowEvent>> handleCancellation(
      CancelSeatReservation cancelSeatReservation, Clock clock) {
    SeatNumber seatNumber = cancelSeatReservation.seatNumber();
    return seats.get(seatNumber).<Either<ShowCommandError, List<ShowEvent>>>map(seat -> {
      if (seat.isReserved()) {
        return right(List.of(new SeatReservationCancelled(id, clock.now(), seatNumber)));
      } else {
        return left(SEAT_NOT_RESERVED);
      }
    }).getOrElse(left(SEAT_NOT_EXISTS));
  }

  public Show apply(ShowEvent event) {
    return switch (event) {
      case SeatReserved seatReserved -> applyReserved(seatReserved);
      case SeatReservationCancelled seatReservationCancelled ->
          applyReservationCancelled(seatReservationCancelled);
    };
  }

  private Show applyReservationCancelled(SeatReservationCancelled seatReservationCancelled) {
    Seat seat = getSeatOrThrow(seatReservationCancelled.seatNumber());
    return new Show(id, title, seats.put(seat.number(), seat.available()));
  }

  private Show applyReserved(SeatReserved seatReserved) {
    Seat seat = getSeatOrThrow(seatReserved.seatNumber());
    return new Show(id, title, seats.put(seat.number(), seat.reserved()));
  }

  private Seat getSeatOrThrow(SeatNumber seatNumber) {
    return seats.get(seatNumber).getOrElseThrow(
        () -> new IllegalStateException("Seat not exists %s".formatted(seatNumber)));
  }

}
