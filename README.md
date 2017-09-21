# Graphite Kotlin Client

Simple Graphite for Kotlin and Java. Right now only supported protocol is plaintext. 

> **NOTE** Right now the client is not released in Maven central and you need to build it manually by cloning the repostory 
> and running `mvn clean install`

### Add Maven dependency

```xml
<dependency>
    <groupId>com.mihkels.graphite</groupId>
    <artifactId>graphite-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### Java usage sample

```java
import com.mihkels.graphite.client
import java.time.Instant;

public class Demo {
    public static void main(String... args) {
        GraphiteClientProperties properties = new GraphiteClientProperties();
        properties.setHostname("localhost");
        properties.setPort(2003);
        properties.setMetricPrefix("central-logging");

        GraphiteClient client = new BasicGraphiteClient(properties, new SocketSender());
        client.send(new GraphiteMetric("hello", "1", Instant.now().toEpochMilli()));
    }
}
```

### Kotlin  usage sample 

```kotlin
fun main(args: Array<String>) {
    // Set up properties
    val sender = SocketSender()
    val properties = GraphiteClientProperties("localhost", 2003, "prefix")
    
    // Create client 
    val graphiteClient = BasicGraphiteClient(properties, sender)
    
    // Create metric 
    val timestamp = Instant.now().toEpochMilli()
    val metric = GraphiteMetric("demo.metric.sample", "13", epoch)
    
    // Send out metric
    graphiteClient.send(metric)
}
```
