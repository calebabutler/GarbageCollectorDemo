
/*
 * A very simple demo of the GarbageCollector class.
 */

public class GarbageCollectorMain {

    private static GarbageCollector gc;

    /*
     * Creates a linked list in the garbage collector given an int[] with the
     * wanted values.
     */
    public static void createList(String variableName, int[] list) {
        gc.setVariable(variableName, gc.allocateNode());
        int pointer = gc.getVariable(variableName);
        for (int i = 0; i < list.length; i++) {
            if (i != 0) {
                gc.setNodeLink(pointer, gc.allocateNode());
                pointer = gc.getNodeLink(pointer);
            }
            gc.setNodeValue(pointer, list[i]);
        }
    }

    /*
     * Prints a linked list from the garbage collector's memory.
     */
    public static void printList(String variableName) {
        int pointer = gc.getVariable(variableName);
        while (pointer != 0) {
            System.out.print(" ");
            System.out.print(gc.getNodeValue(pointer));
            pointer = gc.getNodeLink(pointer);
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        gc = new GarbageCollector();

        // Create linked list "myList"
        createList("myList", new int[] {
            1, 2, 3, 4, 5
        });

        // Print linked list
        System.out.print("myList:");
        printList("myList");

        // Remove variable from scope
        gc.removeVariable("myList");
        System.out.println("Removed myList from scope...");

        // Create linked list "myList2"
        createList("myList2", new int[] {
            6, 7, 8, 9, 10, 11, 12
        });

        // Print linked list "myList2"
        System.out.print("myList2:");
        printList("myList2");

        // Remove variable from scope
        gc.removeVariable("myList2");
        System.out.println("Removed myList2 from scope...");
    }

}
