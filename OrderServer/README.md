## Description
Implemented the specification as a processing pipeline:
`Receiver -> Processing -> Dispatcher`
, orchestrated from `Main`.
Each entity aims to perform its processing in a non-blocking manner: receving work and processing it asynchronously. In this regard, each entity can be considered a/or, at least, easily refactored into a microservice.

Each entity should implement a minimal/generic interface - `LifeCycle` and should be configurable by means of settings containers.

Entities that are not at the fore-front of the pipeline should also implement the interface `OrderDispatcher` sucha that they can receive work from other entities.

## Receiver - file system receiver
The implementation in `com.sacom.order.receiver.filesystem` launches, upon start, a monitoring infrastructure to continuosly poll a file system directory for newly appearing files that meet certain criteria (their name matches a regex pattern).

Once a suitable file is found, it is dispatched to the outside-injected `dispatcher`. 

Needless to say that other implementations can be put in place (i.e.: web services, protobuf, raw sockets, REST, ..). These other implementations can even be assembled together with the existing implementation as part of the pipeline.
## Processing
The implementation in `com\sacom\order\processing` uses a thread pool to which it supplies work bits. Each incoming order is further dispatched to the thread pool where the order is processed. Each resulting separated order document is further dispatched to the outside-injected `dispatcher`.
 ## Dispatcher
 TODO

## Pipeline communication
TODO

## TODO's
- **Everywhere**: appropriate error reporting from the asynchronously executing entities
- **Structure**: replace the pipeline-like connections by a sort of 'message queue' entity + a publisher/subscriber mechanism for the entities performing parts of the processing
- **Receiver**: implement a logic to process files alredy found in the incoming location (tricky)
- **Receiver**: more asynchronicity could be implemented - i.e.: decouple polling and checking/dispatching
- **XML Handling**: polishing/error handling
- **Processing**: implement cleanup of incoming orders if so specified

