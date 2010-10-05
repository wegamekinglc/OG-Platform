/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.interestrate.cash.definition;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.Validate;

import com.opengamma.financial.interestrate.InterestRateDerivativeVisitor;
import com.opengamma.financial.interestrate.InterestRateDerivativeWithRate;
import com.opengamma.util.ArgumentChecker;

/**
 * A cash loan with a unit amount borrowed on some some trade date (which could be now), and an amount (1+r*t) paid at maturity, where r is the Libor rate and t is the time (in years) 
 * between the trade date and the maturity in some day count convention.  
 */
public class Cash implements InterestRateDerivativeWithRate {
  private final double _tradeTime;
  private final double _maturity;
  private final double _yearFraction;
  private final double _rate;
  private final String _curveName;

  /**
   * A cash loan
   * @param maturity Time from now (in years) when the loan matures (is repaid)
   * @param rate The loan rate (continuously compounded)
   * @param yieldCurveName Name of yield curve used to price loan
   */
  public Cash(final double maturity, final double rate, final String yieldCurveName) {
    checkInputs(maturity, rate, yieldCurveName);
    _maturity = maturity;
    _curveName = yieldCurveName;
    _tradeTime = 0.0;
    _yearFraction = maturity;
    _rate = rate;
  }

  /**
   * A cash loan
   * @param maturity Time from now (in years) when the loan matures (is repaid)
   * @param rate Time from now (in years) when the loan matures (is repaid)
   * @param tradeTime Time when the notional amount is borrowed (could be 0, i.e. now)
   * @param yearFraction time (in years) between the trade date and the maturity in some day count convention.
   * @param yieldCurveName Name of yield curve used to price loan
   */
  public Cash(final double maturity, final double rate, final double tradeTime, final double yearFraction, final String yieldCurveName) {
    checkInputs(maturity, rate, yieldCurveName);
    ArgumentChecker.notNegative(tradeTime, "trade time");
    ArgumentChecker.notNegative(yearFraction, "year fraction");
    Validate.isTrue(tradeTime < maturity, "Trade time must be less than payment time");
    _maturity = maturity;
    _curveName = yieldCurveName;
    _tradeTime = tradeTime;
    _yearFraction = yearFraction;
    _rate = rate;

  }

  private void checkInputs(final double maturity, final double rate, final String yieldCurveName) {
    ArgumentChecker.notNegative(maturity, "payment time");
    ArgumentChecker.notNegative(rate, "rate");
    Validate.notNull(yieldCurveName);
  }

  public double getMaturity() {
    return _maturity;
  }

  public String getYieldCurveName() {
    return _curveName;
  }

  public double getTradeTime() {
    return _tradeTime;
  }

  public double getYearFraction() {
    return _yearFraction;
  }

  public double getRate() {
    return _rate;
  }

  @Override
  public <S, T> T accept(final InterestRateDerivativeVisitor<S, T> visitor, final S data) {
    return visitor.visitCash(this, data);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_curveName == null) ? 0 : _curveName.hashCode());
    long temp;
    temp = Double.doubleToLongBits(_maturity);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(_rate);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(_tradeTime);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(_yearFraction);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Cash other = (Cash) obj;
    if (!ObjectUtils.equals(_curveName, other._curveName)) {
      return false;
    }
    if (Double.doubleToLongBits(_maturity) != Double.doubleToLongBits(other._maturity)) {
      return false;
    }
    if (Double.doubleToLongBits(_rate) != Double.doubleToLongBits(other._rate)) {
      return false;
    }
    if (Double.doubleToLongBits(_tradeTime) != Double.doubleToLongBits(other._tradeTime)) {
      return false;
    }
    if (Double.doubleToLongBits(_yearFraction) != Double.doubleToLongBits(other._yearFraction)) {
      return false;
    }
    return true;
  }

  @Override
  public Cash withRate(double rate) {
    return new Cash(getMaturity(), rate, getTradeTime(), getYearFraction(), getYieldCurveName());
  }
}
