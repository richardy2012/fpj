package com.github.fpj.monad;

import com.github.fpj.Function;
import com.github.fpj.Functor;
import com.github.fpj.Tuple;
import com.github.fpj.Unit;

public class State<S,A> extends AbstractMonad<A> {

    private Function<S,Tuple<A,S>> f;

    public static <S,A> State<S,A> state(final Function<S,Tuple<A,S>> f){
        if(f == null)
            throw new IllegalArgumentException("argument has no value");
        return new State<S,A>(f);
    }

    public static <S,A> State<S,A> state(final A a){
        if(a == null)
            throw new IllegalArgumentException("argument has no value");
        return new State<S,A>(new Function<S,Tuple<A,S>>(){
            public Tuple<A,S> apply(final S s){
                return Tuple.tuple(a,s);
            }
        });
    }

    private State(final Function<S,Tuple<A,S>> f){
        this.f = f;
    }

    public Function<S,Tuple<A,S>> runState(){
        return this.f;
    }

    public State<S,S> get(){
        return new State<S,S>(new Function<S,Tuple<S,S>>(){
            public Tuple<S,S> apply(final S s){
                return Tuple.tuple(s,s);
            }
        });
    }

    public State<S,Unit> put(final S ns){
        return new State<S,Unit>(new Function<S,Tuple<Unit,S>>(){
            public Tuple<Unit,S> apply(final S s){
                return Tuple.tuple(Unit.unit(),ns);
            }
        });
    }

    public <B> Monad<B> bind(final Function<A,Monad<B>> f){
        return new State<S,B>(new Function<S,Tuple<B,S>>(){
            public Tuple<B,S> apply(final S s){
                return Tuple.tuple(((State<S,B>)f.apply(runState().apply(s).first())).runState().apply(s).first(), s);
            }
        });
    }

    public <B> Monad<B> ret(final B b){
        return new State<S,B>(new Function<S,Tuple<B,S>>(){
            public Tuple<B,S> apply(final S s){
                return Tuple.tuple(b,s);
            }
        });
    }

    public <B> Functor<B> fmap(final Function<A,B> f){
        return new State<S,B>(new Function<S,Tuple<B,S>>(){
            public Tuple<B,S> apply(final S s){
                return Tuple.tuple(f.apply(runState().apply(s).first()),s);
            }
        });
    }
}




