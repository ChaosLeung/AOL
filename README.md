# Android Object Layout (AOL)

AOL is a tool for analyze object layout in ART.

## Usage

Show the instance layout:

``` java
InstanceLayout.parseInstance("1234567890").dumpToLog()
```

Show the instance layout of instances of given class:

``` java
InstanceLayout.parseClass(Class.forName("java.util.HashMap\$Node")).dumpToLog()
```

## Output

The output is similar to the "[internals](https://github.com/openjdk/jol#internals)" of [JOL](https://github.com/openjdk/jol)

```
java.util.HashMap$Node object internals:
 OFFSET  SIZE                     TYPE  DESCRIPTION                               VALUE
      0     8                           (object header)                           N/A
      8     4         java.lang.Object  Node.key                                  N/A
     12     4   java.util.HashMap.Node  Node.next                                 N/A
     16     4         java.lang.Object  Node.value                                N/A
     20     4                           (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
```