package com.sonicplayground.geminiboard.domain.common;

public interface Constant {

    String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    String VEHICLE_MILEAGE = "mileage";
//    String VEHICLE_TIRE_REPLACEMENT_DATE = "tireReplacementDate";
    String VEHICLE_ENGINE_OIL_CHANGE_DATE = "engineOilChangeDate";
    String VEHICLE_BRAKE_PAD_REPLACEMENT_DATE = "brakePadReplacementDate";

    // New constants for individual tires
    String VEHICLE_TIRE_FORE_LEFT_REPLACEMENT_DATE = "tireForeLeftReplacementDate";
    String VEHICLE_TIRE_FORE_RIGHT_REPLACEMENT_DATE = "tireForeRightReplacementDate";
    String VEHICLE_TIRE_BACK_LEFT_REPLACEMENT_DATE = "tireBackLeftReplacementDate";
    String VEHICLE_TIRE_BACK_RIGHT_REPLACEMENT_DATE = "tireBackRightReplacementDate";

    String TIRE = "tire";
    String ENGINE_OIL = "engineOil";
    String BRAKE_PAD = "brakePad";

    int DEFAULT_ANNUAL_MILEAGE = 10000;

}
