package com.github.fpj;

import com.github.fpj.monad.Maybe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.HashMap;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Functional {

    private static ExecutorService executorService = null;

    private static void initializeExecutorService(){
        if(executorService == null){
            int cores = Runtime.getRuntime().availableProcessors();
            executorService = Executors.newFixedThreadPool(cores+2);
        }
    }

    public static <A,B,C> Function<Tuple<A,B>,C> toFunction(final Function2<A,B,C> f){
        return new Function<Tuple<A,B>,C>(){
            public C apply(final Tuple<A,B> t){
                return f.apply(t.first(),t.second());
            }
        };
    }

    public static <A,B,C> Function2<A,B,C> toFunction2(final Function<Tuple<A,B>,C> f){
        return new Function2<A,B,C>(){
            public C apply(final A a, final B b){
                return f.apply(Tuple.tuple(a,b));
            }
        };
    }

    public static <A,B,C> Function<A,C> compose(final Function<A,B> f, final Function<B,C> g){
        return new Function<A,C>(){
            public C apply(final A a){
                return g.apply(f.apply(a));
            }
        };
    }

    public static <A,B,C> Function2<B,A,C> flip(final Function2<A,B,C> f){
        return new Function2<B,A,C>(){
            public C apply(final B b, final A a){
                return f.apply(a,b);
            }
        };
    }
    
    public static <A,B,C> Function<B,C> partialApply(final Function2<A,B,C> f, final A a){
        return new Function<B,C>(){
            public C apply(B b){
                return f.apply(a,b);
            }
        };
    }

    public static <A,B,C,D> Function2<B,C,D> partialApply(final Function3<A,B,C,D> f, final A a){
        return new Function2<B,C,D>(){
            public D apply(B b, C c){
                return f.apply(a,b,c);
            }
        };
    }

    public static <A,B,C,D,E> Function3<B,C,D,E> partialApply(final Function4<A,B,C,D,E> f, final A a){
        return new Function3<B,C,D,E>(){
            public E apply(B b, C c, D d){
                return f.apply(a,b,c,d);
            }
        };
    }

    public static <A,B,C,D,E,F> Function4<B,C,D,E,F> partialApply(final Function5<A,B,C,D,E,F> f, final A a){
        return new Function4<B,C,D,E,F>(){
            public F apply(B b, C c, D d,E e){
                return f.apply(a,b,c,d,e);
            }
        };
    }

    public static <A,B> Function<Unit,B> lazyApply(final Function<A,B> f, final A a){
        return new Function<Unit,B>(){
            public B apply(Unit unit){
                return f.apply(a);
            }
        };
    }

    public static <A,B> Function<A,Future<B>> parApply(final Function<A,B> f){
        return new Function<A,Future<B>>(){
            public Future<B> apply(final A a){
                return executorService.submit(new Callable<B>(){
                    public B call(){
                        return f.apply(a);
                    }
                });
            }
        };
    }

    public static <A,B> B apply(final Function<A,B> f, final A a){
        return f.apply(a);
    }

    public static <A,B,C> C apply2(final Function2<A,B,C> f, final A a, final B b){
        return f.apply(a,b);
    }

    public static <A,B> void foreach(final Function<A,B> f, final Collection<A> ac){
        for(A a : ac){
            f.apply(a);
        }
    }

    public static <A,B> com.github.fpj.monoid.List<B> map(final Function<A,B> f, final com.github.fpj.monoid.List<A> al){
        return (com.github.fpj.monoid.List<B>)al.fmap(f);
    }

    public static <A,B> Collection<B> map(final Function<A,B> f, final Collection<A> ac){
        return Functional.map(f, ac, new ArrayList<B>(ac.size()));
    }

    public static <A,B> Collection<B> map(final Function<A,B> f, final Collection<A> ac, final Collection<B> bc){
        for(A a : ac){
            bc.add(f.apply(a));
        }
        return bc;
    }

    public static <A,B> A foldLeft(final Function2<A,B,A> f, final A a, final Collection<B> bc){
        A acc = a;
        for(B b : bc){
            acc = f.apply(acc,b);
        }
        return acc;
    }

    public static <A,B> A foldLeft(final Function2<A,B,A> f, final A a, final com.github.fpj.monoid.List<B> bc){
        A acc = a;
        com.github.fpj.monoid.List<B> l = bc;
        while(l.hasValue()){
            acc = f.apply(acc,l.head());
            l = l.tail();
        }
        return acc;
    }

    public static <A,B> B foldRight(final Function2<A,B,B> f, final Collection<A> ac, B b){
        if(ac.size() == 0)
            return b;
        List<A> reversed = new ArrayList<A>(ac);
        Collections.reverse(reversed);
        return Functional.foldLeft(Functional.flip(f), b, reversed);
    }

    public static <A,B> B foldRight(final Function2<A,B,B> f, final List<A> ac, B b){
        if(ac.size() > 0){
            ListIterator<A> it = ac.listIterator(ac.size());
            do {
                b = f.apply(it.previous(),b);            
            } while(it.hasPrevious());
        }
        return b;
    }

    public static <A,B,C> Map<B,C> mapKeys(final Function<A,B> f, final Map<A,C> am){
        return Functional.mapKeys(f, am, new HashMap<B,C>(am.size()));
    }

    public static <A,B,C> Map<B,C> mapKeys(final Function<A,B> f, final Map<A,C> am, final Map<B,C> bm){
        for(Map.Entry<A,C> e : am.entrySet()){
            bm.put(f.apply(e.getKey()), e.getValue());
        }
        return bm;
    }

    public static <A,B,C> Map<C,B> mapValues(final Function<A,B> f, final Map<C,A> am){
        return Functional.mapValues(f, am, new HashMap<C,B>(am.size()));
    }

    public static <A,B,C> Map<C,B> mapValues(final Function<A,B> f, final Map<C,A> am, final Map<C,B> bm){
        for(Map.Entry<C,A> e : am.entrySet()){
            bm.put(e.getKey(), f.apply(e.getValue()));
        }
        return bm;
    }

    public static <A,B,C,D> Map<B,D> mapEntries(final Function<A,B> f, final Function<C,D> g, final Map<A,C> am){
        return Functional.mapEntries(f, g, am, new HashMap<B,D>(am.size()));
    }

    public static <A,B,C,D> Map<B,D> mapEntries(final Function<A,B> f, final Function<C,D> g, final Map<A,C> am, final Map<B,D> bm){
        for(Map.Entry<A,C> e : am.entrySet()){
            bm.put(f.apply(e.getKey()), g.apply(e.getValue()));
        }
        return bm;
    }

    public static <A> Maybe<A> find(final Predicate<A> f, final Collection<A> ac){
        for(A a : ac){
            if(f.apply(a))
                return Maybe.just(a);
        }
        return Maybe.nothing();
    }

    public static <A> boolean exists(final Predicate<A> f, final Collection<A> ac){
        return Functional.find(f,ac).hasValue();
    }

    public static <A,B> Collection<B> parMap(final Function<A,B> f, final Collection<A> ac){
        int cores = Runtime.getRuntime().availableProcessors();
        return Functional.parMap(f, ac, cores+2);
    }

    public static <A,B> Collection<B> parMap(final Function<A,B> f, final Collection<A> ac, final int processes){
        return Functional.parMap(f, ac, new ArrayList<B>(ac.size()), processes, executorService);
    }

    public static <A,B> Collection<B> parMap(final Function<A,B> f, final Collection<A> ac, final Collection<B> bc, final int processes){
        return Functional.parMap(f, ac, bc, processes, executorService);
    }

    private static class ParProcessor<A,B> implements Runnable {

        // state: 0 - waiting for data, 1 - processing, 2 - processed, 3 - exit
        private AtomicInteger state = new AtomicInteger(0);
        private Function<A,B> function;
        private A a = null;
        private B b = null;

        private ParProcessor(Function<A,B> function){
            this.function = function;
        }

        public boolean process(final A a){
            if(state.compareAndSet(0,1)){
                this.a = a;
                return true;
            }else{
                return false;
            }
        }

        public B get(){
            if(state.compareAndSet(2,0)){
                B b = this.b;
                return b;
            }else{
                return null;
            }
        }

        public B waitAndGet(){
            while(state.get() == 1){
                try {
                    Thread.sleep(10);
                }catch(InterruptedException ex){
                }
            }
            return get();
        }

        public void shutdown(){
            while(!state.compareAndSet(0,3)){
                try {
                    Thread.sleep(10);
                }catch(InterruptedException ex){
                }
            }
        }

        public void run(){
            while(true){
                int stateValue = state.get();
                if(stateValue == 1){
                    this.b = this.function.apply(this.a);
                    state.set(2);
                }else if(stateValue == 3){
                    break;
                }else{
                    try {
                        Thread.sleep(10);
                    }catch(InterruptedException ex){
                    }
                }
            }
        }

    }

    public static <A,B> Collection<B> parMap(final Function<A,B> f, final Collection<A> ac, final Collection<B> bc, final int processes, final ExecutorService executorService){
        if(ac.size() == 0)
            return bc;

        List<ParProcessor<A,B>> processors = new ArrayList<ParProcessor<A,B>>(processes);
        for(int i = 0; i < processes; i++){
            ParProcessor<A,B> p = new ParProcessor<A,B>(f);
            processors.add(p);
            executorService.submit(p);
        }

        ListIterator<ParProcessor<A,B>> pi = processors.listIterator();

        ParProcessor<A,B> p = null;
        B b = null;
acLoop: for(A a : ac){
            while(true){
                while(pi.hasNext()){
                    p = pi.next();
                    b = p.get();  
                    if(b != null)
                        bc.add(b);
                    if(p.process(a)){
                        continue acLoop;
                    }
                }
                while(pi.hasPrevious()){
                    p = pi.previous();
                    b = p.get();  
                    if(b != null)
                        bc.add(b);
                    if(p.process(a)){
                        continue acLoop;
                    }
                }
                try {
                    Thread.sleep(10);
                }catch(InterruptedException ex){
                }
            }
        }

        pi = processors.listIterator();
        while(pi.hasNext()){
            p = pi.next();
            b = p.waitAndGet();  
            if(b != null)
                bc.add(b);
            p.shutdown();
        }

        return bc;
    }
    

}
