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
 OFFSET  SIZE   DECLARING_CLASS             TYPE  FIELD
      0     4            Object            Class  shadow$_klass_
      4     4            Object              int  shadow$_monitor_
      8     4       AbstractMap              Set  keySet
     12     4       AbstractMap       Collection  values
     16     4           HashMap              Set  entrySet
     20     4           HashMap   HashMap$Node[]  table
     24     4           HashMap              int  modCount
     28     4           HashMap              int  size
     32     4           HashMap              int  threshold
     36     4           HashMap            float  loadFactor
Instance size: 40 bytes
```

### Class Layout

```
java.util.HashMap class internals:
 OFFSET  SIZE   DECLARING_CLASS          TYPE  FIELD
      0     4            Object         Class  shadow$_klass_
      4     4            Object           int  shadow$_monitor_
      8     4             Class   ClassLoader  classLoader
     12     4             Class         Class  componentType
     16     4             Class        Object  dexCache
     20     4             Class      ClassExt  extData
     24     4             Class      Object[]  ifTable
     28     4             Class        String  name
     32     4             Class         Class  superClass
     36     4             Class        Object  vtable
     40     8             Class          long  iFields
     48     8             Class          long  methods
     56     8             Class          long  sFields
     64     4             Class           int  accessFlags
     68     4             Class           int  classFlags
     72     4             Class           int  classSize
     76     4             Class           int  clinitThreadId
     80     4             Class           int  dexClassDefIndex
     84     4             Class           int  dexTypeIndex
     88     4             Class           int  numReferenceInstanceFields
     92     4             Class           int  numReferenceStaticFields
     96     4             Class           int  objectSize
    100     4             Class           int  objectSizeAllocFastPath
    104     4             Class           int  primitiveType
    108     4             Class           int  referenceInstanceOffsets
    112     4             Class           int  status
    116     2             Class         short  copiedMethodsOffset
    118     2             Class         short  virtualMethodsOffset
    120   424                                  (alignment/padding gap)
    544     8           HashMap          long  serialVersionUID
    552     4           HashMap           int  DEFAULT_INITIAL_CAPACITY
    556     4           HashMap           int  MAXIMUM_CAPACITY
    560     4           HashMap           int  MIN_TREEIFY_CAPACITY
    564     4           HashMap           int  TREEIFY_THRESHOLD
    568     4           HashMap           int  UNTREEIFY_THRESHOLD
    572     4           HashMap         float  DEFAULT_LOAD_FACTOR
Class size: 576 bytes
Space losses: 424 bytes internal + 0 bytes external = 424 bytes total
```