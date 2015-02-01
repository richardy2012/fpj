package com.github.fpj.monoid;

import com.github.fpj.Function;
import com.github.fpj.Functor;

import java.util.Collection;

public class List<A> extends AbstractMonoid<A> implements Functor<A> {

    private A hd;
    private List<A> tl;
    private static final List empty = new List(null,null);

    public static <A> List<A> list(final A head, final List<A> tail){
        if(head == null)
            throw new IllegalArgumentException("head has no value");
        if(tail == null)
            throw new IllegalArgumentException("tail has no value");
        return new List<A>(head,tail);
    
    }

    public static <A> List<A> list(final Collection<A> ac){
        List<A> l = null;
        List<A> r = null;

        for(final A a : ac){
            if(l == null){
                l = new List<A>(a, null);
                r = l;
            }else{
                l.tl = new List<A>(a, null);            
                l = l.tl;
            }
        }

        if(r == null)
            return (List<A>)List.empty();
        else
            l.tl = List.empty();

        return r;
    }

    public static <A> List<A> empty(){
        return empty;
    }

    private List(final A head, final List<A> tail){
        this.hd = head;
        this.tl = tail;
    }

    public A value(){
        if(this.hd == null)
            throw new IllegalStateException("head has no value");
        return this.hd;
    }

    public A getValue(){
        if(this.hd == null)
            throw new IllegalStateException("head has no value");
        return this.hd;
    }

    public int length(){
        int len = 0;
        List<A> l = this;

        while(l.hasValue()){
            len++;
            l = l.tail();
        }

        return len;
    }

    public A head(){
        if(this.hd == null)
            throw new IllegalStateException("head has no value");
        return this.hd;
    }

    public List<A> tail(){
        if(this.tl == null)
            throw new IllegalStateException("has no tail");
        return this.tl;
    }

    public boolean isEmpty(){
        return this.hd == null;
    }

    public boolean isEmptyTail(){
        return this.tl.isEmpty();
    }

    public boolean hasValue(){
        return this.hd != null;
    } 

    public Monoid<A> mempty(){
        return List.empty();
    }

    public Monoid<A> mappend(final Monoid<A> a){

        if(isEmpty())
            return a;
        
        List<A> r = this;

        while(!r.isEmptyTail()){
            r = r.tl;
        }

        r.tl = (List<A>)a;

        return this;
    }

    public <B> Functor<B> fmap(final Function<A,B> f){

        if(isEmpty())
            return List.empty();

        List<A> al = this; 
        List<B> r = new List<B>(f.apply(al.value()),(List<B>)List.empty());
        List<B> r2 = r;

        while(!al.isEmptyTail()){
            al = al.tail();
            r2.tl = new List<B>(f.apply(al.value()), r2.tl);
            r2 = r2.tl;
        }
        
        return r;
    }
}


