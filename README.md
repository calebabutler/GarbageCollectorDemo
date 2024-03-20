GarbageCollectorDemo
====================

This is a very simple Mark-and-sweep garbage collector that comes with a demo in GarbageCollectorMain. It is not very useful in practice, but it shows a simple method a garbage collector can work to free unseen memory automatically for a program.

The root nodes are a list of variables given by variable names. This is essentially what the program can "see" (or has direct access to) within the memory. It is analogous to a program's list of variables.

The only thing this garbage collector can allocate is a Node. A Node, in this context, is an object containing two things: an arbitrary 32-bit integer, and a 32-bit pointer to another Node in the same memory. If this pointer equals 0, then it is a "null" pointer, meaning it points to nothing.
