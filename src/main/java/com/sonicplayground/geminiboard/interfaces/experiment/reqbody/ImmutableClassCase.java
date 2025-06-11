package com.sonicplayground.geminiboard.interfaces.experiment.reqbody;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@Builder
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
}
