package com.forkliu.taskqueue;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TaskQueueUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testOneExecutor(){
        // 这里暂时只开一个窗口。
        TaskQueue taskQueue = new TaskQueue(1);
        taskQueue.start();

        for (int i = 0; i < 10; i++) {
            TestPrintTask task = new TestPrintTask(i);
            taskQueue.add(task);
        }

        //assertSuccess("111");
    }
}