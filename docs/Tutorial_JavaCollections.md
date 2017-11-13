Storing and retrieving things.

## Interfaces and Implementation

When working with Java's collection classes you will notice that there is a difference between a collection interface and the implementation.

When writing methods it is best to use the interface for return and argument types.

# The three most common collections

## Arrays and Lists

Store linear sequences of values.

Arrays have a fixed size.

Lists are dynamically sized.

```java

List<Integer> myList = new ArrayList<>();

// add numbers to the list
for (int i = 0; i < 10; ++i) {
    myList.add(i*i);
}

// fetch numbers from the list
for (int i = 0; i < 10; ++i) {
    int number = myList.get(i);
}

// iterate over every number in the list
for (int i : myList) {
    System.out.println(i);
}
```

The List interface is implemented by two classes in Java's standard library: `ArrayList` and `LinkedList`. There are important differences in the implementation and performance of the two implementations that are beyond the scope of this document. At any rate `ArrayList` provides a good general purpose implementation.

`List` documentation can be found here http://docs.oracle.com/javase/7/docs/api/java/util/List.html

## Maps

Maps associate a key with a value. Maps are otherwise known as dictionaries.


```java

Map<String, String> phoneNumbers = new HashMap<>();

// store numbers
phoneNumbers.put("Anna", "555-543-323");
phoneNumbers.put("Bert", "555-143-765");
phoneNumbers.put("Carl", "555-654-454");

// get Anna's number
String annasNumber = phoneNumbers.get("Anna");

```

`Map` is implemented by `HashMap`

## Sets

A set stores values without order. Sets can contain values only once. Sets provide mechanisms to quickly check if a value is included or not.


```java

Set<String> mySet = new HashSet<>();


```

`Set` is implemented by `HashSet` in Java's standard library.

