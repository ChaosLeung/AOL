# Android Object Layout (AOL)

AOL is a tool for analyze object layout in ART.

## Usage

### Command Line

#### 1. Install aol cli to device

``` shell
./aol.sh -i cli.apk
```

#### 2. Run

``` shell
# Instance layout (instance fields)
./aol.sh -o instance -c java.util.HashMap
# Class layout (static fields)
./aol.sh -o class -c java.util.HashMap
```

#### Command Line Usage

```
Usage:
    -i install aol cli to device
    -s device serial
    -o option of aol, one of [class, instance]
    -c class name
    -h help
```

### Kotlin

Show the layout:

``` kotlin
val layout = ObjectLayout.parse(HashMap::class.java)
layout.dumpToLog(classInfo = false) // Show instance layout (instance fields)
layout.dumpToLog(classInfo = true) // Show class layout (static fields)
```


## Output

The output is similar to the "[internals](https://github.com/openjdk/jol#internals)" of [JOL](https://github.com/openjdk/jol)

### Instance Layout

```
java.util.HashMap object internals:
 OFFSET  SIZE         DECLARING_CLASS                               TYPE  DESCRIPTION
      0     4        java.lang.Object                    java.lang.Class  shadow$_klass_
      4     4        java.lang.Object                                int  shadow$_monitor_
      8     4   java.util.AbstractMap                      java.util.Set  keySet
     12     4   java.util.AbstractMap               java.util.Collection  valuesCollection
     16     4       java.util.HashMap     java.util.HashMap$HashMapEntry  entryForNullKey
     20     4       java.util.HashMap                      java.util.Set  entrySet
     24     4       java.util.HashMap                      java.util.Set  keySet
     28     4       java.util.HashMap   java.util.HashMap$HashMapEntry[]  table
     32     4       java.util.HashMap               java.util.Collection  values
     36     4       java.util.HashMap                                int  modCount
     40     4       java.util.HashMap                                int  size
     44     4       java.util.HashMap                                int  threshold
Instance size: 48 bytes
```

### Class Layout

```
java.util.HashMap class internals:
 OFFSET  SIZE     DECLARING_CLASS                          TYPE  DESCRIPTION
      0     4    java.lang.Object               java.lang.Class  shadow$_klass_
      4     4    java.lang.Object                           int  shadow$_monitor_
      8     4     java.lang.Class         java.lang.ClassLoader  classLoader
     12     4     java.lang.Class               java.lang.Class  componentType
     16     4     java.lang.Class            java.lang.DexCache  dexCache
     20     4     java.lang.Class            java.lang.String[]  dexCacheStrings
     24     4     java.lang.Class            java.lang.Object[]  ifTable
     28     4     java.lang.Class              java.lang.String  name
     32     4     java.lang.Class               java.lang.Class  superClass
     36     4     java.lang.Class               java.lang.Class  verifyErrorClass
     40     4     java.lang.Class              java.lang.Object  vtable
     44     4     java.lang.Class                           int  accessFlags
     48     8     java.lang.Class                          long  directMethods
     56     8     java.lang.Class                          long  iFields
     64     8     java.lang.Class                          long  sFields
     72     8     java.lang.Class                          long  virtualMethods
     80     4     java.lang.Class                           int  classSize
     84     4     java.lang.Class                           int  clinitThreadId
     88     4     java.lang.Class                           int  dexClassDefIndex
     92     4     java.lang.Class                           int  dexTypeIndex
     96     4     java.lang.Class                           int  numDirectMethods
    100     4     java.lang.Class                           int  numInstanceFields
    104     4     java.lang.Class                           int  numReferenceInstanceFields
    108     4     java.lang.Class                           int  numReferenceStaticFields
    112     4     java.lang.Class                           int  numStaticFields
    116     4     java.lang.Class                           int  numVirtualMethods
    120     4     java.lang.Class                           int  objectSize
    124     4     java.lang.Class                           int  primitiveType
    128     4     java.lang.Class                           int  referenceInstanceOffsets
    132     4     java.lang.Class                           int  status
    136   392                                                    (alignment/padding gap)
    528     4   java.util.HashMap         java.util.Map$Entry[]  EMPTY_TABLE
    532     4   java.util.HashMap   java.io.ObjectStreamField[]  serialPersistentFields
    536     8   java.util.HashMap                          long  serialVersionUID
    544     4   java.util.HashMap                           int  MAXIMUM_CAPACITY
    548     4   java.util.HashMap                           int  MINIMUM_CAPACITY
    552     4   java.util.HashMap                         float  DEFAULT_LOAD_FACTOR
Class size: 556 bytes
Space losses: 392 bytes internal + 0 bytes external = 392 bytes total
```