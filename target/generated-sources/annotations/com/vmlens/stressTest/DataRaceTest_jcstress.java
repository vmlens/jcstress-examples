package com.vmlens.stressTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.openjdk.jcstress.infra.runners.TestConfig;
import org.openjdk.jcstress.infra.collectors.TestResultCollector;
import org.openjdk.jcstress.infra.runners.Runner;
import org.openjdk.jcstress.infra.runners.StateHolder;
import org.openjdk.jcstress.util.Counter;
import org.openjdk.jcstress.vm.WhiteBoxSupport;
import org.openjdk.jcstress.util.OpenAddressHashCounter;
import java.util.concurrent.ExecutionException;
import com.vmlens.stressTest.DataRaceTest;
import org.openjdk.jcstress.infra.results.IntResult2_jcstress;

public class DataRaceTest_jcstress extends Runner<IntResult2_jcstress> {

    OpenAddressHashCounter<IntResult2_jcstress> counter_actor1;
    OpenAddressHashCounter<IntResult2_jcstress> counter_actor2;
    volatile StateHolder<Pair> version;

    public DataRaceTest_jcstress(TestConfig config, TestResultCollector collector, ExecutorService pool) {
        super(config, collector, pool, "com.vmlens.stressTest.DataRaceTest");
    }

    @Override
    public void sanityCheck() throws Throwable {
        sanityCheck_API();
        sanityCheck_Footprints();
    }

    private void sanityCheck_API() throws Throwable {
        final DataRaceTest t = new DataRaceTest();
        final DataRaceTest s = new DataRaceTest();
        final IntResult2_jcstress r = new IntResult2_jcstress();
        Collection<Future<?>> res = new ArrayList<>();
        res.add(pool.submit(() -> t.actor1(r)));
        res.add(pool.submit(() -> t.actor2(r)));
        for (Future<?> f : res) {
            try {
                f.get();
            } catch (ExecutionException e) {
                throw e.getCause();
            }
        }
    }

    private void sanityCheck_Footprints() throws Throwable {
        config.adjustStrides(size -> {
            version = new StateHolder<>(new Pair[size], 2, config.spinLoopStyle);
            final DataRaceTest t = new DataRaceTest();
            for (int c = 0; c < size; c++) {
                Pair p = new Pair();
                p.r = new IntResult2_jcstress();
                p.s = new DataRaceTest();
                version.pairs[c] = p;
                p.s.actor1(p.r);
                p.s.actor2(p.r);
            }
        });
    }

    @Override
    public Counter<IntResult2_jcstress> internalRun() {
        version = new StateHolder<>(new Pair[0], 2, config.spinLoopStyle);
        counter_actor1 = new OpenAddressHashCounter<>();
        counter_actor2 = new OpenAddressHashCounter<>();

        control.isStopped = false;
        Collection<Future<?>> tasks = new ArrayList<>();
        tasks.add(pool.submit(this::actor1));
        tasks.add(pool.submit(this::actor2));

        try {
            TimeUnit.MILLISECONDS.sleep(config.time);
        } catch (InterruptedException e) {
        }

        control.isStopped = true;

        waitFor(tasks);

        Counter<IntResult2_jcstress> counter = new OpenAddressHashCounter<>();
        counter.merge(counter_actor1);
        counter.merge(counter_actor2);
        return counter;
    }

    public final void jcstress_consume(StateHolder<Pair> holder, OpenAddressHashCounter<IntResult2_jcstress> cnt, int a, int actors) {
        Pair[] pairs = holder.pairs;
        int len = pairs.length;
        int left = a * len / actors;
        int right = (a + 1) * len / actors;
        for (int c = left; c < right; c++) {
            Pair p = pairs[c];
            IntResult2_jcstress r = p.r;
            DataRaceTest s = p.s;
            p.s = new DataRaceTest();
            cnt.record(r);
            r.r1 = 0;
            r.r2 = 0;
        }
    }

    public final void jcstress_updateHolder(StateHolder<Pair> holder) {
        if (!holder.tryStartUpdate()) return;
        Pair[] pairs = holder.pairs;
        int len = pairs.length;

        int newLen = holder.updateStride ? Math.max(config.minStride, Math.min(len * 2, config.maxStride)) : len;

        Pair[] newPairs = pairs;
        if (newLen > len) {
            newPairs = Arrays.copyOf(pairs, newLen);
            for (int c = len; c < newLen; c++) {
                Pair p = new Pair();
                p.r = new IntResult2_jcstress();
                p.s = new DataRaceTest();
                newPairs[c] = p;
            }
         }

        version = new StateHolder<>(control.isStopped, newPairs, 2, config.spinLoopStyle);
        holder.finishUpdate();
   }

    public final Void actor1() {

        OpenAddressHashCounter<IntResult2_jcstress> counter = counter_actor1;
        while (true) {
            StateHolder<Pair> holder = version;
            if (holder.stopped) {
                return null;
            }

            Pair[] pairs = holder.pairs;

            holder.preRun();

            for (Pair p : pairs) {
                IntResult2_jcstress r = p.r;
                r.trap = 0;
                p.s.actor1(r);
            }

            holder.postRun();

            jcstress_consume(holder, counter, 0, 2);
            jcstress_updateHolder(holder);

            holder.postUpdate();
        }
    }

    public final Void actor2() {

        OpenAddressHashCounter<IntResult2_jcstress> counter = counter_actor2;
        while (true) {
            StateHolder<Pair> holder = version;
            if (holder.stopped) {
                return null;
            }

            Pair[] pairs = holder.pairs;

            holder.preRun();

            for (Pair p : pairs) {
                IntResult2_jcstress r = p.r;
                r.trap = 0;
                p.s.actor2(r);
            }

            holder.postRun();

            jcstress_consume(holder, counter, 1, 2);
            jcstress_updateHolder(holder);

            holder.postUpdate();
        }
    }

    static class Pair {
        public DataRaceTest s;
        public IntResult2_jcstress r;
    }
}
