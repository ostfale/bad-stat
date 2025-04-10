package de.ostfale.qk.ui.dashboard.model;

import org.jboss.logging.Logger;

public class DashboardUIModel {

    private static final Logger log = Logger.getLogger(DashboardUIModel.class);
    
    private DashboardRankingUIModel dashboardRankingUIModel;

    public DashboardRankingUIModel getDashboardRankingUIModel() {
        return dashboardRankingUIModel;
    }

    public void setDashboardRankingUIModel(DashboardRankingUIModel dashboardRankingUIModel) {
        this.dashboardRankingUIModel = dashboardRankingUIModel;
    }

    

}
