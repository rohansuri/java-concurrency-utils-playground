package playground.forkjoin;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.logging.Logger;

public class MergeSort {
    private static final Logger logger = Logger.getLogger(MergeSort.class.toString());

    public static List<Integer> usingSingleThread(List<Integer> list){
        if(list.size() == 1){
            return new ArrayList<>(list);
        }
        return merge(
                usingSingleThread(list.subList(0, list.size() / 2)),
                usingSingleThread(list.subList(list.size() / 2, list.size())));
    }

    public static List<Integer> usingForkJoinPool(List<Integer> list){
        return ForkJoinPool.commonPool().invoke(new MergeSortTask(list));
    }

    private static class MergeSortTask extends RecursiveTask<List<Integer>> {
        private List<Integer> list;
        private static final int THRESHOLD = 10_000;

        public MergeSortTask(List<Integer> list){
            this.list = list;
        }

        @Override
        protected List<Integer> compute() {
            if(list.size() <= THRESHOLD){
                return usingSingleThread(list);
            }
            MergeSortTask left = new MergeSortTask(list.subList(0, list.size() / 2));
            left.fork();
            MergeSortTask right = new MergeSortTask(list.subList(list.size() / 2, list.size()));
            return merge(right.compute(), left.join()); // important to compute before join!
        }
    }

    // TODO: later attempt merging step using streams or TCO
    // http://blog.agiledeveloper.com/2013/01/functional-programming-in-java-is-quite.html
    private static List<Integer> merge(List<Integer> left, List<Integer> right){
        ListIterator<Integer> leftIterator = left.listIterator();
        ListIterator<Integer> rightIterator = right.listIterator();
        List<Integer> result = new ArrayList<>(left.size() + right.size());

        while(leftIterator.hasNext() && rightIterator.hasNext()){
            result.add(
                        left.get(leftIterator.nextIndex()) < right.get(rightIterator.nextIndex()) ?
                        leftIterator.next():rightIterator.next());
        }

        leftIterator.forEachRemaining(element -> result.add(element));
        rightIterator.forEachRemaining(element -> result.add(element));

        return result;
    }

}
