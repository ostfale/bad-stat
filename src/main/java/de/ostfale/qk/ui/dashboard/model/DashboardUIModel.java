package de.ostfale.qk.ui.dashboard.model;

public class DashboardUIModel {

    private DashboardRankingUIModel dashboardRankingUIModel;
    private DashBoardTourCalDTO dashBoardTourCalDTO;

    public DashBoardTourCalDTO getDashBoardTourCalDTO() {
        return dashBoardTourCalDTO;
    }

    public void setDashBoardTourCalDTO(DashBoardTourCalDTO dashBoardTourCalDTO) {
        this.dashBoardTourCalDTO = dashBoardTourCalDTO;
    }

    public DashboardRankingUIModel getDashboardRankingUIModel() {
        return dashboardRankingUIModel;
    }

    public void setDashboardRankingUIModel(DashboardRankingUIModel dashboardRankingUIModel) {
        this.dashboardRankingUIModel = dashboardRankingUIModel;
    }
}
