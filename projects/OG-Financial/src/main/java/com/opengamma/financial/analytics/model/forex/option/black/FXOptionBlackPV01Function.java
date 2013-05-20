/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.forex.option.black;

import static com.opengamma.engine.value.ValuePropertyNames.SURFACE;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.analytics.financial.forex.calculator.PV01ForexCalculator;
import com.opengamma.analytics.financial.forex.method.MultipleCurrencyInterestRateCurveSensitivity;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.model.option.definition.ForexOptionDataBundle;
import com.opengamma.core.config.ConfigSource;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionExecutionContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValuePropertyNames;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.financial.OpenGammaCompilationContext;
import com.opengamma.financial.analytics.ircurve.calcconfig.ConfigDBCurveCalculationConfigSource;
import com.opengamma.financial.analytics.ircurve.calcconfig.MultiCurveCalculationConfig;
import com.opengamma.financial.analytics.model.CalculationPropertyNamesAndValues;
import com.opengamma.financial.analytics.model.InterpolatedDataProperties;
import com.opengamma.financial.analytics.model.forex.ForexVisitors;
import com.opengamma.financial.currency.CurrencyPair;
import com.opengamma.financial.security.FinancialSecurity;
import com.opengamma.util.money.Currency;
import com.opengamma.util.tuple.DoublesPair;

/**
 *
 */
public class FXOptionBlackPV01Function extends FXOptionBlackSingleValuedFunction {
  private static final Logger s_logger = LoggerFactory.getLogger(FXOptionBlackPV01Function.class);
  private static final PV01ForexCalculator CALCULATOR = PV01ForexCalculator.getInstance();

  public FXOptionBlackPV01Function() {
    super(ValueRequirementNames.PV01);
  }

  @Override
  protected Set<ComputedValue> getResult(final InstrumentDerivative forex, final ForexOptionDataBundle<?> data, final ComputationTarget target,
      final Set<ValueRequirement> desiredValues, final FunctionInputs inputs, final ValueSpecification spec, final FunctionExecutionContext executionContext) {
    final ValueRequirement desiredValue = Iterables.getOnlyElement(desiredValues);
    final String currency = desiredValue.getConstraint(ValuePropertyNames.CURVE_CURRENCY);
    final String curveName = desiredValue.getConstraint(ValuePropertyNames.CURVE);
    final String resultCurrency = desiredValue.getConstraint(ValuePropertyNames.CURRENCY);
    final Object curveSensitivitiesObject = inputs.getValue(ValueRequirementNames.FX_CURVE_SENSITIVITIES);
    if (curveSensitivitiesObject == null) {
      throw new OpenGammaRuntimeException("Could not get curve sensitivities");
    }
    final String fullCurveName = curveName + "_" + currency;
    final MultipleCurrencyInterestRateCurveSensitivity curveSensitivities = (MultipleCurrencyInterestRateCurveSensitivity) curveSensitivitiesObject;
    final Map<String, List<DoublesPair>> sensitivitiesForCurrency = curveSensitivities.getSensitivity(Currency.of(resultCurrency)).getSensitivities();
    final Map<String, Double> pv01 = forex.accept(CALCULATOR, sensitivitiesForCurrency);
    if (!pv01.containsKey(fullCurveName)) {
      throw new OpenGammaRuntimeException("Could not get PV01 for " + fullCurveName);
    }
    return Collections.singleton(new ComputedValue(spec, pv01.get(fullCurveName)));
  }

  @Override
  public Set<ValueRequirement> getRequirements(final FunctionCompilationContext context, final ComputationTarget target, final ValueRequirement desiredValue) {
    final Set<ValueRequirement> requirements = super.getRequirements(context, target, desiredValue);
    if (requirements == null) {
      return null;
    }
    final ValueProperties constraints = desiredValue.getConstraints();
    final Set<String> curveNames = constraints.getValues(ValuePropertyNames.CURVE);
    if (curveNames == null || curveNames.size() != 1) {
      s_logger.error("Did not specify a curve name for requirement {}", desiredValue);
      return null;
    }
    final Set<String> currencies = constraints.getValues(ValuePropertyNames.CURVE_CURRENCY);
    if (currencies == null || currencies.size() != 1) {
      s_logger.error("Did not specify a curve currency for requirement {}", desiredValue);
      return null;
    }
    final String callCurveName = Iterables.getOnlyElement(constraints.getValues(CALL_CURVE));
    final String putCurveName = Iterables.getOnlyElement(constraints.getValues(PUT_CURVE));
    final String curveName = Iterables.getOnlyElement(curveNames);
    if (!(curveName.equals(putCurveName) || curveName.equals(callCurveName))) {
      s_logger.info("Curve name {} did not match either put curve name {} or call curve name {}", new Object[] {curveName, putCurveName, callCurveName });
      return null;
    }
    final String callCurveCalculationConfigName = Iterables.getOnlyElement(constraints.getValues(CALL_CURVE_CALC_CONFIG));
    final String putCurveCalculationConfigName = Iterables.getOnlyElement(constraints.getValues(PUT_CURVE_CALC_CONFIG));
    final String surfaceName = Iterables.getOnlyElement(constraints.getValues(SURFACE));
    final String interpolatorName = Iterables.getOnlyElement(constraints.getValues(InterpolatedDataProperties.X_INTERPOLATOR_NAME));
    final String leftExtrapolatorName = Iterables.getOnlyElement(constraints.getValues(InterpolatedDataProperties.LEFT_X_EXTRAPOLATOR_NAME));
    final String rightExtrapolatorName = Iterables.getOnlyElement(constraints.getValues(InterpolatedDataProperties.RIGHT_X_EXTRAPOLATOR_NAME));
    final String currency = Iterables.getOnlyElement(currencies);
    final FinancialSecurity security = (FinancialSecurity) target.getSecurity();
    final Currency putCurrency = security.accept(ForexVisitors.getPutCurrencyVisitor());
    final Currency callCurrency = security.accept(ForexVisitors.getCallCurrencyVisitor());
    final String resultCurrency, resultCurveName, resultCurveConfigName;
    if (currency.equals(putCurrency.getCode())) {
      resultCurrency = putCurrency.getCode();
      resultCurveName = putCurveName;
      resultCurveConfigName = putCurveCalculationConfigName;
    } else if (currency.equals(callCurrency.getCode())) {
      resultCurrency = callCurrency.getCode();
      resultCurveName = callCurveName;
      resultCurveConfigName = callCurveCalculationConfigName;
    } else {
      return null;
    }
    final ConfigSource configSource = OpenGammaCompilationContext.getConfigSource(context);
    final ConfigDBCurveCalculationConfigSource curveCalculationConfigSource = new ConfigDBCurveCalculationConfigSource(configSource);
    final MultiCurveCalculationConfig resultCurveCalculationConfig = curveCalculationConfigSource.getConfig(resultCurveConfigName);
    if (resultCurveCalculationConfig == null) {
      s_logger.error("Could not find curve calculation configuration named " + resultCurveConfigName + " for currency " + resultCurrency);
      return null;
    }
    requirements.add(getCurveSensitivitiesRequirement(putCurveName, putCurveCalculationConfigName, callCurveName, callCurveCalculationConfigName, surfaceName,
        interpolatorName, leftExtrapolatorName, rightExtrapolatorName, currency, resultCurrency, resultCurveName, target));
    return requirements;
  }

  @Override
  protected ValueProperties.Builder getResultProperties(final ComputationTarget target) {
    final ValueProperties.Builder properties = super.getResultProperties(target)
        .withAny(ValuePropertyNames.CURVE_CURRENCY)
        .withAny(ValuePropertyNames.CURVE);
    return properties;
  }

  @Override
  protected ValueProperties.Builder getResultProperties(final ComputationTarget target, final String putCurve, final String putCurveCalculationConfig,
      final String callCurve, final String callCurveCalculationConfig, final CurrencyPair baseQuotePair) {
    final ValueProperties.Builder properties = super.getResultProperties(target, putCurve, putCurveCalculationConfig, callCurve, callCurveCalculationConfig, baseQuotePair)
        .withAny(ValuePropertyNames.CURVE_CURRENCY)
        .withAny(ValuePropertyNames.CURVE);
    return properties;
  }

  @Override
  protected ValueProperties.Builder getResultProperties(final ComputationTarget target, final ValueRequirement desiredValue) {
    final String curveName = desiredValue.getConstraint(ValuePropertyNames.CURVE);
    final String currency = desiredValue.getConstraint(ValuePropertyNames.CURVE_CURRENCY);
    final ValueProperties.Builder properties = super.getResultProperties(target, desiredValue)
        .with(ValuePropertyNames.CURVE_CURRENCY, currency)
        .with(ValuePropertyNames.CURVE, curveName);
    return properties;
  }

  private static ValueRequirement getCurveSensitivitiesRequirement(final String putCurveName, final String putCurveCalculationConfig, final String callCurveName,
      final String callCurveCalculationConfig, final String surfaceName, final String interpolatorName, final String leftExtrapolatorName,
      final String rightExtrapolatorName, final String currency, final String resultCurrency, final String resultCurveName, final ComputationTarget target) {
    final ValueProperties properties = ValueProperties.builder()
        .with(PUT_CURVE, putCurveName)
        .with(CALL_CURVE, callCurveName)
        .with(PUT_CURVE_CALC_CONFIG, putCurveCalculationConfig)
        .with(CALL_CURVE_CALC_CONFIG, callCurveCalculationConfig)
        .with(ValuePropertyNames.SURFACE, surfaceName)
        .with(ValuePropertyNames.CALCULATION_METHOD, CalculationPropertyNamesAndValues.BLACK_METHOD)
        .with(InterpolatedDataProperties.X_INTERPOLATOR_NAME, interpolatorName)
        .with(InterpolatedDataProperties.LEFT_X_EXTRAPOLATOR_NAME, leftExtrapolatorName)
        .with(InterpolatedDataProperties.RIGHT_X_EXTRAPOLATOR_NAME, rightExtrapolatorName)
        .get();
    return new ValueRequirement(ValueRequirementNames.FX_CURVE_SENSITIVITIES, target.toSpecification(), properties);
  }
}
