# 2. Implement a service for selling tickets to cinema shows using Event Sourcing

Date: 2022-06-04

## Status

Accepted

## Context

We are creating an application for selling tickets to cinema shows.

## Decision

In our naive implementation, the state is modeled as the Show aggregate, which represents a cinema show, 
where you can reserve your seats. We will be using 3 main building blocks:
- Commands — define what we want to happen in the system,
- State — it’s usually an aggregate from the DDD approach, which is responsible for keeping some part of the system consistent and valid (aggregate invariants)
- Events — capture what has happened in the system.

The Show aggregate provides 2 entry point methods:
- List process(Command command)
- State apply(Event event)

### Tech decisions
- Use Java 17
- Use Spring Boot
- Use Vavr library

## Consequences

- Consistent and scalable applications without optimistic (or pessimistic) locking
