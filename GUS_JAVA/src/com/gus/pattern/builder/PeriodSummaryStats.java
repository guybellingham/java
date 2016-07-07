package com.gus.pattern.builder;

import java.util.Collections;
import java.util.List;

/**
 * <p>For a (PPA) scenario this immutable 'stats' bean keeps 'period stats' for Roles and/or Costs 
 * for output in the timeline and heatmap views. 
 * <li>You must use a 'configured' {@linkplain PeriodStatsBuilder} to <code>build</code> stats 
 * for a particular scenario. 
 * <pre> 
 * PeriodSummaryStats summaryStats = new PeriodStatsBuilder(scenario).withRoleStats().build();
 * RoleStats = summaryStats.getRoleStats();
 * </pre>
 * @author Guy
 * @see PeriodStatsBuilder
 * 
 */
public class PeriodSummaryStats {
	
	private long scenarioId;
    /**
     * Collects periodic Role statistics if configured.
     */
    private final Object roleSummaryStats;
    /**
     * Collects periodic costs statistics if configured.
     */
    private final Object costSummaryStats;
    
    /**
     * Constructs the instance of summary stats using the configured <code>builder</code>.
     * @param builder
     */
    private PeriodSummaryStats(PeriodStatsBuilder builder) {
        this.scenarioId = builder.scenarioId;
        this.roleSummaryStats = builder.roleStats;
        this.costSummaryStats = builder.costStats;
    }
    
    
    public List getRoleStats() {
        if(roleSummaryStats == null) {
            throw new IllegalStateException("PeriodSummaryStats was not configured to provide role stats.");
        }
        // would invoke the proxy method  roleSummaryStats.getRoleStats();
        return Collections.EMPTY_LIST;
    }

    public List getCostStats() {
        if(costSummaryStats == null) {
            throw new IllegalStateException("PeriodSummaryStats was not configured to provide cost stats.");
        }
        // would invoke the proxy method  costSummaryStats.getCostStats();
        return Collections.EMPTY_LIST;
    }
    
    /**
     * Use this 'builder' class to configure and then 'build' an immutable 
     * {@linkplain PeriodSummaryStats} bean. 
     * @author Guy
     */
    public static class PeriodStatsBuilder {
        private final long scenarioId;
        private Object roleStats;
        private Object costStats;
        /**
         * Always 'build' period stats for a particular optimizer scenario. 
         * @param scenarioId - required OptScenario id
         */
        public PeriodStatsBuilder(long scenarioId) {
            this.scenarioId = scenarioId;
        }
        /**
         * Configure this builder to collect all stats for the scenario.
         */
        public PeriodStatsBuilder withAll() {
            withRoleStats();
            withCostStats();
            return this;
        }
        /**
         * Configure this builder and collect period Role stats for the scenario.
         */
        public PeriodStatsBuilder withRoleStats() {
            this.roleStats = "RoleStatsProxy";
            return this;
        }
        /**
         * Configure this builder and collect period costs stats for the scenario.
         */
        public PeriodStatsBuilder withCostStats() {
            this.costStats = "CostStatsProxy";
            return this;
        }
        /**
         * @return an immutable instance of PeriodSummaryStats configured for use.
         */
        public PeriodSummaryStats build() {
            return new PeriodSummaryStats(this);
        }
    }

}
