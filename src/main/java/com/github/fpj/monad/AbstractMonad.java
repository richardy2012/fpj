package com.github.fpj.monad;

import com.github.fpj.Function;

public abstract class AbstractMonad<A> implements Monad<A> {

    public <B> Monad<B> bind(final Monad<B> b){
        return bind(new Function<A,Monad<B>>(){
            public Monad<B> apply(final A a){
                return b;            
            } 
         });
    }

    public <B> Monad<B> liftM(final Function<A,B> f){
        return bind(new Function<A,Monad<B>>(){
            public Monad<B> apply(final A a){
                return ret(f.apply(a));            
            } 
         });
    }

}

