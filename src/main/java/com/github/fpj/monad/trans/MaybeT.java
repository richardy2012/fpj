package com.github.fpj.monad.trans;

import com.github.fpj.Function;
import com.github.fpj.Functor;
import com.github.fpj.monad.Monad;
import com.github.fpj.monad.Maybe;

public class MaybeT<M extends Monad,A> extends AbstractMonadTrans<A> {

    private Monad<Maybe<A>> m;

    public static <M extends Monad,A> MaybeT<M,A> maybeT(final Maybe<A> a, final M m){
        if(a == null)
            throw new IllegalArgumentException("argument has no value");
        if(m == null)
            throw new IllegalArgumentException("monad has no value");
        return new MaybeT<M,A>(m.ret(a));
    }

    public static <M extends Monad,A> MaybeT<M,A> maybeT(final Monad<Maybe<A>> m){
        if(m == null)
            throw new IllegalArgumentException("monad has no value");
        return new MaybeT<M,A>(m);
    }

    private MaybeT(final Monad<Maybe<A>> m){
        this.m = m;
    }

    public Monad<Maybe<A>> runMaybeT(){
        return this.m;
    }

    public <B> Monad<B> bind(final Function<A,Monad<B>> f){
        return new MaybeT<M,B>(runMaybeT().bind(new Function<Maybe<A>,Monad<Maybe<B>>>(){
            public Monad<Maybe<B>> apply(final Maybe<A> a){
                if(a.isNothing())
                    return runMaybeT().ret((Maybe<B>)Maybe.nothing());
                else{
                    return ((MaybeT<M,B>)f.apply(a.value())).runMaybeT();
                }
            } 
        }));
    }

    public <B> Monad<B> ret(final B b){
        return new MaybeT<M,B>(runMaybeT().ret(Maybe.just(b)));
    }

    public <B> Functor<B> fmap(final Function<A,B> f){
        return new MaybeT<M,B>(runMaybeT().bind(new Function<Maybe<A>,Monad<Maybe<B>>>(){
            public Monad<Maybe<B>> apply(final Maybe<A> a){
                return runMaybeT().ret((Maybe<B>)a.fmap(f));            
            } 
        }));
    }
}

