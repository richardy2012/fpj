package com.github.fpj.monad;

import com.github.fpj.Function;
import com.github.fpj.Functor;
import com.github.fpj.Identity;

public class Reader<E,A> extends AbstractMonad<A> {

    private Function<E,A> f;

    public static <E,A> Reader<E,A> reader(final Function<E,A> f){
        if(f == null)
            throw new IllegalArgumentException("argument has no value");
        return new Reader<E,A>(f);
    }

    private Reader(final Function<E,A> f){
        this.f = f;
    }

    public Function<E,A> runReader(){
        return this.f;
    }

    public Reader<E,E> ask(){
        return new Reader<E,E>((Function<E,E>)Identity.identity());
    }

    public <B> Monad<B> bind(final Function<A,Monad<B>> f){
        return new Reader<E,B>(
                new Function<E,B>(){
                    public B apply(final E e){
                        return ((Reader<E,B>)f.apply(runReader().apply(e))).runReader().apply(e);
                    }
                });
    }

    public <B> Monad<B> ret(final B b){
        return new Reader<E,B>(
                new Function<E,B>(){
                   public B apply(final E e){
                       return b;
                   }
                });
    }

    public <B> Functor<B> fmap(final Function<A,B> f){
        return new Reader<E,B>(
                new Function<E,B>(){
                   public B apply(final E e){
                       return f.apply(runReader().apply(e));
                   }
               });
    }
}


