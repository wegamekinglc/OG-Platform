Worked-out example: STIR futures option - normal model with simple moneyness
==========================================

The code of this example can be found in the test: **STIRFuturesOptionNormalExpSimpleMoneynessMethodE2ETest**.  
Other examples are available in **STIRFuturesOptionNormalExpSimpleMoneynessGBPE2ETest** focusing on GBP-dominated trades and specific outputs.  
Our discussion in this document follows the former test, especially the test included in the public method **testGBP**, but is transferable to the other tests.

Curves
------

See the documentation, worked-out-examples/worked-out-example-stir-futures-discounting.rst.

Parameter provider
------------------

Given a volatility surface in the form of **InterpolatedDoublesSurface** where the volatility surface is parametrised by option's expiry and simple moneyness, the parameter provider for option valuation based on normal model is created by::

    NormalSTIRFuturesExpSimpleMoneynessProviderDiscount NORMAL_MULTICURVES = 
      new NormalSTIRFuturesExpSimpleMoneynessProviderDiscount(MULTICURVES, VOL_SURFACE_SIMPLEMONEY, GBPLIBOR3M, false);

Here the simple moneyness is expressed in terms of rates for **false** input, i.e., strike rate minus the underlying future rate. 
The rates are related to the prices via rate = 1 - strike.


Instrument
----------
First we create an underlying STIR futures security with data stored in the example::

    InterestRateFutureSecurityDefinition ERU2_DEFINITION = 
      new InterestRateFutureSecurityDefinition(LAST_TRADING_DATE, EURIBOR3M, NOTIONAL, FUTURE_FACTOR, NAME, TARGET);

Then we create an option on the STIR futures::

    InterestRateFutureOptionMarginSecurityDefinition OPTION_ERU2_DEFINITION = 
      new InterestRateFutureOptionMarginSecurityDefinition(ERU2_DEFINITION, EXPIRATION_DATE, STRIKE, IS_CALL);

We create a transaction on the above security with data stored in the file::

    InterestRateFutureOptionMarginTransactionDefinition TRANSACTION_1_DEFINITION = 
      new InterestRateFutureOptionMarginTransactionDefinition(OPTION_ERU2_DEFINITION, QUANTITY, TRADE_DATE_1, TRADE_PRICE);

The transaction is the purchase/sell of a given quantity on a given date for a given price.
 
This is the definition version of the transaction, i.e. it contains all the dates and data relevant to the trade.

The definition version is converted to the *derivative* version::

    InterestRateFutureOptionMarginTransaction TRANSACTION_1 = TRANSACTION_1_DEFINITION.toDerivative(REFERENCE_DATE, MARGIN_PRICE);

In this transformation, the dates are replaced by time between the valuation date and the different cash flow dates. Moreover the last priced used in the margining is stored. This is used to compute the present value which is the difference between the current price and the last margin price.


In this examples all the computation are done with a *projection and discounting* model, that we simply call *discounting*. No convexity adjustment for the daily margining is computed in this implementation. 


Present value and price
-----------------------

The first output we produce is the *present value* of the instrument. The present value is computed as the current price minus the last margin price multiplied by the notional, the accrual factor and the quantity.

The code to compute the present value simply read as::

    private static final PresentValueNormalSTIRFuturesCalculator PVNFC = PresentValueNormalSTIRFuturesCalculator.getInstance();
    MultipleCurrencyAmount pv = TRANSACTION_1.accept(PVNFC, NORMAL_MULTICURVES);

The present value calculator is used through the visitor pattern. The result of the present value computation is a **MultipleCurrencyAmount** involving the information about the currency. The present value calculator is further described in worked-out-examples/PresentValueDiscountingCalculator.

As a remark the current price of the option is computed as::

    FuturesPriceNormalSTIRFuturesCalculator POC = FuturesPriceNormalSTIRFuturesCalculator.getInstance();
    double optionPriceQ = TRANSACTION_Q.accept(POC, NORMAL_MULTICURVES) * 100.0;


    
Curve sensitivity
-----------------

The calculators used for curve sensitivity calculation are::

      PresentValueCurveSensitivityNormalSTIRFuturesCalculator PVCSNFC = PresentValueCurveSensitivityNormalSTIRFuturesCalculator.getInstance();
      ParameterSensitivityParameterCalculator<NormalSTIRFuturesProviderInterface> PSSFC = new ParameterSensitivityParameterCalculator<>(PVCSNFC);
      MarketQuoteSensitivityBlockCalculator<NormalSTIRFuturesProviderInterface> MQSBC = new MarketQuoteSensitivityBlockCalculator<>(PSSFC);
      MultipleCurrencyParameterSensitivity bucketedPv01 =  MQSBC.fromInstrument(TRANSACTION_1, NORMAL_MULTICURVES, BLOCK).multipliedBy(BP1);

The **MQSBC** computes the sensitivity to the market quotes (*bucketed PV01*). The computation is not scaled, i.e. it is for a movement of 1. The last part of the code (**multipliedBy**) multiply it by one basis point to match the market standard. In general, the OG-Analytics library uses absolute numbers (not percent or basis point) everywhere and provide the tools to rescale the output easily.

The format of the output is a *MultipleCurrencyParameterSensitivity* object which can be represented by::

    [EUR-DSCON-OIS, EUR]= (-0.0001, -0.0001, 0.0,  0.0,  -0.0026, -0.0046, -0.0061, 0.1790, -0.3696, 0.0265, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    [EUR-EURIBOR3M-FRAIRS, EUR]= (-0.0784, 0.0016, 0.0007, 0.0162, 212.5770, -361.1909, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

The information stored is, for each relevant curve and currency, the sensitivity to each market quote.

Another way of expressing curve sensitivity is *PV01*, total amount of the curve sensitivity, which is computed by::

    PV01CurveParametersCalculator<NormalSTIRFuturesProviderInterface> PV01CPC = new PV01CurveParametersCalculator<>(PVCSNFC);
    ReferenceAmount<Pair<String, Currency>> pv01 = TRANSACTION_1.accept(PV01CPC, NORMAL_MULTICURVES);

The form of the output is::

    [EUR-EURIBOR3M-FRAIRS, EUR]=-149.2087

Note that we compute the sensitivity to curve parameters here. Thus the present value does not exhibit any sensitivity to the discount curve.   For more about the relation between market quote sensitivity and curve parameter sensitivity, See the documentation, worked-out-examples/worked-out-example-stir-futures-discounting.rst.

Option Greeks
-----------------

Finally we produce option Greeks and position Greeks. For example delta and position delta are respectively computed by::

    DeltaNormalSTIRFutureOptionCalculator DNFOC = DeltaNormalSTIRFutureOptionCalculator.getInstance();
    Double delta = TRANSACTION_1.accept(DNFOC, NORMAL_MULTICURVES);
::
    
    PositionDeltaNormalSTIRFutureOptionCalculator PDNFOC = PositionDeltaNormalSTIRFutureOptionCalculator.getInstance();
    Double positionDelta = TRANSACTION_1.accept(PDNFOC, NORMAL_MULTICURVES);

The position delta is obtained from the delta multiplied by quantity of the transaction, and notional and payment accrual factor of the underlying futures. 

