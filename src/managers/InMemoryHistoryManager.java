package managers;

import models.Node;
import models.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{
    private final CustomLinkedList<Task> historyLinkedList = new CustomLinkedList<>();
    private final Map<Integer,Node<Task>> historyTasksMap = new HashMap<>();
    @Override
    public void add(Task task){
      if (historyTasksMap.containsKey(task.getId())) {
          remove(task.getId());
      }
      historyLinkedList.linkLast(task);
      historyTasksMap.put(task.getId(), historyLinkedList.tail);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history =  historyLinkedList.getTasks();
        Collections.reverse(history);
        return history;
    }

    @Override
    public void remove(int id) {
        Node<Task> nodeForDelete = historyTasksMap.get(id);
        if (nodeForDelete != null) {
            historyLinkedList.removeNode(nodeForDelete);
        }
    }
    public int historySize() {
        return historyLinkedList.getSize();
    }



    public static class CustomLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;
        private int size = 0;

        void linkLast(T task) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail,task,null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            size++;
        }


        List<T> getTasks() {
            ArrayList<T> tasksList = new ArrayList<>();
            if (head == null) {
                return tasksList;
            }
            Node<T> currentNode = head;
            while (currentNode != null) {
                tasksList.add(currentNode.data);
                currentNode = currentNode.next;
            }
            return tasksList;
        }

        void removeHead() {
            Node<T> nextHead = head.next;
            if (nextHead != null) {
                nextHead.prev = null;
                head.next = null;
                head = nextHead;
            } else {
                head = null;
                tail = null;
            }
            size--;
        }
        void removeTail() {
            Node<T> prevTail = tail.prev;
            prevTail.next = null;
            tail.prev = null;
            tail = prevTail;
            size--;
        }

        public void removeNode(Node<T> node) {
            if (head.equals(node)) {
                removeHead();
            } else if (tail.equals(node)) {
                removeTail();
            } else {
                Node<T> prevNode = node.prev;
                Node<T> nextNode = node.next;
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
                node.prev = null;
                node.next = null;
                size--;
            }
        }

        public int getSize() {
            return size;
        }
    }
}

