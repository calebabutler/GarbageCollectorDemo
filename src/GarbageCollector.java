
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class GarbageCollector {

    private int[] simulatedHeap;
    private HashMap<String, Integer> variableNames;
    private HashMap<Integer, Boolean> allocatedNodes;

    private final int HEAP_SIZE = 256; // 1024 bytes, a kilobyte!

    public GarbageCollector() {
        simulatedHeap = new int[HEAP_SIZE];
        variableNames = new HashMap<>();
        allocatedNodes = new HashMap<>();
    }

    private boolean isAllocated(int address) {
        if (address == 0) {
            return true;
        }
        return allocatedNodes.keySet().contains(address);
    }

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

    public void setVariable(String variableName, int address) {
        if (isAllocated(address)) {
            variableNames.put(variableName, address);
        } else {
            throw new IllegalArgumentException("Address not allocated.");
        }
    }

    public int getVariable(String variableName) {
        Integer value = variableNames.get(variableName);
        if (value == null) {
            throw new IllegalArgumentException("Variable name not previously set.");
        }
        return value;
    }

    public void removeVariable(String variableName) {
        variableNames.remove(variableName);
    }

    public int getNodeValue(int address) {
        if (isAllocated(address)) {
            return simulatedHeap[address];
        }
        throw new IllegalArgumentException("Address not allocated.");
    }

    public void setNodeValue(int address, int value) {
        if (isAllocated(address)) {
            simulatedHeap[address] = value;
        } else {
            throw new IllegalArgumentException("Address not allocated.");
        }
    }

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

    public void setNodeLink(int address, int address2) {
        if (isAllocated(address) && isAllocated(address2)) {
            simulatedHeap[address + 1] = address2;
        } else {
            throw new IllegalArgumentException("Address not allocated.");
        }
    }

}
