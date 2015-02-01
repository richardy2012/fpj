package com.github.fpj.monoid;

import com.github.fpj.Function;
import com.github.fpj.Function2;
import com.github.fpj.Functional;

import java.util.Collection;

public abstract class AbstractMonoid<A> implements Monoid<A> {

    public Monoid<A> mconcat(final List<Monoid<A>> al){
        return Functional.foldLeft(new Function2<Monoid<A>,Monoid<A>,Monoid<A>>(){
            public Monoid<A> apply(final Monoid<A> a, final Monoid<A> b){
                return a.mappend(b);
            }
        }, this.mempty(), al);
    }

    public Monoid<A> mconcat(final Collection<Monoid<A>> ac){
        return Functional.foldLeft(new Function2<Monoid<A>,Monoid<A>,Monoid<A>>(){
            public Monoid<A> apply(final Monoid<A> a, final Monoid<A> b){
                return a.mappend(b);
            }
        }, this.mempty(), ac);
    }

}


