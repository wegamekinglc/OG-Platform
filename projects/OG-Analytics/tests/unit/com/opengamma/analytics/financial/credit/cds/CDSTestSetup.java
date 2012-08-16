package com.opengamma.analytics.financial.credit.cds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.time.calendar.Period;
import javax.time.calendar.TimeZone;
import javax.time.calendar.ZonedDateTime;

import com.opengamma.analytics.financial.instrument.annuity.AnnuityCouponFixedDefinition;
import com.opengamma.analytics.financial.instrument.annuity.AnnuityPaymentFixedDefinition;
import com.opengamma.analytics.financial.instrument.cds.CDSDefinition;
import com.opengamma.analytics.financial.instrument.cds.CDSPremiumDefinition;
import com.opengamma.analytics.financial.instrument.payment.PaymentFixedDefinition;
import com.opengamma.analytics.financial.interestrate.PeriodicInterestRate;
import com.opengamma.analytics.financial.interestrate.YieldCurveBundle;
import com.opengamma.analytics.financial.model.interestrate.curve.YieldCurve;
import com.opengamma.analytics.math.curve.InterpolatedDoublesCurve;
import com.opengamma.analytics.math.interpolation.LinearInterpolator1D;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.businessday.FollowingBusinessDayConvention;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.financial.convention.calendar.MondayToFridayCalendar;
import com.opengamma.financial.convention.daycount.ActualThreeSixty;
import com.opengamma.financial.convention.daycount.ActualThreeSixtyFive;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.frequency.Frequency;
import com.opengamma.financial.convention.frequency.PeriodFrequency;
import com.opengamma.financial.convention.frequency.SimpleFrequency;
import com.opengamma.util.money.Currency;

public class CDSTestSetup {
  
  protected static DayCount s_act365 = new ActualThreeSixtyFive();
  
  public static double getTimeBetween(final ZonedDateTime date1, final ZonedDateTime date2) {

    final ZonedDateTime rebasedDate2 = date2.withZoneSameInstant(date1.getZone());
    final boolean timeIsNegative = date1.isAfter(rebasedDate2);

    if (!timeIsNegative) {
      return s_act365.getDayCountFraction(date1, rebasedDate2);
    }
    else {
      return -1.0 * s_act365.getDayCountFraction(rebasedDate2, date1);
    }
  }
  
  
  /*
   * ------------------------------------------
   * Test data for the Simple CDS pricing model
   * ------------------------------------------
   */
  
  protected CDSDefinition loadCDS_SimpleModel() {
    
    ZonedDateTime pricingDate = ZonedDateTime.of(2010, 12, 31, 0, 0, 0, 0, TimeZone.UTC);

  
    ZonedDateTime startDate = ZonedDateTime.of(2010, 12, 20, 0, 0, 0, 0, TimeZone.UTC);
    ZonedDateTime maturity = ZonedDateTime.of(2020, 12, 20, 0, 0, 0, 0, TimeZone.UTC);
    ZonedDateTime bondMaturity = ZonedDateTime.of(2016, 6, 20, 0, 0, 0, 0, TimeZone.UTC);
    
    double notional = 1.0;
    double spread = 0.0025;
    double recoveryRate = 0.6;
    Currency currency = Currency.GBP;
    Frequency premiumFrequency = SimpleFrequency.QUARTERLY;
    PeriodFrequency bondPremiumFrequency = PeriodFrequency.ANNUAL;

    Calendar calendar = new MondayToFridayCalendar("TestCalendar");
    DayCount dayCount = new ActualThreeSixtyFive();
    BusinessDayConvention convention = new FollowingBusinessDayConvention();
  
    final AnnuityCouponFixedDefinition premiumDefinition = AnnuityCouponFixedDefinition.from(currency, startDate, maturity, premiumFrequency, calendar, dayCount, convention, /*EOM*/ false, notional, spread, /*isPayer*/false);
  
    List<ZonedDateTime> possibleDefaultDates = scheduleDatesInRange(bondMaturity, bondPremiumFrequency.getPeriod(), pricingDate, maturity, calendar, convention);
    if (maturity.isAfter(bondMaturity)) {
      possibleDefaultDates.add(convention.adjustDate(calendar, maturity));
    }
  
    PaymentFixedDefinition[] defaultPayments = new PaymentFixedDefinition[possibleDefaultDates.size()];
  
    for (int i = 0; i < defaultPayments.length; ++i) {
      defaultPayments[i] = new PaymentFixedDefinition(currency, possibleDefaultDates.get(i), notional * (1.0 - recoveryRate));
    }
  
    final AnnuityPaymentFixedDefinition payoutDefinition = new AnnuityPaymentFixedDefinition(defaultPayments);
    
    return new CDSDefinition(premiumDefinition, payoutDefinition, startDate, maturity, notional, spread, recoveryRate, /* accrualOnDefault */ true, /* payOnDefault */ true, /* protectStart */ true, dayCount);
    
  }
  
  protected static List<ZonedDateTime> scheduleDatesInRange(ZonedDateTime maturity, Period schedulePeriod, ZonedDateTime earliest, ZonedDateTime latest,
    Calendar calendar, BusinessDayConvention convention) {

  List<ZonedDateTime> datesInRange = new ArrayList<ZonedDateTime>();
  ZonedDateTime scheduleDate = maturity;
  int periods = 0;

  while (scheduleDate.isAfter(latest)) {
    scheduleDate = convention.adjustDate(calendar, maturity.minus(schedulePeriod.multipliedBy(++periods)));
  }

  while (!scheduleDate.isBefore(earliest)) {
    datesInRange.add(scheduleDate);
    scheduleDate = convention.adjustDate(calendar, maturity.minus(schedulePeriod.multipliedBy(++periods)));
  }

  Collections.reverse(datesInRange);
  return datesInRange;
}
  
  protected YieldCurveBundle loadCurveBundle_SimpleModel() {
    
    final double[] timePoints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
    final double[] cdsCcyPoints = {
      0.00673222222222214000,
      0.00673222222222214000,
      0.01429905379554900000,
      0.02086135487690320000,
      0.02630494452289900000,
      0.03072389009716760000,
      0.03406975577292020000,
      0.03670379007549540000,
      0.03859767722179330000,
      0.04029139055351690000,
      0.04171488783608950000,
      0.04171488783608950000
    };
    final double[] bondCcyPoints = {
      0.00673222222222214000,
      0.00673222222222214000,
      0.01429905379554900000,
      0.02086135487690320000,
      0.02630494452289900000,
      0.03072389009716760000,
      0.03406975577292020000,
      0.03670379007549540000,
      0.03859767722179330000,
      0.04029139055351690000,
      0.04171488783608950000,
      0.04171488783608950000
    };
    final double[] spreadPoints = {
      0.008094573225337830000000000000,
      0.008094573225337830000000000000,
      0.008472028609360500000000000000,
      0.008833186263998250000000000000,
      0.009178825884456880000000000000,
      0.009509688657093270000000000000,
      0.009826479094981490000000000000,
      0.010129866801184300000000000000,
      0.010420488160288400000000000000,
      0.010698947959110100000000000000,
      0.010965820937831700000000000000,
      0.010965820937831700000000000000
    };
  
    final YieldCurve cdsCcyYieldCurve = YieldCurve.from(InterpolatedDoublesCurve.fromSorted(timePoints, cdsCcyPoints, new LinearInterpolator1D()));
    final YieldCurve bondCcyYieldCurve = YieldCurve.from(InterpolatedDoublesCurve.fromSorted(timePoints, bondCcyPoints, new LinearInterpolator1D()));
    final YieldCurve spreadCurve = YieldCurve.from(InterpolatedDoublesCurve.fromSorted(timePoints, spreadPoints, new LinearInterpolator1D()));
  
    YieldCurveBundle bundle = new YieldCurveBundle();
    bundle.setCurve("CDS_CCY", cdsCcyYieldCurve);
    bundle.setCurve("BOND_CCY", bondCcyYieldCurve);
    bundle.setCurve("SPREAD", spreadCurve);
    
    return bundle;
  }
  
  
  /*
   * -------------------------------------------------------------------------------
   * Data for the ISDA examples provided in main.c as test driver for reference code
   * -------------------------------------------------------------------------------
   */
  
  protected CDSDefinition loadCDS_ISDAExampleMainC(final double spreadBasisPoints) {
    
    final ZonedDateTime startDate = ZonedDateTime.of(2008, 2, 8, 0, 0, 0, 0, TimeZone.UTC);
    final ZonedDateTime maturity = ZonedDateTime.of(2008, 2, 12, 0, 0, 0, 0, TimeZone.UTC);
    
    final double notional = 1.0e7;
    final double spread = spreadBasisPoints/10000.0;
    final double recoveryRate = 0.4;

    final Frequency premiumFrequency = SimpleFrequency.QUARTERLY;
    final Calendar calendar = new MondayToFridayCalendar("TestCalendar");
    final DayCount dayCount = new ActualThreeSixty();
    final BusinessDayConvention convention = new FollowingBusinessDayConvention();

    final CDSPremiumDefinition premiumDefinition = CDSPremiumDefinition.fromISDA(Currency.USD, startDate, maturity, premiumFrequency, calendar, dayCount, convention, notional, spread, /* protectStart */ true);

    return new CDSDefinition(premiumDefinition, null, startDate, maturity, notional, spread, recoveryRate, /* accrualOnDefault */ true, /* payOnDefault */ true, /* protectStart */ true, dayCount);
  }
  
  protected ISDACurve loadDiscountCurve_ISDAExampleMainC() {
    
    final ZonedDateTime baseDate = ZonedDateTime.of(2008, 2, 1, 0, 0, 0, 0, TimeZone.UTC);
    
    double[] times = {
        getTimeBetween(baseDate, ZonedDateTime.of(2008, 2, 3, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2008, 3, 3, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2008, 4, 3, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2008, 7, 3, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2008, 10, 3, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2009, 1, 5, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2009, 7, 3, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2010, 1, 4, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2010, 7, 5, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2011, 1, 3, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2011, 7, 4, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2012, 1, 3, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2012, 7, 3, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2013, 1, 3, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2013, 7, 3, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2014, 1, 3, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2014, 7, 3, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2015, 1, 5, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2015, 7, 3, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2016, 1, 4, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2016, 7, 4, 0, 0, 0, 0, TimeZone.UTC)),
        getTimeBetween(baseDate, ZonedDateTime.of(2017, 1, 3, 0, 0, 0, 0, TimeZone.UTC))
      };
  
      double[] rates = {
        0.000000001013888972778431700000,
        0.000000001013888972778431700000,
        0.000000001013888972778431700000,
        0.000000001013888972778431700000,
        0.000000001013888972778431700000,
        0.000000000997358196030972980000,
        0.000000000995057147790134880000,
        0.000000000993861384526237670000,
        0.000000000983571668555782710000,
        0.000000000976699218749570080000,
        0.000000000989587300992411660000,
        0.000000000999308693520927250000,
        0.000000000999070826068759740000,
        0.000000000998878776248821030000,
        0.000000000998947369268421430000,
        0.000000000999005352108518340000,
        0.000000000999007765400961030000,
        0.000000000999009754006644690000,
        0.000000000998901406035201940000,
        0.000000000998803566131306970000,
        0.000000000998365168314307990000,
        0.000000000997973248135311370000
      };
      
      return new ISDACurve("IR_CURVE", times, rates, 0.0);
  }
  
  protected ISDACurve loadHazardRateCurve_ISDAExampleMainC() {
    
    final ZonedDateTime baseDate = ZonedDateTime.of(2008, 2, 1, 0, 0, 0, 0, TimeZone.UTC);

    double[] times = {
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2008, 2, 12, 0, 0, 0, 0, TimeZone.UTC))
    };
    
    double[] rates = {
        (new PeriodicInterestRate(0.81582707425206369, 1)).toContinuous().getRate()
    };
    
    return new ISDACurve("HAZARD_RATE_CURVE", times, rates, 0.0);
  }


  /*
   * ---------------------------------------------------------------------------------
   * Data for the ISDA examples provided in the example Excel file, CDS calculator tab
   * ---------------------------------------------------------------------------------
   */
  
  protected CDSDefinition loadCDS_ISDAExampleCDSCalcualtor() {
    
    final ZonedDateTime startDate = ZonedDateTime.of(2008, 3, 20, 0, 0, 0, 0, TimeZone.UTC);
    final ZonedDateTime maturity = ZonedDateTime.of(2013, 3, 20, 0, 0, 0, 0, TimeZone.UTC);
    final double notional = 10000000, spread = 0.01 /* 100bp */, recoveryRate = 0.4;
    
    final Frequency couponFrequency = SimpleFrequency.QUARTERLY;
    final Calendar calendar = new MondayToFridayCalendar("TestCalendar");
    final DayCount dayCount = new ActualThreeSixty();
    final BusinessDayConvention convention = new FollowingBusinessDayConvention();
    
    final CDSPremiumDefinition premiumDefinition = CDSPremiumDefinition.fromISDA(Currency.USD, startDate, maturity, couponFrequency, calendar, dayCount, convention, notional, spread, /* protect start */ true);
    
    return new CDSDefinition(premiumDefinition, null, startDate, maturity, notional, spread, recoveryRate, /* accrualOnDefault */ true, /* payOnDefault */ true, /* protectStart */ true, dayCount);
  }
  
  protected CDSDefinition loadCDS_ISDAExampleUpfrontConverter() {
    
    final ZonedDateTime startDate = ZonedDateTime.of(2007, 3, 20, 0, 0, 0, 0, TimeZone.UTC);
    final ZonedDateTime maturity = ZonedDateTime.of(2013, 6, 20, 0, 0, 0, 0, TimeZone.UTC);
    final double notional = 10000000, spread = 0.05 /* 500bp */, recoveryRate = 0.4;
    
    final Frequency couponFrequency = SimpleFrequency.QUARTERLY;
    final Calendar calendar = new MondayToFridayCalendar("TestCalendar");
    final DayCount dayCount = new ActualThreeSixty();
    final BusinessDayConvention convention = new FollowingBusinessDayConvention();
    
    final CDSPremiumDefinition premiumDefinition = CDSPremiumDefinition.fromISDA(Currency.USD, startDate, maturity, couponFrequency, calendar, dayCount, convention, notional, spread, /* protect start */ true);
    
    return new CDSDefinition(premiumDefinition, null, startDate, maturity, notional, spread, recoveryRate, /* accrualOnDefault */ true, /* payOnDefault */ true, /* protectStart */ true, dayCount);
  }
  
  protected ISDACurve loadHazardRateCurve_ISDAExampleCDSCalculator() {
    
    final ZonedDateTime baseDate = ZonedDateTime.of(2008, 9, 18, 0, 0, 0, 0, TimeZone.UTC);
    
    double[] times = {
      0.0,
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2013, 06, 20, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2015, 06, 20, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2018, 06, 20, 0, 0, 0, 0, TimeZone.UTC))
    };
    
    double[] rates = {
      (new PeriodicInterestRate(0.09709857471184660000,1)).toContinuous().getRate(),
      (new PeriodicInterestRate(0.09709857471184660000,1)).toContinuous().getRate(),
      (new PeriodicInterestRate(0.09705141266558010000,1)).toContinuous().getRate(),
      (new PeriodicInterestRate(0.09701141671498870000,1)).toContinuous().getRate()
    };
    
    return new ISDACurve("HAZARD_RATE_CURVE", times, rates, 0.0);
  }
  
  protected ISDACurve loadHazardRateCurve_ISDAExampleCDSCalculator_FlatIRCurve() {
    
    final ZonedDateTime baseDate = ZonedDateTime.of(2008, 9, 18, 0, 0, 0, 0, TimeZone.UTC);
    
    final double[] times = {
      0.0,
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2013, 6, 20, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2015, 6, 20, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2018, 6, 20, 0, 0, 0, 0, TimeZone.UTC)),
    };
    
    final double ccRate = (new PeriodicInterestRate(0.09740867310916100000, 1)).toContinuous().getRate();
    double[] rates = {
      ccRate, ccRate, ccRate, ccRate
    };
    
    return new ISDACurve("HAZARD_RATE_CURVE", times, rates, 0.0);
  }
  
  protected ISDACurve loadDiscountCurve_ISDAExampleExcel() {
    
    final ZonedDateTime pricingDate = ZonedDateTime.of(2008, 9, 18, 0, 0, 0, 0, TimeZone.UTC);
    final ZonedDateTime baseDate = ZonedDateTime.of(2008, 9, 22, 0, 0, 0, 0, TimeZone.UTC);
    
    System.out.println( "from a day to itself is: " + s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2008, 9, 22, 0, 0, 0, 0, TimeZone.UTC)) );
    
    double[] times = { 
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2008, 10, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2008, 11, 24, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2008, 12, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2009, 3, 23, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2009, 6, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2009, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2010, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2010, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2011, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2011, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2012, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2012, 9, 24, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2013, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2013, 9, 23, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2014, 3, 24, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2014, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2015, 3, 23, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2015, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2016, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2016, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2017, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2017, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2018, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2018, 9, 24, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2019, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2019, 9, 23, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2020, 3, 23, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2020, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2021, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2021, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2022, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2022, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2023, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2023, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2024, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2024, 9, 23, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2025, 3, 24, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2025, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2026, 3, 23, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2026, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2027, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2027, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2028, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2028, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2029, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2029, 9, 24, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2030, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2030, 9, 23, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2031, 3, 24, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2031, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2032, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2032, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2033, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2033, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2034, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2034, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2035, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2035, 9, 24, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2036, 3, 24, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2036, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2037, 3, 23, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2037, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2038, 3, 22, 0, 0, 0, 0, TimeZone.UTC)),
      s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2038, 9, 22, 0, 0, 0, 0, TimeZone.UTC)),
    };
    
    double[] rates = {
        (new PeriodicInterestRate(0.00452115893602745000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.00965814197655757000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.01256719569422680000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.01808999617970230000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.01966710100627830000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.02112741666666660000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.01809534760435110000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.01655763824251000000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.01880609764411780000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.02033274208031280000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.02201082479582110000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.02329627269146610000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.02457991990962620000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.02564349380607000000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.02664198869678810000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.02747534265210970000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.02822421752113560000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.02887011718207980000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.02947938315126190000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03001849170997110000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03051723047721790000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03096814372457490000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03140378315953840000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03180665717369410000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03220470040815960000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03257895748982500000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03300576868204530000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03339934269742980000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03371439235915700000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03401013049588440000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03427957764613110000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03453400145380310000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03476707646146720000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03498827591548650000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03504602653686710000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03510104623115760000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03515188034751750000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03519973661653090000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03524486925430900000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03528773208373260000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03532784361012300000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03536647655059340000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03540272683370320000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03543754047166620000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03539936837313170000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03536201961264760000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03532774866571060000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03529393446018300000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03526215518920560000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03523175393297300000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03520264296319420000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03517444167763210000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03514783263597550000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03512186451200650000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03510945878934860000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03509733233582990000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03508585365890470000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03507449693456950000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03506379166273740000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03505346751846350000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03504350450444570000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03503383205190350000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03502458863645770000, 1)).toContinuous().getRate(),
        (new PeriodicInterestRate(0.03501550511625420000, 1)).toContinuous().getRate()
    };
    
    return new ISDACurve("IR_CURVE", times, rates, s_act365.getDayCountFraction(pricingDate, baseDate));
  }
  
  protected ISDACurve loadDiscountCurve_ISDAExampleCDSExcelFlat() {
    
    final ZonedDateTime baseDate = ZonedDateTime.of(2008, 9, 18, 0, 0, 0, 0, TimeZone.UTC);
    
    double[] times = {
        0.0,
        s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2008, 11, 28, 0, 0, 0, 0, TimeZone.UTC)),
        s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2009, 2, 27, 0, 0, 0, 0, TimeZone.UTC)),
        s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2009, 5, 29, 0, 0, 0, 0, TimeZone.UTC)),
        s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2010, 5, 29, 0, 0, 0, 0, TimeZone.UTC)),
        s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2011, 5, 29, 0, 0, 0, 0, TimeZone.UTC)),
        s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2012, 5, 29, 0, 0, 0, 0, TimeZone.UTC)),
        s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2013, 5, 29, 0, 0, 0, 0, TimeZone.UTC)),
        s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2014, 5, 29, 0, 0, 0, 0, TimeZone.UTC)),
        s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2015, 5, 29, 0, 0, 0, 0, TimeZone.UTC)),
        s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2016, 5, 29, 0, 0, 0, 0, TimeZone.UTC)),
        s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2017, 5, 29, 0, 0, 0, 0, TimeZone.UTC)),
        s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2018, 5, 29, 0, 0, 0, 0, TimeZone.UTC)),
        s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2019, 5, 29, 0, 0, 0, 0, TimeZone.UTC)),
        s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2020, 5, 29, 0, 0, 0, 0, TimeZone.UTC)),
        s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2023, 5, 29, 0, 0, 0, 0, TimeZone.UTC)),
        s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2028, 5, 29, 0, 0, 0, 0, TimeZone.UTC)),
        s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2033, 5, 29, 0, 0, 0, 0, TimeZone.UTC)),
        s_act365.getDayCountFraction(baseDate, ZonedDateTime.of(2038, 5, 29, 0, 0, 0, 0, TimeZone.UTC)),
    };
    
    double[] rates = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    
    return new ISDACurve("IR_CURVE", times, rates, 0.0);
  }

}
