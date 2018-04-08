package playground.forkjoin;

import com.google.common.collect.Ordering;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// Not a good test, no JVM warm up
@RunWith(Parameterized.class)
public class MergeSortTest {

    // Junit creates new instance of Test class before invoking each @Test method
    @Parameterized.Parameter
    public List<Integer> list;

    @Test
    public void usingSingleThread(){
        // list.forEach(x -> System.out.print(x + ",")); System.out.println();

        long time = System.currentTimeMillis();
        List<Integer> sorted = MergeSort.usingSingleThread(list);
        logInfo(testName.getMethodName(), System.currentTimeMillis() - time, list.size());
        Assert.assertTrue(Ordering.natural().isOrdered(sorted));
    }

    @Test
    public void usingForkJoinPool(){
//        list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
//        list.forEach(x -> System.out.print(x + ",")); System.out.println();
//        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "1");
        long time = System.currentTimeMillis();
        List<Integer> sorted = MergeSort.usingForkJoinPool(list);
        logInfo(testName.getMethodName(), System.currentTimeMillis() - time, list.size());
        Assert.assertTrue(Ordering.natural().isOrdered(sorted));
    }

    private static final int SMALL_SAMPLE_SIZE = 1_000_000;
    private static final int LARGE_SAMPLE_SIZE = 10_000_000;
    private static final int LARGER_SAMPLE_SIZE = 100_000_000;
    private static final int LARGEST_SAMPLE_SIZE = 1_000_000_000;


    private static void logInfo(String testName, long millis, int listSize) {
        logger.info(String.format("Test %s spent %d milliseconds to sort %d elements",
                testName, millis, listSize));
    }

    private static final Logger logger = Logger.getLogger(MergeSortTest.class.toString());

    static IntStream revRange(int from, int to) {
        return IntStream.range(from, to)
                .map(i -> to - i + from - 1);
    }

    @Parameterized.Parameters
    public static Collection<List<Integer>> randomTestSet(){
        Random random = new Random();
        List<List<Integer>> list = new ArrayList<>();
//        list.add(random.ints(SMALL_SAMPLE_SIZE, 1, SMALL_SAMPLE_SIZE + 1).boxed().collect(Collectors.toList()));
        list.add(random.ints(LARGE_SAMPLE_SIZE, 1, LARGE_SAMPLE_SIZE + 1).boxed().collect(Collectors.toList()));
//        list.add(random.ints(LARGER_SAMPLE_SIZE, 1, LARGER_SAMPLE_SIZE + 1).boxed().collect(Collectors.toList()));
//        list.add(random.ints(LARGEST_SAMPLE_SIZE, 1, LARGEST_SAMPLE_SIZE + 1).boxed().collect(Collectors.toList()));
        return list;
    }


/*    @Parameterized.Parameters
    public static Collection<List<Integer>> decreasingTestSet(){
        List<List<Integer>> list = new ArrayList<>();
        list.add(revRange(0, SMALL_SAMPLE_SIZE).boxed().collect(Collectors.toList()));
        list.add(revRange(0, LARGE_SAMPLE_SIZE).boxed().collect(Collectors.toList()));
        list.add(revRange(0, LARGER_SAMPLE_SIZE).boxed().collect(Collectors.toList()));
        list.add(revRange(0, LARGEST_SAMPLE_SIZE).boxed().collect(Collectors.toList()));
        return list;
    }
*/

    @Rule
    public TestName testName = new TestName();
}
