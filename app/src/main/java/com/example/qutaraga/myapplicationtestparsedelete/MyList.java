package com.example.qutaraga.myapplicationtestparsedelete;
/**Create own LinkedList.
 *@author Qutaraga.
 *@version 1.0
 * */
public class MyList<T> {

    private Node head;
    private int listCount;

    public int listSize(){
        return listCount;
    }

    /**
     * Add object in last position
     * */
    public void add(T num){

        Node addNum = new Node(num);
        Node current = head;

        if(head == null)
            head = addNum;
        else {
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(addNum);
        }
        listCount++;
    }

    /**
     * Add object in first position
     * */
    public void addFirst(T num) {
        Node addNum = new Node(num);
        Node current = head;

        head = addNum;

        addNum.setNext(current);

        listCount++;
    }

    /**
     * Add object in index position.
     *
     * @throws new Exception("Index must be >=0 and <= listSize");
     * */
    public void addTo(T num, int index) throws  Exception{
        Node addNum = new Node(num);
        Node current = head;

        if(index >= 0 && index <= listSize() ) {

            if (index == 0) {
                addFirst(num);
            } else {

                for (int i = 1; i < index; i++) {
                    current = current.getNext();
                }
                addNum.setNext(current.getNext());
                current.setNext(addNum);
                listCount++;
            }
        }else
            throw new Exception("Index must be >=0 and <= listSize");
    }

    /**
     * Checks object.
     *
     * If object contains you object list - return true;
     * */
    public boolean scan(T num){
        Node scan = new Node(num);
        Node current = head;

        for(int i =0; i < listCount; i++){
            current = current.getNext();
            if(current.getObjects().equals(num)){
                return true;
            }
        }
        return false;
    }

    /**
     * Replase object by index
     * */
    public void replase(T num, int index){
        Node current = head;

        if(index >= 0 && index <= listCount){

            for(int i = 0; i < index; i++){
                current = current.getNext();
            }
            current.setObjects(num);
        }
    }


    /**
     * Swap objects by index.
     * */
    public void swap(int index, int index1){
        Node current = head;
        T a = null;
        T b = null;

        if(index == 0){
            a = current.getObjects();
        }
        if(index1 == 0){
            b = current.getObjects();
        }
            for (int i = 1; i < listSize(); i++) {
                current = current.getNext();
                 if (i == index) {
                    a = current.getObjects();
                }

                else if (i == index1) {
                    b = current.getObjects();
                }
        }
        replase(b, index);
        replase(a, index1);
    }

    /**
     * Delete object from list.
     * */
    public void remove(int index) {
        if(index == 0)head = head.next;
        else
        {
            Node current = head;
            if (index < 0 || index > listSize()) {
                System.out.println("Index must be > 0 || < listSize!");
            }

            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            }
            current.setNext(current.getNext().getNext());
        }
        listCount--;

    }


    public void clear(){
        for(int i =0; i < listSize(); i++){
            remove(i);
        }
    }

    /**
     * Get object from object list.
     * */
    public T get(int index){

        Node current = head;

        if(index <0){return null;}

        for(int i =0; i < index; i++ ){

            if(index >= 0 || index <= listSize()){
                if(current == null){return null;}

                current = current.getNext();
            }
        }
        return current.getObjects();
    }

    /**
     * Reverse list.
     * */
    public void reverse(){
        Node current = head;
        Node current1 = null;

        while (true) {
           Node rev = current.getNext();
           current.next = current1;

           if(rev != null) {
               current1 = current;
               current = rev;
           }

           if(rev == null){break;}
       }
           head = current;
}

class Node {

    Node next;
    T objects;

    public Node(T _objects) {
        next = null;
        objects = _objects;
        }

    public Node getNext() {
        return next;
        }

    public void setNext(Node next) {
        this.next = next;
        }

    public T getObjects() {
        return objects;
        }

    public void setObjects(T objects) {
        this.objects = objects;
        }
    }
}


