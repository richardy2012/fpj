package com.github.fpj;

public interface Functor<A> {

    <B> Functor<B> fmap(final Function<A,B> f);

}

