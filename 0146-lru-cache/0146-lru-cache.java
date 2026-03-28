import java.util.HashMap; // import HashMap for quick key->node lookup
import java.util.Map; // import Map interface for the node cache

class LRUCache {

    private static class Node { // doubly linked list node stores key/value and neighbors
        int key; // key of this cache entry
        int value; // value of this cache entry
        Node prev; // pointer to previous node in list
        Node next; // pointer to next node in list

        Node(int key, int value) { // node constructor sets key and value
            this.key = key; // store provided key
            this.value = value; // store provided value
        }
    }

    private final int capacity; // maximum number of entries allowed
    private final Map<Integer, Node> map; // map from key to node for O(1) access
    private final Node head; // dummy head sentinel of doubly linked list
    private final Node tail; // dummy tail sentinel of doubly linked list

    public LRUCache(int capacity) { // cache constructor with fixed capacity
        this.capacity = capacity; // save capacity
        this.map = new HashMap<>(capacity); // initialize map with approximate size
        head = new Node(0, 0); // create dummy head node
        tail = new Node(0, 0); // create dummy tail node
        head.next = tail; // link head to tail
        tail.prev = head; // link tail back to head
    }

    public int get(int key) { // read operation for cache
        Node node = map.get(key); // look up node by key in map
        if (node == null) { // if key is not present
            return -1; // return -1 for cache miss
        }
        moveToHead(node); // on cache hit, move node to most recently used position
        return node.value; // return stored value
    }

    public void put(int key, int value) { // write/update operation for cache
        Node node = map.get(key); // check whether node already exists
        if (node != null) { // if key exists in cache
            node.value = value; // update the value of existing node
            moveToHead(node); // mark existing node as most recently used
        } else { // if key does not exist yet
            Node newNode = new Node(key, value); // create a new node for key/value
            map.put(key, newNode); // store mapping from key to node
            addToHead(newNode); // insert new node at head as most recently used
            if (map.size() > capacity) { // if cache exceeded capacity
                Node lru = tail.prev; // least recently used node is just before tail
                removeNode(lru); // remove LRU node from linked list
                map.remove(lru.key); // remove LRU key from map
            }
        }
    }

    private void addToHead(Node node) { // helper to insert node immediately after head
        node.prev = head; // set new node previous pointer to head
        node.next = head.next; // set new node next pointer to old first node
        head.next.prev = node; // link old first node back to new node
        head.next = node; // link head forward to new node
    }

    private void removeNode(Node node) { // helper to unlink node from list
        node.prev.next = node.next; // bypass node by linking previous directly to next
        node.next.prev = node.prev; // bypass node by linking next back to previous
    }

    private void moveToHead(Node node) { // helper to reposition existing node as most recent
        removeNode(node); // unlink node from its current position
        addToHead(node); // insert node right after head
    }

}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */