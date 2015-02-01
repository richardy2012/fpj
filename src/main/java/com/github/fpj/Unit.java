package com.github.fpj;

public class Unit {
    
    private static final Unit unit = new Unit();

    public static Unit unit(){
        return unit;
    }

}
