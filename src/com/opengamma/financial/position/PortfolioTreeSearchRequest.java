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

import com.opengamma.util.db.PagingRequest;

/**
 * Request for searching for portfolio trees.
 */
@BeanDefinition
public class PortfolioTreeSearchRequest extends DirectBean {

  /**
   * The request for paging.
   * By default all matching items will be returned.
   */
  @PropertyDefinition
  private PagingRequest _pagingRequest = PagingRequest.ALL;
  /**
   * The portfolio name, wildcards allowed, null to not match on name.
   */
  @PropertyDefinition
  private String _name;
  /**
   * The instant to search for a version at.
   * Null is treated as the latest version.
   */
  @PropertyDefinition
  private Instant _versionAsOfInstant;
  /**
   * The instant to search for corrections for.
   * Null is treated as the latest correction.
   */
  @PropertyDefinition
  private Instant _correctedToInstant;
  /**
   * The depth of nodes to return.
   * A value of zero returns no nodes, one returns the root node, two returns the
   * root node and immediate children, and so on.
   * By default this is zero to save space in the response.
   */
  @PropertyDefinition
  private int _depth;

  /**
   * Creates an instance.
   */
  public PortfolioTreeSearchRequest() {
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code PortfolioTreeSearchRequest}.
   * @return the meta-bean, not null
   */
  public static PortfolioTreeSearchRequest.Meta meta() {
    return PortfolioTreeSearchRequest.Meta.INSTANCE;
  }

  @Override
  public PortfolioTreeSearchRequest.Meta metaBean() {
    return PortfolioTreeSearchRequest.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName) {
    switch (propertyName.hashCode()) {
      case -2092032669:  // pagingRequest
        return getPagingRequest();
      case 3373707:  // name
        return getName();
      case 598802432:  // versionAsOfInstant
        return getVersionAsOfInstant();
      case -28367267:  // correctedToInstant
        return getCorrectedToInstant();
      case 95472323:  // depth
        return getDepth();
    }
    return super.propertyGet(propertyName);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue) {
    switch (propertyName.hashCode()) {
      case -2092032669:  // pagingRequest
        setPagingRequest((PagingRequest) newValue);
        return;
      case 3373707:  // name
        setName((String) newValue);
        return;
      case 598802432:  // versionAsOfInstant
        setVersionAsOfInstant((Instant) newValue);
        return;
      case -28367267:  // correctedToInstant
        setCorrectedToInstant((Instant) newValue);
        return;
      case 95472323:  // depth
        setDepth((int) (Integer) newValue);
        return;
    }
    super.propertySet(propertyName, newValue);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the request for paging.
   * By default all matching items will be returned.
   * @return the value of the property
   */
  public PagingRequest getPagingRequest() {
    return _pagingRequest;
  }

  /**
   * Sets the request for paging.
   * By default all matching items will be returned.
   * @param pagingRequest  the new value of the property
   */
  public void setPagingRequest(PagingRequest pagingRequest) {
    this._pagingRequest = pagingRequest;
  }

  /**
   * Gets the the {@code pagingRequest} property.
   * By default all matching items will be returned.
   * @return the property, not null
   */
  public final Property<PagingRequest> pagingRequest() {
    return metaBean().pagingRequest().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the portfolio name, wildcards allowed, null to not match on name.
   * @return the value of the property
   */
  public String getName() {
    return _name;
  }

  /**
   * Sets the portfolio name, wildcards allowed, null to not match on name.
   * @param name  the new value of the property
   */
  public void setName(String name) {
    this._name = name;
  }

  /**
   * Gets the the {@code name} property.
   * @return the property, not null
   */
  public final Property<String> name() {
    return metaBean().name().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the instant to search for a version at.
   * Null is treated as the latest version.
   * @return the value of the property
   */
  public Instant getVersionAsOfInstant() {
    return _versionAsOfInstant;
  }

  /**
   * Sets the instant to search for a version at.
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
   * Gets the instant to search for corrections for.
   * Null is treated as the latest correction.
   * @return the value of the property
   */
  public Instant getCorrectedToInstant() {
    return _correctedToInstant;
  }

  /**
   * Sets the instant to search for corrections for.
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
   * Gets the depth of nodes to return.
   * A value of zero returns no nodes, one returns the root node, two returns the
   * root node and immediate children, and so on.
   * By default this is zero to save space in the response.
   * @return the value of the property
   */
  public int getDepth() {
    return _depth;
  }

  /**
   * Sets the depth of nodes to return.
   * A value of zero returns no nodes, one returns the root node, two returns the
   * root node and immediate children, and so on.
   * By default this is zero to save space in the response.
   * @param depth  the new value of the property
   */
  public void setDepth(int depth) {
    this._depth = depth;
  }

  /**
   * Gets the the {@code depth} property.
   * A value of zero returns no nodes, one returns the root node, two returns the
   * root node and immediate children, and so on.
   * By default this is zero to save space in the response.
   * @return the property, not null
   */
  public final Property<Integer> depth() {
    return metaBean().depth().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code PortfolioTreeSearchRequest}.
   */
  public static class Meta extends BasicMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code pagingRequest} property.
     */
    private final MetaProperty<PagingRequest> _pagingRequest = DirectMetaProperty.ofReadWrite(this, "pagingRequest", PagingRequest.class);
    /**
     * The meta-property for the {@code name} property.
     */
    private final MetaProperty<String> _name = DirectMetaProperty.ofReadWrite(this, "name", String.class);
    /**
     * The meta-property for the {@code versionAsOfInstant} property.
     */
    private final MetaProperty<Instant> _versionAsOfInstant = DirectMetaProperty.ofReadWrite(this, "versionAsOfInstant", Instant.class);
    /**
     * The meta-property for the {@code correctedToInstant} property.
     */
    private final MetaProperty<Instant> _correctedToInstant = DirectMetaProperty.ofReadWrite(this, "correctedToInstant", Instant.class);
    /**
     * The meta-property for the {@code depth} property.
     */
    private final MetaProperty<Integer> _depth = DirectMetaProperty.ofReadWrite(this, "depth", Integer.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map;

    @SuppressWarnings("unchecked")
    protected Meta() {
      LinkedHashMap temp = new LinkedHashMap();
      temp.put("pagingRequest", _pagingRequest);
      temp.put("name", _name);
      temp.put("versionAsOfInstant", _versionAsOfInstant);
      temp.put("correctedToInstant", _correctedToInstant);
      temp.put("depth", _depth);
      _map = Collections.unmodifiableMap(temp);
    }

    @Override
    public PortfolioTreeSearchRequest createBean() {
      return new PortfolioTreeSearchRequest();
    }

    @Override
    public Class<? extends PortfolioTreeSearchRequest> beanType() {
      return PortfolioTreeSearchRequest.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code pagingRequest} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<PagingRequest> pagingRequest() {
      return _pagingRequest;
    }

    /**
     * The meta-property for the {@code name} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> name() {
      return _name;
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

    /**
     * The meta-property for the {@code depth} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Integer> depth() {
      return _depth;
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
