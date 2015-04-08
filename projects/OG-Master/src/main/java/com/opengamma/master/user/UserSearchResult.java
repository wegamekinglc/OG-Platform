/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.paging.Paging;

/**
 * Result from searching for users.
 * <p>
 * The returned documents will match the search criteria.
 * See {@link UserSearchRequest} for more details.
 */
@BeanDefinition
public class UserSearchResult implements Bean {

  /**
   * The paging information, not null if correctly created.
   */
  @PropertyDefinition
  private Paging _paging;
  /**
   * The users that matched the search.
   */
  @PropertyDefinition
  private final List<ManageableUser> _users = new ArrayList<>();

  /**
   * Creates an instance.
   */
  protected UserSearchResult() {
  }

  /**
   * Creates an instance from a collection of users.
   * 
   * @param paging  the paging information, not null
   * @param users  the collection of users to add, not null
   */
  public UserSearchResult(Paging paging, Collection<ManageableUser> users) {
    _paging = ArgumentChecker.notNull(paging, "paging");
    _users.addAll(ArgumentChecker.notNull(users, "users"));
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code UserSearchResult}.
   * @return the meta-bean, not null
   */
  public static UserSearchResult.Meta meta() {
    return UserSearchResult.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(UserSearchResult.Meta.INSTANCE);
  }

  @Override
  public UserSearchResult.Meta metaBean() {
    return UserSearchResult.Meta.INSTANCE;
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
   * Gets the paging information, not null if correctly created.
   * @return the value of the property
   */
  public Paging getPaging() {
    return _paging;
  }

  /**
   * Sets the paging information, not null if correctly created.
   * @param paging  the new value of the property
   */
  public void setPaging(Paging paging) {
    this._paging = paging;
  }

  /**
   * Gets the the {@code paging} property.
   * @return the property, not null
   */
  public final Property<Paging> paging() {
    return metaBean().paging().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the users that matched the search.
   * @return the value of the property, not null
   */
  public List<ManageableUser> getUsers() {
    return _users;
  }

  /**
   * Sets the users that matched the search.
   * @param users  the new value of the property, not null
   */
  public void setUsers(List<ManageableUser> users) {
    JodaBeanUtils.notNull(users, "users");
    this._users.clear();
    this._users.addAll(users);
  }

  /**
   * Gets the the {@code users} property.
   * @return the property, not null
   */
  public final Property<List<ManageableUser>> users() {
    return metaBean().users().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public UserSearchResult clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      UserSearchResult other = (UserSearchResult) obj;
      return JodaBeanUtils.equal(getPaging(), other.getPaging()) &&
          JodaBeanUtils.equal(getUsers(), other.getUsers());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getPaging());
    hash = hash * 31 + JodaBeanUtils.hashCode(getUsers());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("UserSearchResult{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("paging").append('=').append(JodaBeanUtils.toString(getPaging())).append(',').append(' ');
    buf.append("users").append('=').append(JodaBeanUtils.toString(getUsers())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code UserSearchResult}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code paging} property.
     */
    private final MetaProperty<Paging> _paging = DirectMetaProperty.ofReadWrite(
        this, "paging", UserSearchResult.class, Paging.class);
    /**
     * The meta-property for the {@code users} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<ManageableUser>> _users = DirectMetaProperty.ofReadWrite(
        this, "users", UserSearchResult.class, (Class) List.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "paging",
        "users");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -995747956:  // paging
          return _paging;
        case 111578632:  // users
          return _users;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends UserSearchResult> builder() {
      return new DirectBeanBuilder<UserSearchResult>(new UserSearchResult());
    }

    @Override
    public Class<? extends UserSearchResult> beanType() {
      return UserSearchResult.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code paging} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Paging> paging() {
      return _paging;
    }

    /**
     * The meta-property for the {@code users} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<ManageableUser>> users() {
      return _users;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -995747956:  // paging
          return ((UserSearchResult) bean).getPaging();
        case 111578632:  // users
          return ((UserSearchResult) bean).getUsers();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -995747956:  // paging
          ((UserSearchResult) bean).setPaging((Paging) newValue);
          return;
        case 111578632:  // users
          ((UserSearchResult) bean).setUsers((List<ManageableUser>) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((UserSearchResult) bean)._users, "users");
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
