package com.github.fpj.monad.trans;

import com.github.fpj.Function;
import com.github.fpj.Functor;
import com.github.fpj.Tuple;
import com.github.fpj.Unit;
import com.github.fpj.monad.Monad;
import com.github.fpj.monoid.Monoid;

public class StateT<S,M extends Monad,A> extends AbstractMonadTrans<A> {

    private Function<S,Monad<Tuple<A,S>>> f;

    private StateT(final Function<S,Monad<Tuple<A,S>>> f){
        this.f = f;
    }

    public Function<S,Monad<Tuple<A,S>>> runStateT(){
        return this.f;
    }

    public StateT<S,M,S> get(){
        return new StateT<S,M,S>(new Function<S,Monad<Tuple<S,S>>>(){
            public Monad<Tuple<S,S>> apply(final S s){
                return runStateT().apply(s).ret(Tuple.tuple(s,s));
            }
        });
    }

    public StateT<S,M,Unit> put(final S ns){
        return new StateT<S,M,Unit>(new Function<S,Monad<Tuple<Unit,S>>>(){
            public Monad<Tuple<Unit,S>> apply(final S s){
                return runStateT().apply(s).ret(Tuple.tuple(Unit.unit(),ns));
            }
        });
    }

    public <B> Monad<B> bind(final Function<A,Monad<B>> f){
        return new StateT<S,M,B>(new Function<S,Monad<Tuple<B,S>>>(){
            public Monad<Tuple<B,S>> apply(final S s){
                return runStateT().apply(s).bind(new Function<Tuple<A,S>,Monad<Tuple<B,S>>>(){
                    public Monad<Tuple<B,S>> apply(final Tuple<A,S> t){
                        return f.apply(t.first()).bind(new Function<B,Monad<Tuple<B,S>>>(){
                            public Monad<Tuple<B,S>> apply(final B b){
                                return runStateT().apply(s).ret(Tuple.tuple(b,s));
                            }
                        });
                    }
                });
            }
        });
    }

    public <B> Monad<B> ret(final B b){
        return new StateT<S,M,B>(new Function<S,Monad<Tuple<B,S>>>(){
            public Monad<Tuple<B,S>> apply(final S s){
                return runStateT().apply(s).ret(Tuple.tuple(b,s));
            }
        });
    }

    public <B> Functor<B> fmap(final Function<A,B> f){
        return new StateT<S,M,B>(new Function<S,Monad<Tuple<B,S>>>(){
            public Monad<Tuple<B,S>> apply(final S s){
                return runStateT().apply(s).bind(new Function<Tuple<A,S>,Monad<Tuple<B,S>>>(){
                    public Monad<Tuple<B,S>> apply(final Tuple<A,S> t){
                        return runStateT().apply(s).ret(Tuple.tuple(f.apply(t.first()),s));                    
                    }
                });
            }
        });
    }

}
