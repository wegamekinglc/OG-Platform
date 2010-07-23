/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.position;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.time.Instant;

import org.joda.beans.BeanDefinition;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.BasicMetaBean;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectMetaProperty;

import com.opengamma.id.UniqueIdentifier;

/**
 * Request for getting a position with full detail.
 */
@BeanDefinition
public class FullPositionGetRequest extends DirectBean {

  /**
   * The position identifier.
   * Any identifier version specified will be overridden by the instants.
   */
  @PropertyDefinition
  private UniqueIdentifier _positionId;
  /**
   * The instant representing the version to obtain.
   * Null is treated as the latest version.
   */
  @PropertyDefinition
  private Instant _versionAsOfInstant;
  /**
   * The instant representing the version has been corrected to.
   * Null is treated as the latest correction.
   */
  @PropertyDefinition
  private Instant _correctedToInstant;

  /**
   * Creates an instance.
   */
  public FullPositionGetRequest() {
  }

  /**
   * Creates an instance.
   * @param uid  the portfolio node unique identifier
   */
  public FullPositionGetRequest(UniqueIdentifier uid) {
    setPositionId(uid);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code FullPositionGetRequest}.
   * @return the meta-bean, not null
   */
  public static FullPositionGetRequest.Meta meta() {
    return FullPositionGetRequest.Meta.INSTANCE;
  }

  @Override
  public FullPositionGetRequest.Meta metaBean() {
    return FullPositionGetRequest.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName) {
    switch (propertyName.hashCode()) {
      case 1381039140:  // positionId
        return getPositionId();
      case 598802432:  // versionAsOfInstant
        return getVersionAsOfInstant();
      case -28367267:  // correctedToInstant
        return getCorrectedToInstant();
    }
    return super.propertyGet(propertyName);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue) {
    switch (propertyName.hashCode()) {
      case 1381039140:  // positionId
        setPositionId((UniqueIdentifier) newValue);
        return;
      case 598802432:  // versionAsOfInstant
        setVersionAsOfInstant((Instant) newValue);
        return;
      case -28367267:  // correctedToInstant
        setCorrectedToInstant((Instant) newValue);
        return;
    }
    super.propertySet(propertyName, newValue);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the position identifier.
   * Any identifier version specified will be overridden by the instants.
   * @return the value of the property
   */
  public UniqueIdentifier getPositionId() {
    return _positionId;
  }

  /**
   * Sets the position identifier.
   * Any identifier version specified will be overridden by the instants.
   * @param positionId  the new value of the property
   */
  public void setPositionId(UniqueIdentifier positionId) {
    this._positionId = positionId;
  }

  /**
   * Gets the the {@code positionId} property.
   * Any identifier version specified will be overridden by the instants.
   * @return the property, not null
   */
  public final Property<UniqueIdentifier> positionId() {
    return metaBean().positionId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the instant representing the version to obtain.
   * Null is treated as the latest version.
   * @return the value of the property
   */
  public Instant getVersionAsOfInstant() {
    return _versionAsOfInstant;
  }

  /**
   * Sets the instant representing the version to obtain.
   * Null is treated as the latest version.
   * @param versionAsOfInstant  the new value of the property
   */
  public void setVersionAsOfInstant(Instant versionAsOfInstant) {
    this._versionAsOfInstant = versionAsOfInstant;
  }

  /**
   * Gets the the {@code versionAsOfInstant} property.
   * Null is treated as the latest version.
   * @return the property, not null
   */
  public final Property<Instant> versionAsOfInstant() {
    return metaBean().versionAsOfInstant().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the instant representing the version has been corrected to.
   * Null is treated as the latest correction.
   * @return the value of the property
   */
  public Instant getCorrectedToInstant() {
    return _correctedToInstant;
  }

  /**
   * Sets the instant representing the version has been corrected to.
   * Null is treated as the latest correction.
   * @param correctedToInstant  the new value of the property
   */
  public void setCorrectedToInstant(Instant correctedToInstant) {
    this._correctedToInstant = correctedToInstant;
  }

  /**
   * Gets the the {@code correctedToInstant} property.
   * Null is treated as the latest correction.
   * @return the property, not null
   */
  public final Property<Instant> correctedToInstant() {
    return metaBean().correctedToInstant().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code FullPositionGetRequest}.
   */
  public static class Meta extends BasicMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code positionId} property.
     */
    private final MetaProperty<UniqueIdentifier> _positionId = DirectMetaProperty.ofReadWrite(this, "positionId", UniqueIdentifier.class);
    /**
     * The meta-property for the {@code versionAsOfInstant} property.
     */
    private final MetaProperty<Instant> _versionAsOfInstant = DirectMetaProperty.ofReadWrite(this, "versionAsOfInstant", Instant.class);
    /**
     * The meta-property for the {@code correctedToInstant} property.
     */
    private final MetaProperty<Instant> _correctedToInstant = DirectMetaProperty.ofReadWrite(this, "correctedToInstant", Instant.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map;

    @SuppressWarnings("unchecked")
    protected Meta() {
      LinkedHashMap temp = new LinkedHashMap();
      temp.put("positionId", _positionId);
      temp.put("versionAsOfInstant", _versionAsOfInstant);
      temp.put("correctedToInstant", _correctedToInstant);
      _map = Collections.unmodifiableMap(temp);
    }

    @Override
    public FullPositionGetRequest createBean() {
      return new FullPositionGetRequest();
    }

    @Override
    public Class<? extends FullPositionGetRequest> beanType() {
      return FullPositionGetRequest.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code positionId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<UniqueIdentifier> positionId() {
      return _positionId;
    }

    /**
     * The meta-property for the {@code versionAsOfInstant} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Instant> versionAsOfInstant() {
      return _versionAsOfInstant;
    }

    /**
     * The meta-property for the {@code correctedToInstant} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Instant> correctedToInstant() {
      return _correctedToInstant;
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
