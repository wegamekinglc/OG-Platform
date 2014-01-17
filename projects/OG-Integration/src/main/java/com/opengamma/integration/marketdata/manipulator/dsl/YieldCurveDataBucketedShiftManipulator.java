/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.marketdata.manipulator.dsl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.ImmutableList;
import com.opengamma.analytics.ShiftType;
import com.opengamma.analytics.financial.model.interestrate.curve.YieldCurve;
import com.opengamma.engine.marketdata.manipulator.function.StructureManipulator;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.financial.analytics.ircurve.FixedIncomeStripWithSecurity;
import com.opengamma.financial.analytics.ircurve.InterpolatedYieldCurveSpecificationWithSecurities;
import com.opengamma.financial.analytics.ircurve.YieldCurveData;
import com.opengamma.util.tuple.DoublesPair;

/**
 * A {@link StructureManipulator} which performs a list of bucketed shifts on a {@link YieldCurve}.
 */
@BeanDefinition
public final class YieldCurveDataBucketedShiftManipulator implements ImmutableBean, StructureManipulator<YieldCurveData> {

  /**
   * Shifts to apply
   */
  @PropertyDefinition
  private final ImmutableList<YieldCurveBucketedShift> _shifts;

  /**
   * Creates a new YieldCurveBucketedShifts object
   * @param shifts the list of shifts
   * @return a new YieldCurveBucketedShifts object
   */
  public static YieldCurveDataBucketedShiftManipulator create(ImmutableList<YieldCurveBucketedShift> shifts) {
    return new YieldCurveDataBucketedShiftManipulator(shifts);
  }


  @Override
  public YieldCurveData execute(YieldCurveData curveData, ValueSpecification valueSpec) {
    final List<DoublesPair> buckets = new ArrayList<>();
    final List<Double> shifts = new ArrayList<>();
    ShiftType shiftType = null;
    for (YieldCurveBucketedShift bucketedShift : _shifts) {
      buckets.add(DoublesPair.of(bucketedShift.getStartYears(), bucketedShift.getEndYears()));
      shifts.add(bucketedShift.getShift());
      if (shiftType == null) {
        shiftType = bucketedShift.getCurveShiftType().toAnalyticsType();
      }
    }
    InterpolatedYieldCurveSpecificationWithSecurities curveSpec = curveData.getCurveSpecification();
    for (FixedIncomeStripWithSecurity strip : curveSpec.getStrips()) {

    }
    throw new UnsupportedOperationException();
  }

  @Override
  public Class<YieldCurveData> getExpectedType() {
    return YieldCurveData.class;
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code YieldCurveDataBucketedShiftManipulator}.
   * @return the meta-bean, not null
   */
  public static YieldCurveDataBucketedShiftManipulator.Meta meta() {
    return YieldCurveDataBucketedShiftManipulator.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(YieldCurveDataBucketedShiftManipulator.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static YieldCurveDataBucketedShiftManipulator.Builder builder() {
    return new YieldCurveDataBucketedShiftManipulator.Builder();
  }

  private YieldCurveDataBucketedShiftManipulator(
      List<YieldCurveBucketedShift> shifts) {
    this._shifts = (shifts != null ? ImmutableList.copyOf(shifts) : null);
  }

  @Override
  public YieldCurveDataBucketedShiftManipulator.Meta metaBean() {
    return YieldCurveDataBucketedShiftManipulator.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets shifts to apply
   * @return the value of the property
   */
  public ImmutableList<YieldCurveBucketedShift> getShifts() {
    return _shifts;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public YieldCurveDataBucketedShiftManipulator clone() {
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      YieldCurveDataBucketedShiftManipulator other = (YieldCurveDataBucketedShiftManipulator) obj;
      return JodaBeanUtils.equal(getShifts(), other.getShifts());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getShifts());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("YieldCurveDataBucketedShiftManipulator{");
    buf.append("shifts").append('=').append(JodaBeanUtils.toString(getShifts()));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code YieldCurveDataBucketedShiftManipulator}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code shifts} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<ImmutableList<YieldCurveBucketedShift>> _shifts = DirectMetaProperty.ofImmutable(
        this, "shifts", YieldCurveDataBucketedShiftManipulator.class, (Class) ImmutableList.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "shifts");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -903338959:  // shifts
          return _shifts;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public YieldCurveDataBucketedShiftManipulator.Builder builder() {
      return new YieldCurveDataBucketedShiftManipulator.Builder();
    }

    @Override
    public Class<? extends YieldCurveDataBucketedShiftManipulator> beanType() {
      return YieldCurveDataBucketedShiftManipulator.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code shifts} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ImmutableList<YieldCurveBucketedShift>> shifts() {
      return _shifts;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -903338959:  // shifts
          return ((YieldCurveDataBucketedShiftManipulator) bean).getShifts();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code YieldCurveDataBucketedShiftManipulator}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<YieldCurveDataBucketedShiftManipulator> {

    private List<YieldCurveBucketedShift> _shifts;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(YieldCurveDataBucketedShiftManipulator beanToCopy) {
      this._shifts = (beanToCopy.getShifts() != null ? new ArrayList<YieldCurveBucketedShift>(beanToCopy.getShifts()) : null);
    }

    //-----------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case -903338959:  // shifts
          this._shifts = (List<YieldCurveBucketedShift>) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder setString(MetaProperty<?> property, String value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public YieldCurveDataBucketedShiftManipulator build() {
      return new YieldCurveDataBucketedShiftManipulator(
          _shifts);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the {@code shifts} property in the builder.
     * @param shifts  the new value
     * @return this, for chaining, not null
     */
    public Builder shifts(List<YieldCurveBucketedShift> shifts) {
      this._shifts = shifts;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(64);
      buf.append("YieldCurveDataBucketedShiftManipulator.Builder{");
      buf.append("shifts").append('=').append(JodaBeanUtils.toString(_shifts));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}