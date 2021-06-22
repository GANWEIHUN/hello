import java.util.Arrays;
import java.util.Iterator;

public class ReverseArray<T> implements Iterable<T> {

    private final T[] array;

    @SafeVarargs
    public ReverseArray(T... t) {
        array = Arrays.copyOfRange(t, 0, t.length);
    }

    @Override
    public Iterator<T> iterator() {
        return new ReverseIterator();
    }

    class ReverseIterator implements Iterator<T> {

        private int index = array.length;

        @Override
        public boolean hasNext() {
            return index > 0;
        }

        @Override
        public T next() {
            index--;
            return array[index];
        }
    }
}
