package com.github.fpj.monad.trans;

import com.github.fpj.Function;
import com.github.fpj.monad.Monad;
import com.github.fpj.monad.AbstractMonad;

public abstract class AbstractMonadTrans<A> extends AbstractMonad<A> implements MonadTrans<A> {

    public MonadTrans<A> lift(final Monad<A> m){
        return (MonadTrans<A>)m.bind(new Function<A,Monad<A>>(){
            public Monad<A> apply(final A a){
                return ret(a);
            }
        });
    }
}


