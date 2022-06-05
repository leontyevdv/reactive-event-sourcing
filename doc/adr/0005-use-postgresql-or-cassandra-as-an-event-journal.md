# 5. Use PostgreSQL or Cassandra as an event journal

Date: 2022-06-05

## Status

Accepted

## Context

We want to store events to be able to restore the state of aggregates.

## Decision

Use PostgreSQL, JDBC driver + Slick library as a reactive facade for the non-reactive driver.
At some level, a single host database will not be enough. Vertical scaling will hit its limits and 
to handle more load, we will need to switch to something distributed. With Apache Cassandra we will get:
- partitioning (data is spread across all nodes in the cluster),
- replication (in case of a node failure, we can use a replica),
- optimized for writes (the throughput is really great),
- near-linear horizontal scaling.

Advantages:
- Partitions are not too small, but also not too big (we can configure how many events should go to a single partition). 
- With such a schema, reading events for a given aggregate will be very efficient (important for a recovery phase). 
- The reactive driver for the communication will suit our design very well.

Disadvantages:
- Maintaining a distributed event store on production is something completely different from a single host database. It will require a lot of DevOps power and knowledge (or money in case of hosted solution).
- 

## Consequences

Events are persistent.
