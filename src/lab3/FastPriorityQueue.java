package lab3;

import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

public class FastPriorityQueue<E> extends AbstractQueue<E> {
	private int size = 0;
	private static final int DEFAULT_INITIAL_CAPACITY = 11;
	private final Comparator<? super E> comparator;
	Object[] queue;
	
	HashMap<Object,Integer> indexTracker = new HashMap<>();

	public FastPriorityQueue() {
		this(null);
	}

	public FastPriorityQueue(Comparator<? super E> comparator) {
		this.comparator = comparator;
		this.queue = new Object[DEFAULT_INITIAL_CAPACITY];
	}

	@Override
	public boolean offer(E e) {
		if (e == null)
			throw new NullPointerException();
		int i = size;
		if (i >= queue.length)
			grow();
		size = i + 1;
		if (i == 0) {
			queue[0] = e;
			indexTracker.put(e, 0); //Indextracker
		}
		else
			siftUp(i, e);
		return true;
	}
	
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i == -1)
            return false;
        else {
            removeAt(i);
            return true;
        }
    }
    public boolean add(E e) {
        return offer(e);
    }

	@Override
	  public E peek() {
        return (size == 0) ? null : (E) queue[0];
    }

    public E poll() {
        if (size == 0)
            return null;
        int s = --size;
        E result = (E) queue[0];
        E x = (E) queue[s];
        queue[s] = null;
        indexTracker.remove(x);
        if (s != 0)
            siftDown(0, x);
        return result;
    }

	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return size;
	}
	
    private void siftUp(int k, E x) {
        if (comparator != null)
            siftUpUsingComparator(k, x);
        else throw new NullPointerException("No comparator supplied to function");
       
    }
    private E removeAt(int i) {
        // assert i >= 0 && i < size;
        int s = --size;
        if (s == i) { // removed last element
            queue[i] = null;
        	indexTracker.remove(queue[i]);
        }
        else {
            E moved = (E) queue[s];
            queue[s] = null;
            indexTracker.remove(queue[s]);
            siftDown(i, moved);
            if (queue[i] == moved) {
                siftUp(i, moved);
                if (queue[i] != moved)
                    return moved;
            }
        }
        return null;
    }
    private void siftUpUsingComparator(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = queue[parent];
            if (comparator.compare(x, (E) e) >= 0)
                break;
            queue[k] = e;
            indexTracker.put(e, k);
            k = parent;
        }
        queue[k] = x;
        indexTracker.put(x, k);
    }
    
    
    private void siftDown(int k, E x) {
        if (comparator != null)
            siftDownUsingComparator(k, x);
        else throw new NullPointerException("No comparator supplied to function");
    }
    



    @SuppressWarnings("unchecked")
    private void siftDownUsingComparator(int k, E x) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            Object c = queue[child];
            int right = child + 1;
            if (right < size &&
                comparator.compare((E) c, (E) queue[right]) > 0)
                c = queue[child = right];
            	indexTracker.put(c, child);
            if (comparator.compare(x, (E) c) <= 0)
                break;
            queue[k] = c;
            indexTracker.put(c, k);
            k = child;
        }
        queue[k] = x;
        indexTracker.put(x, k);
    }
    
    private int indexOf(Object o) {
    	/*
        if (o != null) {
            for (int i = 0; i < size; i++)
                if (o.equals(queue[i]))
                    return i;
        }
        return -1;
        */
    	if(indexTracker.get(o)!=null) {
    		return indexTracker.get(o);
    	} else {
    		return -1;
    	}
    	
    }
	
    private void grow() {
        int oldCapacity = queue.length;
        // Double size if small; else grow by 50%
        int newCapacity = oldCapacity + ((oldCapacity < 64) ?
                                         (oldCapacity + 2) :
                                         (oldCapacity >> 1));
        queue = Arrays.copyOf(queue, newCapacity);
    }

}
