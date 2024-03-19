
public class GarbageCollectorMain {

    public static void printList(GarbageCollector gc, int pointer) {
        while (pointer != 0) {
            System.out.print(" ");
            System.out.print(gc.getNodeValue(pointer));
            pointer = gc.getNodeLink(pointer);
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        // Allocate one linked list of 100 entries
        GarbageCollector gc = new GarbageCollector();

        gc.setVariable("myList", gc.allocateNode());

        int pointer = gc.getVariable("myList");
        for (int i = 0; i < 100; i++) {
            gc.setNodeValue(pointer, i);
            gc.setNodeLink(pointer, gc.allocateNode());
            pointer = gc.getNodeLink(pointer);
        }

        // Print linked list
        System.out.print("myList:");
        printList(gc, gc.getVariable("myList"));

        // Remove variable from scope
        gc.removeVariable("myList");
        System.out.println("Removing myList from scope...");

        gc.setVariable("myList2", gc.allocateNode());

        int pointer2 = gc.getVariable("myList2");
        for (int i = 0; i < 20; i++) {
            gc.setNodeValue(pointer2, (i + 1) * 7);
            gc.setNodeLink(pointer2, gc.allocateNode());
            pointer2 = gc.getNodeLink(pointer2);
        }

        // Print linked list
        System.out.print("myList2:");
        printList(gc, gc.getVariable("myList2"));

        // Remove variable from scope
        gc.removeVariable("myList2");
        System.out.println("Remove myList2 from scope...");
    }

}
