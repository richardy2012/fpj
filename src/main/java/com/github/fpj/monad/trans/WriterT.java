package com.github.fpj.monad.trans;

import com.github.fpj.Function;
import com.github.fpj.Functor;
import com.github.fpj.Tuple;
import com.github.fpj.Unit;
import com.github.fpj.monad.Monad;
import com.github.fpj.monoid.Monoid;

public class WriterT<W extends Monoid,M extends Monad,A> extends AbstractMonadTrans<A> {

    private Monad<Tuple<A,W>> m;

    public static <W extends Monoid,M extends Monad,A> WriterT<W,M,A> writerT(final A a, final W w, final M m){
        return new WriterT<W,M,A>((Monad<Tuple<A,W>>)m.ret(Tuple.tuple(a,w)));
    }

    private WriterT(final Monad<Tuple<A,W>> m){
        this.m = m;
    }

    public Monad<Tuple<A,W>> runWriterT(){
        return this.m;
    }

    public WriterT<W,M,Unit> tell(final W w){
        return new WriterT<W,M,Unit>(runWriterT().bind(new Function<Tuple<A,W>,Monad<Tuple<Unit,W>>>(){
            public Monad<Tuple<Unit,W>> apply(final Tuple<A,W> t){
                return runWriterT().ret(Tuple.tuple(Unit.unit(),(W)t.second().mappend(w))); 
            }
        }));
    }

    public <B> Monad<B> bind(final Function<A,Monad<B>> f){
        return new WriterT<W,M,B>(runWriterT().bind(new Function<Tuple<A,W>,Monad<Tuple<B,W>>>(){
            public Monad<Tuple<B,W>> apply(final Tuple<A,W> t){
                return ((WriterT<W,M,B>) f.apply(t.first())).runWriterT().bind(new Function<Tuple<B,W>,Monad<Tuple<B,W>>>(){
                    public Monad<Tuple<B,W>> apply(final Tuple<B,W> t2){
                        return runWriterT().ret(Tuple.tuple(t2.first(),t.second()));
                    }                
                });
            }
        }));
    }

    public <B> Monad<B> ret(final B b){
        return new WriterT<W,M,B>(runWriterT().bind(new Function<Tuple<A,W>,Monad<Tuple<B,W>>>(){
            public Monad<Tuple<B,W>> apply(final Tuple<A,W> t){
                return runWriterT().ret(Tuple.tuple(b,t.second()));
            }
        }));
    }

    public <B> Functor<B> fmap(final Function<A,B> f){
        return new WriterT<W,M,B>(runWriterT().bind(new Function<Tuple<A,W>,Monad<Tuple<B,W>>>(){
            public Monad<Tuple<B,W>> apply(final Tuple<A,W> t){
                return runWriterT().ret(Tuple.tuple(f.apply(t.first()),t.second()));
            }
        }));
    }
}

