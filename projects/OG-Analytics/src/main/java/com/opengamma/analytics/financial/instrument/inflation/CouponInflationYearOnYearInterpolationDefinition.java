/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */

package com.opengamma.analytics.financial.instrument.inflation;

import javax.time.calendar.ZonedDateTime;

import com.opengamma.analytics.financial.instrument.InstrumentDefinitionVisitor;
import com.opengamma.analytics.financial.instrument.InstrumentDefinitionWithData;
import com.opengamma.analytics.financial.instrument.index.IndexPrice;
import com.opengamma.analytics.financial.interestrate.inflation.derivative.CouponInflationYearOnYearInterpolation;
import com.opengamma.analytics.financial.interestrate.payments.derivative.Coupon;
import com.opengamma.analytics.financial.interestrate.payments.derivative.Payment;
import com.opengamma.analytics.util.time.TimeCalculator;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.money.Currency;
import com.opengamma.util.timeseries.DoubleTimeSeries;

/**
 * Class describing a Year on Year inflation coupon were the inflation figure are interpolated.
 * The index for a given month is given in the yield curve and in the time series on the first of the month.
 * The pay-off is paymentYearFraction*(Index_End / Index_Start - X) with X=0 for notional payment and X=1 for no notional payment.
 */

public class CouponInflationYearOnYearInterpolationDefinition extends CouponInflationDefinition implements InstrumentDefinitionWithData<Payment, DoubleTimeSeries<ZonedDateTime>> {

  /**
   * The reference date for the index at the coupon start. May not be relevant as the index value is known.
   */
  private final ZonedDateTime[] _referenceStartDate;

  /**
   * The reference date for the index at the coupon end. The first of the month. There is usually a difference of two or three month between the reference date and the payment date.
   */
  private final ZonedDateTime[] _referenceEndDate;
  /**
   * The weight on the first month index in the interpolation of the index at the coupon start.
   */
  private final double _weightStart;
  /**
   * The weight on the first month index in the interpolation of the index at the coupon end.
   */
  private final double _weightEnd;
  /**
   * Flag indicating if the notional is paid (true) or not (false).
   */
  private final boolean _payNotional;
  /**
   * The lag in month between the index validity and the coupon dates.
   */
  private final int _monthLag;

  /**
   * Constructor for Year on Year inflation coupon.
   * @param currency The coupon currency.
   * @param paymentDate The payment date.
   * @param accrualStartDate Start date of the accrual period.
   * @param accrualEndDate End date of the accrual period.
   * @param paymentYearFraction Accrual factor of the accrual period.
   * @param notional Coupon notional.
   * @param priceIndex The price index associated to the coupon.
   * @param monthLag The lag in month between the index validity and the coupon dates.
   * @param referenceStartDate The reference date for the index at the coupon start.
   * @param referenceEndDate The reference date for the index at the coupon end.
   * @param weightStart The weight on the first month index in the interpolation of the index at the coupon start.
   * @param weightEnd The weight on the first month index in the interpolation of the index at the coupon end.
   * @param payNotional Flag indicating if the notional is paid (true) or not (false).
   */
  public CouponInflationYearOnYearInterpolationDefinition(final Currency currency, final ZonedDateTime paymentDate, final ZonedDateTime accrualStartDate,
      final ZonedDateTime accrualEndDate, final double paymentYearFraction, final double notional, final IndexPrice priceIndex, final ZonedDateTime[] referenceStartDate,
      final ZonedDateTime[] referenceEndDate, final double weightStart, final double weightEnd, final int monthLag, final boolean payNotional) {
    super(currency, paymentDate, accrualStartDate, accrualEndDate, paymentYearFraction, notional, priceIndex);
    ArgumentChecker.notNull(referenceStartDate, "Reference start date");
    ArgumentChecker.notNull(referenceEndDate, "Reference end date");
    this._referenceStartDate = referenceStartDate;
    this._referenceEndDate = referenceEndDate;
    _weightStart = weightStart;
    _weightEnd = weightEnd;
    _payNotional = payNotional;
    _monthLag = monthLag;
  }

  /**
   * Builder for inflation Year on Yearn based on an inflation lag and index publication. The fixing date is the publication lag after the last reference month.
   * @param accrualStartDate Start date of the accrual period.
   * @param paymentDate The payment date.
   * @param notional Coupon notional.
   * @param priceIndex The price index associated to the coupon.
   * @param weightStart The weight on the first month index in the interpolation of the index at the _referenceStartDate.
   * @param weightEnd The weight on the first month index in the interpolation of the index at the _referenceEndDate.
   * @param monthLag The lag in month between the index validity and the coupon dates.
   * @param payNotional Flag indicating if the notional is paid (true) or not (false).
   * @return The inflation zero-coupon.
   */
  public static CouponInflationYearOnYearInterpolationDefinition from(final ZonedDateTime accrualStartDate, final ZonedDateTime paymentDate, final double notional, final IndexPrice priceIndex,
      final double weightStart, final double weightEnd, final int monthLag, final boolean payNotional) {
    ZonedDateTime[] referenceStartDate = new ZonedDateTime[2];
    ZonedDateTime[] referenceEndDate = new ZonedDateTime[2];
    referenceStartDate[0] = accrualStartDate.minusMonths(monthLag);
    referenceStartDate[1] = referenceStartDate[0].plusMonths(1);
    referenceEndDate[0] = paymentDate.minusMonths(monthLag).withDayOfMonth(1);
    referenceEndDate[1] = referenceEndDate[0].plusMonths(1);

    return new CouponInflationYearOnYearInterpolationDefinition(priceIndex.getCurrency(), paymentDate, accrualStartDate, paymentDate, 1.0, notional, priceIndex, referenceStartDate,
        referenceEndDate, weightStart, weightEnd, monthLag, payNotional);
  }

  /**
   * Gets the reference date for the index at the coupon start.
   * @return The reference date for the index at the coupon start.
   */
  public ZonedDateTime[] getReferenceStartDate() {
    return _referenceStartDate;
  }

  /**
   * Gets the reference date for the index at the coupon end.
   * @return The reference date for the index at the coupon end.
   */
  public ZonedDateTime[] getReferenceEndDate() {
    return _referenceEndDate;
  }

  /**
   * Gets the weight on the first month index in the interpolation for the index at the coupon start.
   * @return The weight.
   */
  public double getWeightStart() {
    return _weightStart;
  }

  /**
   * Gets the weight on the first month index in the interpolation for the index at the coupon end.
   * @return The weight.
   */
  public double getWeightEnd() {
    return _weightEnd;
  }

  /**
   * Gets the pay notional flag.
   * @return The flag.
   */
  public boolean payNotional() {
    return _payNotional;
  }

  /**
   * Gets the lag in month between the index validity and the coupon dates.
   * @return The lag.
   */
  public int getMonthLag() {
    return _monthLag;
  }

  @Override
  public CouponInflationDefinition with(final ZonedDateTime paymentDate, final ZonedDateTime accrualStartDate, final ZonedDateTime accrualEndDate, final double notional) {
    final ZonedDateTime refInterpolatedDate = accrualEndDate.minusMonths(_monthLag);
    final ZonedDateTime[] referenceEndDate = new ZonedDateTime[2];
    referenceEndDate[0] = refInterpolatedDate.withDayOfMonth(1);
    referenceEndDate[1] = referenceEndDate[0].plusMonths(1);
    return new CouponInflationYearOnYearInterpolationDefinition(getCurrency(), paymentDate, accrualStartDate, accrualEndDate, getPaymentYearFraction(), getNotional(), getPriceIndex(),
        getReferenceStartDate(), referenceEndDate, getWeightStart(), getWeightEnd(), _monthLag, payNotional());
  }

  @Override
  public CouponInflationYearOnYearInterpolation toDerivative(ZonedDateTime date, String... yieldCurveNames) {
    ArgumentChecker.notNull(date, "date");
    ArgumentChecker.isTrue(!date.isAfter(getPaymentDate()), "Do not have any fixing data but are asking for a derivative after the payment date");
    ArgumentChecker.notNull(yieldCurveNames, "yield curve names");
    ArgumentChecker.isTrue(yieldCurveNames.length > 0, "at least one curve required");
    ArgumentChecker.isTrue(!date.isAfter(getPaymentDate()), "date is after payment date");
    final double paymentTime = TimeCalculator.getTimeBetween(date, getPaymentDate());
    final double[] referenceStartTime = new double[2];
    referenceStartTime[0] = TimeCalculator.getTimeBetween(date, getReferenceStartDate()[0]);
    referenceStartTime[1] = TimeCalculator.getTimeBetween(date, getReferenceStartDate()[1]);
    final double[] referenceEndTime = new double[2];
    referenceEndTime[0] = TimeCalculator.getTimeBetween(date, getReferenceEndDate()[0]);
    referenceEndTime[1] = TimeCalculator.getTimeBetween(date, getReferenceEndDate()[1]);

    final String discountingCurveName = yieldCurveNames[0];
    return new CouponInflationYearOnYearInterpolation(getCurrency(), paymentTime, discountingCurveName, getPaymentYearFraction(), getNotional(), getPriceIndex(), referenceStartTime, referenceEndTime,
        _weightStart, _weightStart, _payNotional);
  }

  @Override
  public Coupon toDerivative(ZonedDateTime date, DoubleTimeSeries<ZonedDateTime> data, String... yieldCurveNames) {
    ArgumentChecker.notNull(date, "date");
    ArgumentChecker.notNull(yieldCurveNames, "yield curve names");
    ArgumentChecker.isTrue(yieldCurveNames.length > 0, "at least one curve required");
    ArgumentChecker.isTrue(!date.isAfter(getPaymentDate()), "date is after payment date");
    final double paymentTime = TimeCalculator.getTimeBetween(date, getPaymentDate());
    final String discountingCurveName = yieldCurveNames[0];
    double[] referenceEndTime = new double[2];
    double[] referenceStartTime = new double[2];
    referenceEndTime[0] = TimeCalculator.getTimeBetween(date, _referenceEndDate[0]);
    referenceEndTime[1] = TimeCalculator.getTimeBetween(date, _referenceEndDate[1]);
    referenceStartTime[0] = TimeCalculator.getTimeBetween(date, _referenceStartDate[0]);
    referenceStartTime[1] = TimeCalculator.getTimeBetween(date, _referenceStartDate[1]);
    return new CouponInflationYearOnYearInterpolation(getCurrency(), paymentTime, discountingCurveName, getPaymentYearFraction(), getNotional(), getPriceIndex(), referenceStartTime, referenceEndTime,
        _weightStart, _weightStart, _payNotional);
  }

  @Override
  public <U, V> V accept(InstrumentDefinitionVisitor<U, V> visitor, U data) {
    ArgumentChecker.notNull(visitor, "visitor");
    return visitor.visitCouponInflationYearOnYearInterpolationDefinition(this, data);
  }

  @Override
  public <V> V accept(InstrumentDefinitionVisitor<?, V> visitor) {
    ArgumentChecker.notNull(visitor, "visitor");
    return visitor.visitCouponInflationYearOnYearInterpolationDefinition(this);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((_referenceEndDate == null) ? 0 : _referenceEndDate.hashCode());
    result = prime * result + ((_referenceStartDate == null) ? 0 : _referenceStartDate.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    CouponInflationYearOnYearInterpolationDefinition other = (CouponInflationYearOnYearInterpolationDefinition) obj;
    if (_referenceEndDate == null) {
      if (other._referenceEndDate != null) {
        return false;
      }
    } else if (!_referenceEndDate.equals(other._referenceEndDate)) {
      return false;
    }
    if (_referenceStartDate == null) {
      if (other._referenceStartDate != null) {
        return false;
      }
    } else if (!_referenceStartDate.equals(other._referenceStartDate)) {
      return false;
    }
    return true;
  }

}
