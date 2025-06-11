package com.sonicplayground.geminiboard.interfaces.experiment.reqbody;

import java.beans.ConstructorProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Getter
@ToString
public class ImmutableClassCase {
    private final int pIntVar;
    private final Integer wIntVar;
    private final long pLongVar;
    private final Long wLongVar;
    private final double pDoubleVar;
    private final Double wDoubleVar;
    private final String StringVar;
    private final char pCharVar;
    private final Character wCharVar;
    private final boolean pBooleanVar;
    private final Boolean wBooleanVar;
    private final byte[] pByteArrayVar;
    private final Byte[] wByteArrayVar;

    @ConstructorProperties({"pIntVar", "wIntVar", "pLongVar", "wLongVar", "pDoubleVar",
        "wDoubleVar", "StringVar", "pCharVar", "wCharVar", "pBooleanVar", "wBooleanVar",
        "pByteArrayVar", "wByteArrayVar"})
    public ImmutableClassCase(int pIntVar, Integer wIntVar, long pLongVar, Long wLongVar,
                              double pDoubleVar, Double wDoubleVar, String StringVar,
                              char pCharVar, Character wCharVar, boolean pBooleanVar,
                              Boolean wBooleanVar, byte[] pByteArrayVar, Byte[] wByteArrayVar) {
        this.pIntVar = pIntVar;
        this.wIntVar = wIntVar;
        this.pLongVar = pLongVar;
        this.wLongVar = wLongVar;
        this.pDoubleVar = pDoubleVar;
        this.wDoubleVar = wDoubleVar;
        this.StringVar = StringVar;
        this.pCharVar = pCharVar;
        this.wCharVar = wCharVar;
        this.pBooleanVar = pBooleanVar;
        this.wBooleanVar = wBooleanVar;
        this.pByteArrayVar = pByteArrayVar;
        this.wByteArrayVar = wByteArrayVar;
    }
}
