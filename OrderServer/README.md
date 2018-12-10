# Description
Implemented the specification as a sort of pipeline
`Receiver -> Processing -> Dispatcher`
orchestrated from `Main`.

# Receiver
TODO

# Processing
 TODO
 
 # Dispatcher
 TODO

# TODO's
- **Everywhere**: better error reporting from the asynchronously executing entities
- **Structure**: replace the pipeline-like connections by a sort of 'message queue' entity + a publisher/subscriber mechanism for the entities performing part of the processing
- **Receiver**: implement a logic to process files alredy found in the incoming location (tricky)
