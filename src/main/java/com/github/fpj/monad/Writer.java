package com.github.fpj.monad;

import com.github.fpj.Function;
import com.github.fpj.Functor;
import com.github.fpj.Tuple;
import com.github.fpj.Unit;
import com.github.fpj.monoid.Monoid;

public class Writer<W extends Monoid,A> extends AbstractMonad<A> {

    private Tuple<A,W> t;

    public static <W extends Monoid,A> Writer<W,A> writer(final Tuple<A,W> t){
        if(t == null)
            throw new IllegalArgumentException("argument has no value");
        return new Writer<W,A>(t);
    }

    public static <W extends Monoid,A> Writer<W,A> writer(final A a, final W w){
        return new Writer<W,A>(Tuple.tuple(a,w));
    }

    private Writer(Tuple<A,W> t){
        this.t = t;
    }

    public Tuple<A,W> runWriter(){
        return this.t;
    }

    public Writer<W,Unit> tell(final W w){
        return new Writer<W,Unit>(Tuple.tuple(Unit.unit(), (W)runWriter().second().mappend(w)));
    }

    public <B> Monad<B> bind(final Function<A,Monad<B>> f){
        final Writer<W,B> m = (Writer<W,B>)f.apply(runWriter().first());
        return new Writer<W,B>(Tuple.tuple(m.runWriter().first(), (W)runWriter().second().mappend(m.runWriter().second())));
    }

    public <B> Monad<B> ret(final B b){
        return new Writer<W,B>(Tuple.tuple(b, runWriter().second()));
    }

    public <B> Functor<B> fmap(final Function<A,B> f){
        return new Writer<W,B>(Tuple.tuple(f.apply(runWriter().first()),runWriter().second()));
    }
}



