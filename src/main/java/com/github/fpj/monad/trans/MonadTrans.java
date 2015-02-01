package com.github.fpj.monad.trans;

import com.github.fpj.monad.Monad;

public interface MonadTrans<A> extends Monad<A> {

    MonadTrans<A> lift(final Monad<A> m);

}

