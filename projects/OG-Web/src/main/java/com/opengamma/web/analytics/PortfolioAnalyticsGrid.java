/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.analytics;

import java.util.List;

import com.google.common.collect.Lists;
import com.opengamma.core.position.Portfolio;
import com.opengamma.engine.ComputationTargetResolver;
import com.opengamma.engine.view.compilation.CompiledViewDefinition;
import com.opengamma.id.ObjectId;
import com.opengamma.web.analytics.blotter.BlotterColumnMapper;

/**
 * A grid for displaying portfolio analytics data.
 */
/* package */ class PortfolioAnalyticsGrid extends MainAnalyticsGrid {

  private final PortfolioGridStructure _gridStructure;

  /* package */ PortfolioAnalyticsGrid(PortfolioGridStructure gridStructure,
                                       String gridId,
                                       ComputationTargetResolver targetResolver,
                                       ViewportListener viewportListener) {
    super(AnalyticsView.GridType.PORTFORLIO, gridStructure, gridId, targetResolver, viewportListener);
    _gridStructure = gridStructure;
  }

  /* package */ PortfolioAnalyticsGrid withUpdatedRows(Portfolio portfolio) {
    PortfolioGridStructure updatedStructure = _gridStructure.withUpdatedRows(portfolio);
    return new PortfolioAnalyticsGrid(updatedStructure, getCallbackId(), getTargetResolver(), getViewportListener());
  }

  // TODO don't the value mappings change when the columns are updated?
  /* package */ PortfolioAnalyticsGrid withUpdatedColumns(CompiledViewDefinition compiledViewDef) {
    PortfolioGridStructure updatedStructure = _gridStructure.withUpdatedColumns(compiledViewDef);
    return new PortfolioAnalyticsGrid(updatedStructure, getCallbackId(), getTargetResolver(), getViewportListener());
  }

  /* package */ static PortfolioAnalyticsGrid forAnalytics(String gridId,
                                                           Portfolio portfolio,
                                                           ComputationTargetResolver targetResolver,
                                                           ViewportListener viewportListener) {
    PortfolioGridStructure gridStructure = PortfolioGridStructure.create(portfolio, new ValueMappings());
    return new PortfolioAnalyticsGrid(gridStructure, gridId, targetResolver, viewportListener);
  }

  /* package */ static PortfolioAnalyticsGrid forBlotter(String gridId,
                                                         Portfolio portfolio,
                                                         ComputationTargetResolver targetResolver,
                                                         ViewportListener viewportListener,
                                                         BlotterColumnMapper blotterColumnMapper) {
    PortfolioGridStructure gridStructure = BlotterGridStructure.create(portfolio, blotterColumnMapper);
    return new PortfolioAnalyticsGrid(gridStructure, gridId, targetResolver, viewportListener);
  }

  /* package */ List<String> updateEntity(ObjectId id, ResultsCache cache) {
    List<String> ids = Lists.newArrayList();
    for (MainGridViewport viewport : getViewports().values()) {
      viewport.updateResults(cache);
      ids.add(viewport.getCallbackId());
    }
    return ids;
  }
}
