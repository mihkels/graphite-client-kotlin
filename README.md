# Graphite Kotlin Client

Simple Graphite JMX metrics pushing client written in Kotlin.

### Add Maven dependency

```xml
<dependency>
    <groupId>com.mihkels.graphite</groupId>
    <artifactId>graphite-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### Usage 

```kotlin
fun main(args: Array<String>) {
    val graphiteClient = BasicGraphiteClient(GraphiteSettings("localhost", 2003))
    val epoch = Instant.now().toEpochMilli()
    val metric = GraphiteMetric("demo.metric.sample", "13", epoch)

    graphiteClient.send(metric)
}
```