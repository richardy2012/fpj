package com.github.fpj.monoid;

public interface Monoid<A> {

    Monoid<A> mempty(); 

    Monoid<A> mappend(final Monoid<A> a);

}


