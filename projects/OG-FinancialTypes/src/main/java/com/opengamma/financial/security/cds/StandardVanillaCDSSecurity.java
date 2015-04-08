/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.cds;

import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.threeten.bp.ZonedDateTime;

import com.opengamma.analytics.financial.credit.DebtSeniority;
import com.opengamma.analytics.financial.credit.RestructuringClause;
import com.opengamma.financial.convention.StubType;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.frequency.Frequency;
import com.opengamma.financial.security.FinancialSecurityVisitor;
import com.opengamma.financial.security.swap.InterestRateNotional;
import com.opengamma.id.ExternalId;
import com.opengamma.master.security.SecurityDescription;

/**
 * @deprecated use com.opengamma.financial.security.credit.StandardCDSSecurity
 */
@BeanDefinition
@SecurityDescription(type = StandardVanillaCDSSecurity.SECURITY_TYPE, description = "Standard vanilla cds")
public class StandardVanillaCDSSecurity extends StandardCDSSecurity {

  /** Serialization version */
  private static final long serialVersionUID = 2L;

  /**
   * The security type
   */
  public static final String SECURITY_TYPE = "STANDARD_VANILLA_CDS";
  /**
   * The premium leg coupon in basis points.
   */
  @PropertyDefinition(validate = "notNull")
  private double _coupon;

  /**
   * The settlement date.
   */
  @PropertyDefinition(validate = "notNull")
  private ZonedDateTime _cashSettlementDate;

  /**
   * Whether to adjust the cash settlement date.
   */
  @PropertyDefinition(validate = "notNull")
  private boolean _adjustCashSettlementDate;

  StandardVanillaCDSSecurity() { // for fudge
    super(SECURITY_TYPE);
  }

  public StandardVanillaCDSSecurity(final boolean isBuy, final ExternalId protectionSeller, final ExternalId protectionBuyer, final ExternalId referenceEntity, //CSIGNORE
      final DebtSeniority debtSeniority, final RestructuringClause restructuringClause, final ExternalId regionId, final ZonedDateTime startDate,
      final ZonedDateTime effectiveDate, final ZonedDateTime maturityDate, final StubType stubType, final Frequency couponFrequency, final DayCount dayCount,
      final BusinessDayConvention businessDayConvention, final boolean immAdjustMaturityDate, final boolean adjustEffectiveDate,
      final boolean adjustMaturityDate, final InterestRateNotional notional, final boolean includeAccruedPremium,
      final boolean protectionStart, final double quotedSpread, final InterestRateNotional upfrontAmount, final double coupon,
      final ZonedDateTime cashSettlementDate, final boolean adjustCashSettlementDate) {
    super(isBuy, protectionSeller, protectionBuyer, referenceEntity, debtSeniority, restructuringClause, regionId, startDate, effectiveDate, maturityDate, stubType,
        couponFrequency, dayCount, businessDayConvention, immAdjustMaturityDate, adjustEffectiveDate, adjustMaturityDate, notional, includeAccruedPremium,
        protectionStart, quotedSpread, upfrontAmount, SECURITY_TYPE);
    setCoupon(coupon);
    setCashSettlementDate(cashSettlementDate);
    setAdjustCashSettlementDate(adjustCashSettlementDate);
  }

  @Override
  public final <T> T accept(final FinancialSecurityVisitor<T> visitor) {
    return visitor.visitStandardVanillaCDSSecurity(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code StandardVanillaCDSSecurity}.
   * @return the meta-bean, not null
   */
  public static StandardVanillaCDSSecurity.Meta meta() {
    return StandardVanillaCDSSecurity.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(StandardVanillaCDSSecurity.Meta.INSTANCE);
  }

  @Override
  public StandardVanillaCDSSecurity.Meta metaBean() {
    return StandardVanillaCDSSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the premium leg coupon in basis points.
   * @return the value of the property, not null
   */
  public double getCoupon() {
    return _coupon;
  }

  /**
   * Sets the premium leg coupon in basis points.
   * @param coupon  the new value of the property, not null
   */
  public void setCoupon(double coupon) {
    JodaBeanUtils.notNull(coupon, "coupon");
    this._coupon = coupon;
  }

  /**
   * Gets the the {@code coupon} property.
   * @return the property, not null
   */
  public final Property<Double> coupon() {
    return metaBean().coupon().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the settlement date.
   * @return the value of the property, not null
   */
  public ZonedDateTime getCashSettlementDate() {
    return _cashSettlementDate;
  }

  /**
   * Sets the settlement date.
   * @param cashSettlementDate  the new value of the property, not null
   */
  public void setCashSettlementDate(ZonedDateTime cashSettlementDate) {
    JodaBeanUtils.notNull(cashSettlementDate, "cashSettlementDate");
    this._cashSettlementDate = cashSettlementDate;
  }

  /**
   * Gets the the {@code cashSettlementDate} property.
   * @return the property, not null
   */
  public final Property<ZonedDateTime> cashSettlementDate() {
    return metaBean().cashSettlementDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets whether to adjust the cash settlement date.
   * @return the value of the property, not null
   */
  public boolean isAdjustCashSettlementDate() {
    return _adjustCashSettlementDate;
  }

  /**
   * Sets whether to adjust the cash settlement date.
   * @param adjustCashSettlementDate  the new value of the property, not null
   */
  public void setAdjustCashSettlementDate(boolean adjustCashSettlementDate) {
    JodaBeanUtils.notNull(adjustCashSettlementDate, "adjustCashSettlementDate");
    this._adjustCashSettlementDate = adjustCashSettlementDate;
  }

  /**
   * Gets the the {@code adjustCashSettlementDate} property.
   * @return the property, not null
   */
  public final Property<Boolean> adjustCashSettlementDate() {
    return metaBean().adjustCashSettlementDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public StandardVanillaCDSSecurity clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      StandardVanillaCDSSecurity other = (StandardVanillaCDSSecurity) obj;
      return JodaBeanUtils.equal(getCoupon(), other.getCoupon()) &&
          JodaBeanUtils.equal(getCashSettlementDate(), other.getCashSettlementDate()) &&
          (isAdjustCashSettlementDate() == other.isAdjustCashSettlementDate()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getCoupon());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCashSettlementDate());
    hash = hash * 31 + JodaBeanUtils.hashCode(isAdjustCashSettlementDate());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(128);
    buf.append("StandardVanillaCDSSecurity{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  @Override
  protected void toString(StringBuilder buf) {
    super.toString(buf);
    buf.append("coupon").append('=').append(JodaBeanUtils.toString(getCoupon())).append(',').append(' ');
    buf.append("cashSettlementDate").append('=').append(JodaBeanUtils.toString(getCashSettlementDate())).append(',').append(' ');
    buf.append("adjustCashSettlementDate").append('=').append(JodaBeanUtils.toString(isAdjustCashSettlementDate())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code StandardVanillaCDSSecurity}.
   */
  public static class Meta extends StandardCDSSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code coupon} property.
     */
    private final MetaProperty<Double> _coupon = DirectMetaProperty.ofReadWrite(
        this, "coupon", StandardVanillaCDSSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code cashSettlementDate} property.
     */
    private final MetaProperty<ZonedDateTime> _cashSettlementDate = DirectMetaProperty.ofReadWrite(
        this, "cashSettlementDate", StandardVanillaCDSSecurity.class, ZonedDateTime.class);
    /**
     * The meta-property for the {@code adjustCashSettlementDate} property.
     */
    private final MetaProperty<Boolean> _adjustCashSettlementDate = DirectMetaProperty.ofReadWrite(
        this, "adjustCashSettlementDate", StandardVanillaCDSSecurity.class, Boolean.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "coupon",
        "cashSettlementDate",
        "adjustCashSettlementDate");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -1354573786:  // coupon
          return _coupon;
        case 487875210:  // cashSettlementDate
          return _cashSettlementDate;
        case -1224855431:  // adjustCashSettlementDate
          return _adjustCashSettlementDate;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends StandardVanillaCDSSecurity> builder() {
      return new DirectBeanBuilder<StandardVanillaCDSSecurity>(new StandardVanillaCDSSecurity());
    }

    @Override
    public Class<? extends StandardVanillaCDSSecurity> beanType() {
      return StandardVanillaCDSSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code coupon} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> coupon() {
      return _coupon;
    }

    /**
     * The meta-property for the {@code cashSettlementDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTime> cashSettlementDate() {
      return _cashSettlementDate;
    }

    /**
     * The meta-property for the {@code adjustCashSettlementDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> adjustCashSettlementDate() {
      return _adjustCashSettlementDate;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -1354573786:  // coupon
          return ((StandardVanillaCDSSecurity) bean).getCoupon();
        case 487875210:  // cashSettlementDate
          return ((StandardVanillaCDSSecurity) bean).getCashSettlementDate();
        case -1224855431:  // adjustCashSettlementDate
          return ((StandardVanillaCDSSecurity) bean).isAdjustCashSettlementDate();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -1354573786:  // coupon
          ((StandardVanillaCDSSecurity) bean).setCoupon((Double) newValue);
          return;
        case 487875210:  // cashSettlementDate
          ((StandardVanillaCDSSecurity) bean).setCashSettlementDate((ZonedDateTime) newValue);
          return;
        case -1224855431:  // adjustCashSettlementDate
          ((StandardVanillaCDSSecurity) bean).setAdjustCashSettlementDate((Boolean) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((StandardVanillaCDSSecurity) bean)._coupon, "coupon");
      JodaBeanUtils.notNull(((StandardVanillaCDSSecurity) bean)._cashSettlementDate, "cashSettlementDate");
      JodaBeanUtils.notNull(((StandardVanillaCDSSecurity) bean)._adjustCashSettlementDate, "adjustCashSettlementDate");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
