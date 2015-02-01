package com.github.fpj.monad.trans;

import com.github.fpj.Function;
import com.github.fpj.Functor;
import com.github.fpj.monad.Monad;
import com.github.fpj.monad.Maybe;
import com.github.fpj.monad.Either;

public class EitherT<M extends Monad,A,B> extends AbstractMonadTrans<B> {

    private Monad<Either<A,B>> m;

    public static <M extends Monad,A,B> EitherT<M,A,B> eitherT(final Either<A,B> a, final M m){
        if(a == null)
            throw new IllegalArgumentException("argument has no value");
        if(m == null)
            throw new IllegalArgumentException("monad has no value");
        return new EitherT<M,A,B>(m.ret(a));
    }

    public static <M extends Monad,A,B> EitherT<M,A,B> eitherT(final Monad<Either<A,B>> m){
        if(m == null)
            throw new IllegalArgumentException("monad has no value");
        return new EitherT<M,A,B>(m);
    }

    private EitherT(final Monad<Either<A,B>> m){
        this.m = m;
    }

    public Monad<Either<A,B>> runEitherT(){
        return this.m;
    }

    public <C> Monad<C> bind(final Function<B,Monad<C>> f){
        return new EitherT(runEitherT().bind(new Function<Either<A,B>,Monad<Either<A,C>>>(){
            public Monad<Either<A,C>> apply(final Either<A,B> a){
                if(a.isLeft())
                    return runEitherT().ret((Either<A,C>)Either.left(a.left()));
                else{
                    return ((EitherT<M,A,C>)f.apply(a.right())).runEitherT();
                }
            }
        }));
    }

    public <C> Monad<C> ret(final C c){
        return new EitherT(runEitherT().ret(Either.right(c)));
    }

    public <C> Functor<C> fmap(final Function<B,C> f){
        return new EitherT(runEitherT().bind(new Function<Either<A,B>,Monad<Either<A,C>>>(){
            public Monad<Either<A,C>> apply(final Either<A,B> a){
                return runEitherT().ret((Either<A,C>)a.fmap(f));            
            }
        }));
    }
}
