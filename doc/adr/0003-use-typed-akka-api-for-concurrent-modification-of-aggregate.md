# 3. Use Typed Akka API for concurrent modification of Aggregate

Date: 2022-06-04

## Status

Accepted

## Context

We want to achieve the guarantee that in case of reserving the same seat concurrently, one request will 
succeed and one will fail.

## Decision

Use Akka Actors to handle concurrency.

## Consequences

The application is able to work in concurrent environment.
The application is scalable.
