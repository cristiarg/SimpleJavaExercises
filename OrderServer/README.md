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
The implementation in `com\sacom\order\processing` uses a thread pool to which it supplies work bits. Each incoming order is further dispatched to the thread pool where the order is processed. Each resulting order document is further dispatched on its own to the outside-injected `dispatcher`.
## Dispatcher
The implementation in `com\sacom\order\dispatcher\filesystem` handles the writing the processed orders to the file system as XML document.

It is also implemented by means of a thread pool where incoming `order descriptions` are being submitted to. 

Likewise, this implementation ca be easily replaced by another that which uses other means to process orders (i.e.: database, network submission, ..)

## Pipeline communication
Communication between entities happens by means of messages. For the moment there is only one message type -  `com\sacom\order\common\OrderDescription` - a key/value map augmented by a `nature` string value whose semantics is defined as an internal application protocol.

## TODO's
- **Everywhere**: appropriate error reporting from the asynchronously executing entities
- **XML Handling**: polishing/error handling
- **Structure**: replace the pipeline-like connections by a sort of 'message queue' entity + a publisher/subscriber mechanism for the entities performing parts of the processing
- **Receiver**: implement a logic to process files alredy found in the incoming location (tricky)
- **Receiver**: more asynchronicity could be implemented - i.e.: decouple polling and checking/dispatching
- **Processing**: implement cleanup of incoming orders if so specified

