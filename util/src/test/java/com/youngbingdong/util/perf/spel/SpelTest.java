package com.youngbingdong.util.perf.spel;

import com.youngbingdong.util.spring.spel.SpringExpressionEvaluator;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author yangbingdong
 * @date 2020/6/1
 * @contact yangbingdong1994@gmail.com
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 3, time = 5)
@Threads(16)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
public class SpelTest {

    @Benchmark
    public String spelEvalUsingCache() {
        return SpringExpressionEvaluator.evalAsText(SpelPayload.spel, SpelPayload.method, SpelPayload.args, SpelPayload.target, SpelPayload.targetClass, null);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(SpelTest.class.getSimpleName())
                                              .build();
        new Runner(options).run();
    }
}
