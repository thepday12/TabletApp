package com.senter.demo.common.misc;

public final class Appliables {
    public interface Appliable1<TypeOfResult, TypeOfParameter> {
        public TypeOfResult apply(TypeOfParameter parameter);
    }

    public interface Appliable2<TypeOfResult, TypeOfParameter0, TypeOfParameter1> {
        public TypeOfResult apply(TypeOfParameter0 parameter0, TypeOfParameter1 parameter1);
    }

    public interface Appliable3<TypeOfResult, TypeOfParameter0, TypeOfParameter1, TypeOfParameter2> {
        public TypeOfResult apply(TypeOfParameter0 parameter0, TypeOfParameter1 parameter1, TypeOfParameter2 parameter2);
    }
}
