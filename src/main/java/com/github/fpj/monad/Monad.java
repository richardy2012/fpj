package com.github.fpj.monad;

import com.github.fpj.Function;
import com.github.fpj.Functor;

public interface Monad<A> extends Functor<A> {

    <B> Monad<B> bind(final Function<A,Monad<B>> f);

    <B> Monad<B> ret(final B b);

}

