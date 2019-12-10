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
public class ProtostuffSerializePerfTest {

	private static final ProtostuffSerializer protostuffSerializer = new ProtostuffSerializer();
    private static final DefaultJDKSerializer defaultJDKSerializer = new DefaultJDKSerializer();
    private static final FSTSerializer fstSerializer = new FSTSerializer();
    private static final KryoSerializer kryoSerializer = new KryoSerializer();
	private static TestUser user = TestUser.buildUserById(1L);

	@Benchmark
	public byte[] protostuff() {
		return protostuffSerializer.serialize(user);
	}

    @Benchmark
    public byte[] defaultJDK() {
        return defaultJDKSerializer.serialize(user);
    }

    @Benchmark
    public byte[] fst() {
        return fstSerializer.serialize(user);
    }

    @Benchmark
    public byte[] kryo() {
        return kryoSerializer.serialize(user);
    }


    public static void main(String[] args) throws RunnerException {
		Options options = new OptionsBuilder().include(ProtostuffSerializePerfTest.class.getSimpleName())
											  .build();
		new Runner(options).run();
	}
}
