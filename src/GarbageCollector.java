
/*
 * This is a very simple Mark-and-sweep garbage collector. It is not very
 * useful in practice, but it shows a simple method a garbage collector can
 * work to free unseen memory automatically for a program.
 * 
 * The root nodes are a list of variables given by variable names. This is
 * essentially what the program can "see" (or has direct access to) within the
 * memory. It is analogous to a program's list of variables.
 * 
 * The only thing this garbage collector can allocate is a Node. A Node, in
 * this context, is an object containing two things: an arbitrary 32-bit
 * integer, and a 32-bit pointer to another Node in the same memory. If this
 * pointer equals 0, then it is a "null" pointer, meaning points to nothing.
 */

import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class GarbageCollector {

    /*
     * This is what the heap would be if we were using a non-garbage-collected
     * language (like C, or C++). Since Java has its own garbage collector, we
     * cannot use the heap itself, as it would be pointless since Java handles
     * that memory automatically for us anyway.
     */
    private int[] simulatedHeap;
    private final int HEAP_SIZE = 20;

    /*
     * These are the root nodes, essentially what the program can "see" or to
     * what areas of memory the program has direct access.
     */
    private HashMap<String, Integer> variableNames;

    /*
     * These are all allocated nodes. Anything not in this map is "free", and
     * can be reused by other variables. The boolean associated with each
     * pointer are for internal use, for when the garbage collector is marking
     * every pointer as "seen" or "unseen."
     */
    private HashMap<Integer, Boolean> allocatedNodes;

    public GarbageCollector() {
        simulatedHeap = new int[HEAP_SIZE];
        variableNames = new HashMap<>();
        allocatedNodes = new HashMap<>();
    }

    /*
     * This method simply checks if the address exists within the
     * allocatedNodes table.
     */
    private boolean isAllocated(int address) {
        if (address == 0) {
            return true;
        }
        return allocatedNodes.keySet().contains(address);
    }

    /*
     * This method does the mark-and-sweep process to "free" unused memory. It
     * is called when the allocateNode method runs out of memory.
     */
    private void collectGarbage() {
        for (int key : allocatedNodes.keySet()) {
            allocatedNodes.put(key, false);
        }

        for (String variable : variableNames.keySet()) {
            int address = variableNames.get(variable);
            while (address != 0) {
                allocatedNodes.put(address, true);
                address = getNodeLink(address);
            }
        }

        Set<Integer> keys = new HashSet<>(allocatedNodes.keySet());
        for (int key : keys) {
            if (!allocatedNodes.get(key)) {
                allocatedNodes.remove(key);
                System.out.println("Deallocating address " + key + " !");
            }
        }
    }

    /*
     * This method allocates a new node in the simulatedHeap and returns a
     * pointer. It can throw an exception if there is no available memory,
     * even after running through a mark-and-sweep run.
     */
    public int allocateNode() {
        int address = 2;
        boolean garbageCollected = false;

        while (isAllocated(address)) {
            address += 2;
            if (address > HEAP_SIZE - 2) {
                if (garbageCollected) {
                    throw new OOMException("Out of memory!");
                } else {
                    collectGarbage();
                    address = 2;
                    garbageCollected = true;
                }
            }
        }
        allocatedNodes.put(address, false);
        simulatedHeap[address] = 0;
        simulatedHeap[address + 1] = 0;
        return address;
    }

    /*
     * This method assigns an address to a variable.
     */
    public void setVariable(String variableName, int address) {
        if (isAllocated(address)) {
            variableNames.put(variableName, address);
        } else {
            throw new IllegalArgumentException("Address not allocated.");
        }
    }

    /*
     * This method returns an address given a variable.
     */
    public int getVariable(String variableName) {
        Integer value = variableNames.get(variableName);
        if (value == null) {
            throw new IllegalArgumentException("Variable name not previously set.");
        }
        return value;
    }

    /*
     * This method removes a variable from the variable table.
     */
    public void removeVariable(String variableName) {
        variableNames.remove(variableName);
    }

    /*
     * This method returns the value portion of a node given the address.
     */
    public int getNodeValue(int address) {
        if (isAllocated(address)) {
            return simulatedHeap[address];
        }
        throw new IllegalArgumentException("Address not allocated.");
    }

    /*
     * This method sets the value portion of the node at the given address.
     */
    public void setNodeValue(int address, int value) {
        if (isAllocated(address)) {
            simulatedHeap[address] = value;
        } else {
            throw new IllegalArgumentException("Address not allocated.");
        }
    }

    /*
     * This method returns the link (or pointer/address) portion of the node
     * at the given address.
     */
    public int getNodeLink(int address) {
        if (isAllocated(address)) {
            int returnAddress = simulatedHeap[address + 1];
            if (!isAllocated(returnAddress)) {
                throw new RuntimeException("Unexpected behavior, likely bug in garbage collector");
            }
            return returnAddress;
        }
        throw new IllegalArgumentException("Address not allocated.");
    }

    /*
     * This method sets the link (or pointer/address) portion of the node at
     * the given address.
     */
    public void setNodeLink(int address, int address2) {
        if (isAllocated(address) && isAllocated(address2)) {
            simulatedHeap[address + 1] = address2;
        } else {
            throw new IllegalArgumentException("Address not allocated.");
        }
    }

}
