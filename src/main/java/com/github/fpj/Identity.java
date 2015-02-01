package com.github.fpj;

public class Identity<A> implements Function<A,A> {

    public static final Identity identity = new Identity();

    public static <A> Identity<A> identity(){
        return (Identity<A>)identity;
    }

    public A apply(final A a){
        return a;
    }

}

