# What is publisher confirms in Rabbit-MQ and when use it?
## Broker acknowledgments to publishers are a protocol extension to AMQP 0-9-1, called publisher confirms.

### What is it?
Using standard AMQP 0-9-1, the only way to guarantee that a message isn't lost is by using transactions.
make the channel transactional then for each message or set of messages publish, commit.
Transactions are unnecessarily heavyweight and decrease throughput by a factor of 250. To remedy this,
a confirmation mechanism was introduced.

### When use it?
Publish confirm has a performance impact, keep in mind that it is required if the publisher needs
at least once processing of messages.

### Appropriate actions for confirm callback.
You can do logging or enqueuing nack-ed messages.
It can be tempting to re-publish a nack-ed message from the corresponding callback but this should be avoided,
as confirm callbacks are dispatched in an I/O thread where channels are not supposed to do operations.
A better solution consists in enqueuing the message in an in-memory queue which is polled by a publishing thread.






