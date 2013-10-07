/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.credit.creditdefaultswap.pricing.vanilla.isdanew.fastcalibration;

import com.opengamma.analytics.financial.credit.creditdefaultswap.pricing.vanilla.isdanew.ISDABaseTest;

/**
 * Test.
 */
public class CreditCruveCalibrationTest extends ISDABaseTest {
  // TODO: fix this test
  
  
//  private static final CDSAnalyticFactory CDS_FACTORY = new CDSAnalyticFactory().withPayAccOnDefault(true);
//  private static final Period[] PILLARS = new Period[] {Period.ofMonths(6), Period.ofYears(1), Period.ofYears(2), Period.ofYears(3), Period.ofYears(4), Period.ofYears(5), Period.ofYears(7),
//    Period.ofYears(10) };
//
//  @Test
//  public void test() {
//    final LocalDate tradeDate = LocalDate.of(2013, Month.SEPTEMBER, 5);
//    final LocalDate spotDate = addWorkDays(tradeDate.minusDays(1), 1, DEFAULT_CALENDAR);
//    final String[] yieldCurvePoints = new String[] {"1M", "2M", "3M", "6M", "1Y", "2Y", "3Y", "4Y", "5Y", "6Y", "7Y", "8Y", "9Y", "10Y", "12Y", "15Y", "20Y", "25Y", "30Y" };
//    final String[] yieldCurveInstruments = new String[] {"M", "M", "M", "M", "M", "S", "S", "S", "S", "S", "S", "S", "S", "S", "S", "S", "S", "S", "S" };
//    //final String[] yieldCurveInstruments = new String[] {"M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M" };
//    final double[] rates = new double[] {0.004919, 0.005006, 0.00515, 0.005906, 0.008813, 0.0088, 0.01195, 0.01534, 0.01836, 0.02096, 0.02322, 0.02514, 0.02673, 0.02802, 0.02997, 0.0318, 0.03331,
//      0.03383, 0.034 };
//    final ISDACompliantYieldCurve yieldCurve = makeYieldCurve(tradeDate, spotDate, yieldCurvePoints, yieldCurveInstruments, rates, ACT_ACT_ISDA, ACT_ACT_ISDA, Period.ofMonths(6));
//
//    final CDSAnalytic[] cds = CDS_FACTORY.makeIMMCDS(tradeDate, PILLARS);
//    final int n = PILLARS.length;
//    final double[] spreads = new double[] {0.01, 0.012, 0.015, 0.02, 0.023, 0.021, 0.02, 0.019 };
//    ArgumentChecker.isTrue(n == spreads.length, "spreads wrong length");
//    final CDSMarketInfo[] market = new CDSMarketInfo[n];
//    for (int i = 0; i < n; i++) {
//      market[i] = new CDSMarketInfo(spreads[i], 0, RECOVERY_RATE);
//    }
//
//    final AnalyticCDSPricer pricer = new AnalyticCDSPricer(true);
//    final CreditCurveCalibrator calibrator1 = new CreditCurveCalibrator(cds, yieldCurve);
//    final ISDACompliantCreditCurve cc1 = calibrator1.calibrate(market);
//    final FastCreditCurveBuilder calibrator2 = new FastCreditCurveBuilder(true);
//    final ISDACompliantCreditCurve cc2 = calibrator2.calibrateCreditCurve(cds, spreads, yieldCurve);
//    for (int i = 0; i < n; i++) {
//      //   System.out.println(cc1.getZeroRateAtIndex(i) + "\t" + cc2.getZeroRateAtIndex(i));
//      assertEquals(cc2.getZeroRateAtIndex(i), cc1.getZeroRateAtIndex(i), 1e-15);
//    }
//    //    System.out.println();
//    //    final double[] price = calibrator1.price(market, cc1);
//    //    for (int i = 0; i < n; i++) {
//    //      final double p = pricer.pv(cds[i], yieldCurve, cc2, spreads[i]);
//    //      System.out.println(price[i] + "\t" + p);
//    //    }
//    //    System.out.println();
//    //    final double[] protLegD = calibrator1.protLeg(RECOVERY_RATE, cc2);
//    //    final double[] premLegD = calibrator1.premLeg(spreads, cc2);
//    //    for (int i = 0; i < n; i++) {
//    //      final double protLeg = pricer.protectionLeg(cds[i], yieldCurve, cc2);
//    //      final double premLeg = pricer.pvPremiumLegPerUnitSpread(cds[i], yieldCurve, cc2, PriceType.CLEAN) * spreads[i];
//    //      System.out.println(protLeg + "\t" + protLegD[i] + "\t" + premLeg + "\t" + premLegD[i]);
//    //    }
//
//    final int warmups = 200;
//    final int hotspots = 1000;
//
//    for (int i = 0; i < warmups; i++) {
//      //      final CreditCurveCalibrator calibrator1a = new CreditCurveCalibrator(cds, yieldCurve);
//      //      final FastCreditCurveBuilder calibrator2a = new FastCreditCurveBuilder(true);
//      final ISDACompliantCreditCurve cc1a = calibrator1.calibrate(market);
//      final ISDACompliantCreditCurve cc2a = calibrator2.calibrateCreditCurve(cds, spreads, yieldCurve);
//    }
//
//    long t0 = System.nanoTime();
//    for (int i = 0; i < hotspots; i++) {
//      //  final CreditCurveCalibrator calibrator1a = new CreditCurveCalibrator(cds, yieldCurve);
//      final ISDACompliantCreditCurve cc1a = calibrator1.calibrate(market);
//    }
//    long t1 = System.nanoTime();
//    System.out.println("time of new calibration: " + (t1 - t0) / hotspots / 1e6 + "ms");
//
//    t0 = System.nanoTime();
//    for (int i = 0; i < hotspots; i++) {
//      //     final FastCreditCurveBuilder calibrator2a = new FastCreditCurveBuilder(true);
//      final ISDACompliantCreditCurve cc2a = calibrator2.calibrateCreditCurve(cds, spreads, yieldCurve);
//    }
//    t1 = System.nanoTime();
//    System.out.println("time of old calibration: " + (t1 - t0) / hotspots / 1e6 + "ms");
//  }
//
//  @Test
//  public void test2() {
//    final LocalDate tradeDate = LocalDate.of(2013, Month.SEPTEMBER, 5);
//    final LocalDate spotDate = addWorkDays(tradeDate.minusDays(1), 1, DEFAULT_CALENDAR);
//    final String[] yieldCurvePoints = new String[] {"1M", "2M", "3M", "6M", "1Y", "2Y", "3Y", "4Y", "5Y", "6Y", "7Y", "8Y", "9Y", "10Y", "12Y", "15Y", "20Y", "25Y", "30Y" };
//    final String[] yieldCurveInstruments = new String[] {"M", "M", "M", "M", "M", "S", "S", "S", "S", "S", "S", "S", "S", "S", "S", "S", "S", "S", "S" };
//    //final String[] yieldCurveInstruments = new String[] {"M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M", "M" };
//    final double[] rates = new double[] {0.004919, 0.005006, 0.00515, 0.005906, 0.008813, 0.0088, 0.01195, 0.01534, 0.01836, 0.02096, 0.02322, 0.02514, 0.02673, 0.02802, 0.02997, 0.0318, 0.03331,
//      0.03383, 0.034 };
//    final ISDACompliantYieldCurve yieldCurve = makeYieldCurve(tradeDate, spotDate, yieldCurvePoints, yieldCurveInstruments, rates, ACT_ACT_ISDA, ACT_ACT_ISDA, Period.ofMonths(6));
//
//    final LocalDate[] maturities = new LocalDate[] {LocalDate.of(2013, Month.OCTOBER, 30), LocalDate.of(2014, Month.SEPTEMBER, 20), LocalDate.of(2015, Month.JUNE, 10),
//      LocalDate.of(2016, Month.SEPTEMBER, 5), LocalDate.of(2017, Month.OCTOBER, 1), LocalDate.of(2018, Month.DECEMBER, 30), LocalDate.of(2020, Month.JANUARY, 12),
//      LocalDate.of(2023, Month.OCTOBER, 30) };
//
//    final LocalDate effective = LocalDate.of(2013, Month.AUGUST, 1);
//    final CDSAnalytic[] cds = CDS_FACTORY.makeCDS(tradeDate, effective, maturities);
//
//    final int n = cds.length;
//    final double[] spreads = new double[] {0.01, 0.012, 0.015, 0.02, 0.023, 0.021, 0.02, 0.019 };
//    ArgumentChecker.isTrue(n == spreads.length, "spreads wrong length");
//    final CDSMarketInfo[] market = new CDSMarketInfo[n];
//    for (int i = 0; i < n; i++) {
//      market[i] = new CDSMarketInfo(spreads[i], 0, RECOVERY_RATE);
//    }
//
//    final AnalyticCDSPricer pricer = new AnalyticCDSPricer(true);
//    final CreditCurveCalibrator calibrator1 = new CreditCurveCalibrator(cds, yieldCurve);
//    final ISDACompliantCreditCurve cc1 = calibrator1.calibrate(market);
//    final FastCreditCurveBuilder calibrator2 = new FastCreditCurveBuilder(true);
//    final ISDACompliantCreditCurve cc2 = calibrator2.calibrateCreditCurve(cds, spreads, yieldCurve);
//    for (int i = 0; i < n; i++) {
//      System.out.println(cc1.getZeroRateAtIndex(i) + "\t" + cc2.getZeroRateAtIndex(i));
//      assertEquals(cc2.getZeroRateAtIndex(i), cc1.getZeroRateAtIndex(i), 1e-15);
//    }
//
//    final int warmups = 200;
//    final int hotspots = 1000;
//
//    for (int i = 0; i < warmups; i++) {
//      //      final CreditCurveCalibrator calibrator1a = new CreditCurveCalibrator(cds, yieldCurve);
//      //      final FastCreditCurveBuilder calibrator2a = new FastCreditCurveBuilder(true);
//      final ISDACompliantCreditCurve cc1a = calibrator1.calibrate(market);
//      final ISDACompliantCreditCurve cc2a = calibrator2.calibrateCreditCurve(cds, spreads, yieldCurve);
//    }
//
//    long t0 = System.nanoTime();
//    for (int i = 0; i < hotspots; i++) {
//      //  final CreditCurveCalibrator calibrator1a = new CreditCurveCalibrator(cds, yieldCurve);
//      final ISDACompliantCreditCurve cc1a = calibrator1.calibrate(market);
//    }
//    long t1 = System.nanoTime();
//    System.out.println("time of new calibration: " + (t1 - t0) / hotspots / 1e6 + "ms");
//
//    t0 = System.nanoTime();
//    for (int i = 0; i < hotspots; i++) {
//      //     final FastCreditCurveBuilder calibrator2a = new FastCreditCurveBuilder(true);
//      final ISDACompliantCreditCurve cc2a = calibrator2.calibrateCreditCurve(cds, spreads, yieldCurve);
//    }
//    t1 = System.nanoTime();
//    System.out.println("time of old calibration: " + (t1 - t0) / hotspots / 1e6 + "ms");
//
//  }

}
