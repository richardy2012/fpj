package com.github.fpj.monad.trans;

import com.github.fpj.Function;
import com.github.fpj.Functor;
import com.github.fpj.monad.Monad;

public class ReaderT<E,M extends Monad,A> extends AbstractMonadTrans<A> {

    private Function<E,Monad<A>> f;

    public static <E,M extends Monad,A> ReaderT<E,M,A> readerT(final Function<E,Monad<A>> f){
        if(f == null)
            throw new IllegalArgumentException("argument has no value");
        return new ReaderT<E,M,A>(f);
    }

    private ReaderT(final Function<E,Monad<A>> f){
        this.f = f;
    }

    public Function<E,Monad<A>> runReaderT(){
        return this.f;
    }

    public ReaderT<E,M,E> ask(){
        return new ReaderT<E,M,E>(new Function<E,Monad<E>>(){
            public Monad<E> apply(final E e){
                return runReaderT().apply(e).ret(e);
            }
        });
    }

    public <B> Monad<B> bind(final Function<A,Monad<B>> f){
        return new ReaderT<E,M,B>(new Function<E,Monad<B>>(){
            public Monad<B> apply(final E e){
                return runReaderT().apply(e).bind(f);            
            }
        });
    }

    public <B> Monad<B> ret(final B b){
        return new ReaderT<E,M,B>(new Function<E,Monad<B>>(){
            public Monad<B> apply(final E e){
                return runReaderT().apply(e).ret(b);            
            }
        });
    }

    public <B> Functor<B> fmap(final Function<A,B> f){
        return new ReaderT<E,M,B>(new Function<E,Monad<B>>(){
            public Monad<B> apply(final E e){
                return runReaderT().apply(e).bind(new Function<A,Monad<B>>(){
                    public Monad<B> apply(final A a){
                        return runReaderT().apply(e).ret(f.apply(a));
                    }
                });            
            }
        });
    }

}
