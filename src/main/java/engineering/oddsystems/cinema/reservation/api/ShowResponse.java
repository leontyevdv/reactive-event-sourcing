package engineering.oddsystems.cinema.reservation.api;

import engineering.oddsystems.cinema.reservation.domain.Show;
import java.util.List;

public record ShowResponse(String id, String title, List<SeatResponse> seats) {

  public static ShowResponse from(Show show) {
    return new ShowResponse(show.id().id().toString(), show.title(),
        show.seats().values().map(SeatResponse::from).toJavaList());
  }

}
