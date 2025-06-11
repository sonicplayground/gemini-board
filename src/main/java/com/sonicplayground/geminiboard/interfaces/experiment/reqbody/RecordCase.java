package com.sonicplayground.geminiboard.interfaces.experiment.reqbody;

public record RecordCase(
    int pIntVar,
    Integer wIntVar,
    long pLongVar,
    Long wLongVar,
    double pDoubleVar,
    Double wDoubleVar,
    String StringVar,
    char pCharVar,
    Character wCharVar,
    boolean pBooleanVar,
    Boolean wBooleanVar,
    byte[] pByteArrayVar,
    Byte[] wByteArrayVar
) {

}
