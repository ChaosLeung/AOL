# Android Object Layout (AOL)

AOL is a tool for analyze object layout in ART.

## Usage

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
 OFFSET  SIZE     DECLARING_CLASS                            TYPE  DESCRIPTION
      0     4    java.lang.Object                 java.lang.Class  shadow$_klass_
      4     4    java.lang.Object                             int  shadow$_monitor_
      8     4     java.lang.Class           java.lang.ClassLoader  classLoader
     12     4     java.lang.Class                 java.lang.Class  componentType
     16     4     java.lang.Class              java.lang.DexCache  dexCache
     20     4     java.lang.Class              java.lang.String[]  dexCacheStrings
     24     4     java.lang.Class   java.lang.reflect.ArtMethod[]  directMethods
     28     4     java.lang.Class    java.lang.reflect.ArtField[]  iFields
     32     4     java.lang.Class              java.lang.Object[]  ifTable
     36     4     java.lang.Class                java.lang.String  name
     40     4     java.lang.Class    java.lang.reflect.ArtField[]  sFields
     44     4     java.lang.Class                 java.lang.Class  superClass
     48     4     java.lang.Class                 java.lang.Class  verifyErrorClass
     52     4     java.lang.Class   java.lang.reflect.ArtMethod[]  virtualMethods
     56     4     java.lang.Class   java.lang.reflect.ArtMethod[]  vtable
     60     4     java.lang.Class                             int  accessFlags
     64     4     java.lang.Class                             int  classSize
     68     4     java.lang.Class                             int  clinitThreadId
     72     4     java.lang.Class                             int  dexClassDefIndex
     76     4     java.lang.Class                             int  dexTypeIndex
     80     4     java.lang.Class                             int  numReferenceInstanceFields
     84     4     java.lang.Class                             int  numReferenceStaticFields
     88     4     java.lang.Class                             int  objectSize
     92     4     java.lang.Class                             int  primitiveType
     96     4     java.lang.Class                             int  referenceInstanceOffsets
    100     4     java.lang.Class                             int  referenceStaticOffsets
    104     4     java.lang.Class                             int  status
    108   392                                                      (alignment/padding gap)
    500     4   java.util.HashMap           java.util.Map$Entry[]  EMPTY_TABLE
    504     4   java.util.HashMap     java.io.ObjectStreamField[]  serialPersistentFields
    508     4   java.util.HashMap                             int  MAXIMUM_CAPACITY
    512     8   java.util.HashMap                            long  serialVersionUID
    520     4   java.util.HashMap                             int  MINIMUM_CAPACITY
    524     4   java.util.HashMap                           float  DEFAULT_LOAD_FACTOR
Class size: 528 bytes
Space losses: 392 bytes internal + 0 bytes external = 392 bytes total
```