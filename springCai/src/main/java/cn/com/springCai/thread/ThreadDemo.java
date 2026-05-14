package cn.com.springCai.thread;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

/**
 * @Author caiJH
 * @Date 2026/1/23 3:37 PM
 * @Version 1.0
 */
public class ThreadDemo {

    /**
     * 方式一
     * 因为类是单继承，因此与线程耦合比较强
     */
    static class ThreadTest extends Thread{
        @Override
        public void run() {
            System.out.println("继承方式实现多线程");
        }
    }

    /**
     * 方式二
     * 实现接口方式实现多线程
     * 因为接口可以多实现
     * 因此与线程耦合比较弱
     * 还可继承其他类特性
     */
    static class RunnableTest implements Runnable{
        @Override
        public void run() {
            System.out.println("实现Runnable接口方式实现多线程");
        }
    }

    /**
     * 方式三
     * 实现Callable接口实现多线程
     * 可以返回结果、可抛出异常
     * 配合FutureTask使用
     */
    static class CallableTest implements Callable<String> {

        @Override
        public String call() throws Exception {
            return "Callable返回信息";
        }
    }

    /**
     * 方式四
     * 线程池实现线程
     */
    static class ThreadPoolDemo{

        private ExecutorService executorService;

        /**
         * 初始化线程池
         * @param num
         */
        public void getInstance(int num){
            executorService = Executors.newFixedThreadPool(num);
        }

        /**
         * 获取默认大小线程池
         * @return
         */
        public ExecutorService getExecutorService(){
            if(null == executorService){
                getInstance(4);
            }
            return  executorService;
        }

        /**
         * 线程池不存在，获取制定大小线程池
         * @param num
         * @return
         */
        public ExecutorService getExecutorService(int num){
            if(null == executorService){
                getInstance(num);
            }
            return  executorService;
        }

        /**
         * 关闭线程池
         */
        public void close(){
            if(null != executorService){
                executorService.shutdown();
            }
        }


        // 1. 固定大小线程池
        ExecutorService fixedPool = Executors.newFixedThreadPool(5);

        // 2. 单线程线程池
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

        // 3. 可缓存线程池（自动回收）
        ExecutorService cachedPool = Executors.newCachedThreadPool();

        // 4. 定时任务线程池
        ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(3);

        // 5. 自定义线程池（推荐）
        ThreadPoolExecutor customPool = new ThreadPoolExecutor(
                5,      // 核心线程数
                10,     // 最大线程数
                60L,    // 空闲线程存活时间
                TimeUnit.SECONDS,  // 时间单位
                new ArrayBlockingQueue<>(100)  // 任务队列
        );
    }

    /**
     * 方式五
     * CompletableFuture是 java8新增的类，实现了Future、CompletionStage接口
     */
    static class CompletableFutureDemo{

        public void test(){

            /**
             * 无返回值
             */
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("异步执行");
            });

            /**
             * 有返回值
             */
            CompletableFuture<?> futureCallBack = CompletableFuture.supplyAsync(() -> {
                return "异步执行";
            });

            future.thenAccept(result -> {
                System.out.println("future-异步执行结果：" + result);
            });

            futureCallBack.thenAccept(result -> {
                System.out.println("futureCallBack-异步执行结果：" + result);
            });

            CompletableFuture.allOf(future, futureCallBack).join();
        }
    }

    /**
     * 方式六
     * ForkJoinPool 适合计算密集型任务
     * ForkJoinPool是ExecutorService的实现类，因此使用方式与线程池类似
     *
     * ForkJoinPool基于"分而治之"的思想，特别适用于那些可以被递归地拆分成子任务的任务。
     * 它使用工作窃取（work-stealing）算法，这意味着每个工作线程都有自己的双端队列，当自己的任务完成后，
     * 可以从其他线程的队列中“窃取”任务来执行，从而平衡负载，提高CPU利用率。
     * 工作窃取（Work-Stealing）算法 是一种多线程任务调度策略，用于提高多线程程序的性能：每个工作线程维护一个双端队列（deque）来存放任务。
     *      当一个线程完成自己队列中的任务后，它可以从其他线程的队列末尾窃取任务来执行。这种方式可以有效地平衡各线程的负载，提高CPU利用率。
     * 分治任务：通常我们使用 ForkJoinTask 来表示一个可分解的任务，其中最常用的两个子类是 RecursiveAction（用于没有返回结果的任务）
     *      和 RecursiveTask（用于有返回结果的任务）。
     * 使用场景：
     *      递归任务：例如，归并排序、快速排序等排序算法，以及树和图的遍历等。
     *      并行计算：当一个问题可以分解为独立的子问题，并且子问题可以并行计算时，例如，计算斐波那契数列、矩阵乘法等。
     *      大数据处理：在数据处理中，例如，对大量数据同时进行相同的操作（如转换、过滤等），可以使用 ForkJoinPool 来并行处理。
     *      模拟和仿真：一些模拟问题，如蒙特卡洛模拟，可以通过分解成多个独立的模拟任务来并行执行。
     *      ForkJoinPool 的使用步骤：
     *       1. 创建一个 ForkJoinTask 子类，并实现 compute() 方法。
     *       2. 创建一个 ForkJoinPool 实例。
     *       3. 调用 ForkJoinPool 的 invoke() 或 submit() 方法来执行任务。
     *       4. 调用 ForkJoinTask 的 join() 方法来获取结果。
     *       5. 关闭 ForkJoinPool。
     *
     */
    static class ForkJoinDemoTask extends RecursiveTask<Long>{

        private long start;
        private long end;
        private static long THRESHOLD = 1000;

        public ForkJoinDemoTask() {}

        public ForkJoinDemoTask(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start <= THRESHOLD) {
                // 直接计算
                long sum = 0;
                for (long i = start; i <= end; i++) {
                    sum += i;
                }
//                System.out.println("直接计算结果：" + sum);
                return sum;
            } else {
                // 拆分任务
                long middle = (start + end) / 2;
                System.out.println("拆分平均值：" + middle);
                ForkJoinDemoTask left = new ForkJoinDemoTask(start, middle);
                ForkJoinDemoTask right = new ForkJoinDemoTask(middle + 1, end);
                left.fork();  // 异步执行
                right.fork();
                return left.join() + right.join();  // 合并结果
            }
        }

        public void tMain(){
            ForkJoinPool pool = new ForkJoinPool();
            ForkJoinDemoTask task = new ForkJoinDemoTask(1, 10000L);
            Long result = pool.invoke(task);
            System.out.println("结果: " + result);
        }
    }

    static class ForkJoinDemoAction extends RecursiveAction{

        @Override
        protected void compute() {

        }
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {

//        /**
//         * 无返回值
//         */
//        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//            System.out.println("异步执行，无返回值");
//        });
//
//        /**
//         * 有返回值
//         */
//        CompletableFuture<?> futureCallBack = CompletableFuture.supplyAsync(() -> {
//            System.out.println("异步执行，有返回值");
//            return "异步执行";
//        });
//
//        future.thenAccept(result -> {
//            System.out.println("future-异步执行结果：" + result);
//        });
//
//        futureCallBack.thenAccept(result -> {
//            System.out.println("futureCallBack-异步执行结果：" + result);
//        });
//
//        CompletableFuture.allOf(future, futureCallBack).join();


//        new ThreadTest().start();
//        new Thread(new RunnableTest()).start();
//
//        /**
//         * FutureTask实现了RunnableFuture接口，RunnableFuture接口继承了Runnable、Future接口
//         * 因此FutureTask可以当成线程Runnable使用，也可以作为Future获取Callable的返回结果
//         * FutureTask作为Runnable调用run方法，调用Callable对象的call方法
//         * 当然也可以把FutureTask放到线程池中
//         */
//        FutureTask<String> futureTaskTest = new FutureTask<String>(new CallableTest());
//        new Thread(futureTaskTest).start();
//        System.out.printf(futureTaskTest.get() + "\n");
//
//        /**
//         * 使用线程池1.0
//         */
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        // 提交的Runnable
//        executorService.submit(futureTaskTest);
//        System.out.printf(futureTaskTest.get() + "\n");
//        executorService.shutdown();
//
//        /**
//         * 使用线程池1.1
//         * 使用Future不使用FutureTask
//         * executorService提交Callable返回一个Future对象
//         * executorService线程池包装了FutureTask，会将Callable提交给FutureTask
//         */
//        // 提交的Callable
//        Future<String> future = executorService.submit(new CallableTest());
//        System.out.printf(future.get() + "\n");


//        ForkJoinDemoTask forkJoinDemoTask = new ForkJoinDemoTask();
//        forkJoinDemoTask.tMain();
    }
}
