package com.youngbingdong.redisoper.perf;

import com.youngbingdong.redisoper.serilize.DefaultJDKSerializer;
import com.youngbingdong.redisoper.serilize.FSTSerializer;
import com.youngbingdong.redisoper.serilize.KryoSerializer;
import com.youngbingdong.redisoper.serilize.ProtostuffSerializer;
import com.youngbingdong.redisoper.vo.TestUser;
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
 * @author ybd
 * @date 19-4-28
 * @contact yangbingdong1994@gmail.com
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1, time = 3)
@Measurement(iterations = 2, time = 5)
@Threads(8)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
public class ProtostuffDeserializePerfTest {

    private static final ProtostuffSerializer protostuffSerializer = new ProtostuffSerializer();
    private static final DefaultJDKSerializer defaultJDKSerializer = new DefaultJDKSerializer();
    private static final FSTSerializer fstSerializer = new FSTSerializer();
    private static final KryoSerializer kryoSerializer = new KryoSerializer();
    private static TestUser user = TestUser.buildUserById(1L);

    private static final byte[] protostuffBytes = protostuffSerializer.serialize(user);
    private static final byte[] jdkBytes = defaultJDKSerializer.serialize(user);
    private static final byte[] fstBytes = fstSerializer.serialize(user);
    private static final byte[] kryoSerialize = kryoSerializer.serialize(user);

    @Benchmark
    public TestUser protostuff() {
        return protostuffSerializer.deserialize(protostuffBytes, TestUser.class);
    }

    @Benchmark
    public TestUser defaultJDK() {
        return defaultJDKSerializer.deserialize(jdkBytes, TestUser.class);
    }

    @Benchmark
    public TestUser fst() {
        return fstSerializer.deserialize(fstBytes,TestUser.class);
    }

    @Benchmark
    public TestUser kryo() {
        return kryoSerializer.deserialize(kryoSerialize,TestUser.class);
    }


    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(ProtostuffDeserializePerfTest.class.getSimpleName())
                                              .build();
        new Runner(options).run();
    }
}
