# 4. Hide ShowEntity under ShowService

Date: 2022-06-04

## Status

Accepted

## Context

We have ShowEntity and we want to hide it under a service wrapper to prevent using it from different
parts of the code. ShowService will be our port from the [Hexagonal Architecture](https://netflixtechblog.com/ready-for-changes-with-hexagonal-architecture-b315ec967749) 
point of view, ready to use by any adapter in the future.

## Decision

Wrap ShowEntity calls into ShowService.

## Consequences

Swappable adapters
