package com.chenhm.springdemo.ws;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import com.chenhm.springdemo.ws.service.Hello;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/applicationContext.xml")
public class CxfClientTests {

    @Resource(name = "helloService")
    Hello helloService;

    @Before
    public void setUp() {
        System.setProperty("javax.net.ssl.trustStorePassword", "123");
        String storePath = getClass().getResource("/localhost.jks").getPath();
        System.setProperty("javax.net.ssl.trustStore", storePath);
    }

    @Test
    public void testHelloService() {
        String result = helloService.sayHello("frank");
        assertThat(result, containsString("Hello, Welcome to CXF Spring boot frank!!!"));
    }

    @Test
    public void parallel() throws Exception {
        run(count -> {
            String result = helloService.sayHello(String.valueOf(count));
            assertEquals(result, "Hello, Welcome to CXF Spring boot " + count + "!!!");
        }, 1000, Runtime.getRuntime().availableProcessors(), "parallel:");
    }

    private void run(IntConsumer callable, int runTimes, int poolSize, String msg) throws InterruptedException, ExecutionException {
        ForkJoinPool forkJoinPool = new ForkJoinPool(poolSize);
        long start = System.currentTimeMillis();
        forkJoinPool.submit(() ->
                IntStream.range(1, runTimes).parallel().forEach(value -> {
                    callable.accept(value);
                })
        ).get();
        System.out.println(msg + (System.currentTimeMillis() - start));
    }
}