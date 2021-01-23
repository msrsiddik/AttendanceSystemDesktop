package msr.attend.desktop;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import msr.attend.desktop.model.ReportModel;

import java.util.Deque;

public class DetailsView {

    @FXML
    private TableView<ReportModel> detailsTable;

    @FXML
    private TableColumn<ReportModel, String> rollCol;

    @FXML
    private TableColumn<ReportModel, String> nameCol;

    @FXML
    private TableColumn<ReportModel, String> batchCol;

    @FXML
    private TableColumn<ReportModel, String> departCol;

    @FXML
    private TableColumn<ReportModel, String> statusCol;

    @FXML
    private TableColumn<ReportModel, String> timeCol;

    @FXML
    void initialize() {
        rollCol.setCellValueFactory(new PropertyValueFactory<>("roll"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        batchCol.setCellValueFactory(new PropertyValueFactory<>("batch"));
        departCol.setCellValueFactory(new PropertyValueFactory<>("depart"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
    }

    public void setUpDataTable(Deque<ReportModel> reportModels){
        detailsTable.getItems().addAll(reportModels);
    }
}
