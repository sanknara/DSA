class LRUCache {

    // Add node method to insert the doubly linked list node
    // Always add the new node next to head
    private void addNode(DLinkedNode node){

        // Set the node previous and next values from head:
        node.prev = head;
        node.next = head.next;

        // Update the head previous and next values considering new node:
        head.next.prev = node;
        head.next = node;

    }


    // Remove node method to remove the node 
    // Rearrange the next and previous node 
    private void removeNode(DLinkedNode node){

        // link the previous and next node of current node to remove the reference with other nodes
        // As doubly linked list has pseudo head and pseudo tail, null check is not necessary
        node.prev.next = node.next;
        node.next.prev = node.prev;

    }


    // Move to head method supports rearranging node in between head
    private void moveToHead(DLinkedNode node){

        // Remove the node and add node(as add node always node next to head)
        removeNode(node);
        addNode(node);

    }

    // Pop tail method to support the removal of least accessed node
    private DLinkedNode popTail(){

        DLinkedNode leastAccessedNode=tail.prev;
        removeNode(leastAccessedNode);
        return leastAccessedNode;

    }

    // Doubly Linked List node - to support remove operation in O(1)
    // Also insert/update the node in O(1)
    class DLinkedNode{
        int key;
        int value;
        DLinkedNode prev;
        DLinkedNode next;
    }

    //LRU cache -> Least Recently Used Cache -> supports get,put operations based on allocated capacity, if reaches maximum capacity,cache should remove the least accessed object and insert the latest one. 
    // Map -> To perform and get operation in O(1)
    private Map<Integer, DLinkedNode> cache = new HashMap<>();
    // Doubly Linked List -> To perform delete/remove operation in O(1)
    private DLinkedNode head, tail;
    // Get the size of the cache
    private int size;
    // Get the allocated capacity
    private int capacity;

    public LRUCache(int capacity) {
        this.size = 0;
        this.capacity = capacity;

        // Initialize pseudo head and pseudo tail and link it
        head = new DLinkedNode();
        // head.prev = null
        tail = new DLinkedNode();
        // tail.next = null

        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        DLinkedNode node=cache.get(key);

        if(node == null) return -1;

        // Move the accessed node to head to move the least accessed node to tail for removal when cache is full
        moveToHead(node);

        return node.value;
    }

    public void put(int key, int value) {
        DLinkedNode node=cache.get(key);

        // Check the node before inserting whether to create new entry or update older key.
        if(node == null){
            //Create a new node and set it on the cache
            DLinkedNode newNode = new DLinkedNode();
            newNode.key = key;
            newNode.value = value;
            // Insert key and value on the Node for parallel operations
            cache.put(key, newNode);
            // Add Node on doubly linked node for parallel operations
            addNode(newNode);
            ++size;

            // If cache size reaches maximum capacity, remove the least accessed object from the cache :
            if(size > capacity){
                // Pop tail to remove the least accessed node
                DLinkedNode leastAccessedNode = popTail();
                // Remove the key from the cache 
                cache.remove(leastAccessedNode.key);
                size--;
            }


        }else{
            // just update the node value
            node.value = value;
            // move the node to head as its new value to be inserted
            moveToHead(node);

        }
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
