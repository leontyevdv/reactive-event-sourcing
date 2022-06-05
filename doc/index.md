# Architectural Decision Log

This log lists the architectural decisions for the application that allows to sell cinema show tickets. 
The application is implemented using Event Sourcing pattern. 
Find the series of articles on which this repository is based [here](https://softwaremill.com/reactive-event-sourcing-in-java-part-1-domain/).

- [ADR-0001](adr/0001-record-architecture-decisions.md) - Record Architecture Decisions
- [ADR-0002](adr/0002-implement-a-service-for-selling-tickets-to-cinema-shows-using-event-sourcing.md) - Implement a service for selling tickets to cinema shows using Event Sourcing
- [ADR-0003](adr/0003-use-typed-akka-api-for-concurrent-modification-of-aggregate.md) - Use Typed Akka API for concurrent modification of Aggregate
- [ADR-0004](adr/0004-hide-showentity-under-showservice.md) - Hide ShowEntity under ShowService
- [ADR-0005](adr/0005-use-postgresql-or-cassandra-as-an-event-journal.md) - Use PostgreSQL or Cassandra as an event journal

For new ADRs, please use [adr-tools](https://github.com/npryce/adr-tools).

For general information about architectural decision records see [Michael Nygard's article](http://thinkrelevance.com/blog/2011/11/15/documenting-architecture-decisions).