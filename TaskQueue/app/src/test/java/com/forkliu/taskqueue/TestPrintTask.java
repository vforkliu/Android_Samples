package com.forkliu.taskqueue;

import static org.junit.Assert.assertEquals;

public class TestPrintTask implements ITask {
    private int id;

    public TestPrintTask(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        // 为了尽量模拟窗口办事的速度，我们这里停顿两秒。
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }

        // System.out.println("我的id是：" + id);
        assertEquals("我的id是：" + id,1,1);
    }
}
