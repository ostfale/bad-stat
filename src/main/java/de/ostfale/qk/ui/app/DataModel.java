package de.ostfale.qk.ui.app;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;
import org.jboss.logging.Logger;

import java.util.Comparator;
import java.util.List;

public class DataModel<T> {

    private static final Logger log = Logger.getLogger(DataModel.class);

    private final ObservableList<T> objectList = FXCollections.observableArrayList();
    private final ObjectProperty<T> currentObject = new SimpleObjectProperty<>();

    private ChangeListener<T> changeListener = null;
    private Comparator<T> comparator = null;
    private StringConverter<T> stringConverter = null;

    public void updateModel(List<T> aList, ComboBox<T> aCombobox) {
        log.debug("Update model for ComboBox");
        if (!currentObject.isBound()) {
            log.trace("Bind selected combobox");
            currentObject.bind(aCombobox.getSelectionModel().selectedItemProperty());
            currentObject.addListener(changeListener);
        }

        if (stringConverter != null) {
            aCombobox.setConverter(stringConverter);
        }

        if (comparator != null) {
            var sortedList = this.getSortedList(comparator);
            aCombobox.setItems(sortedList);
            // sortedList.comparatorProperty().bind(aCombobox.getSelectionModel().com);
        } else {
            aCombobox.setItems(objectList);
        }
        setItemList(aList);
    }

    public void updateModel(List<T> aList, ListView<T> aListView) {
        log.debug("Update model for ListView");
        if (!currentObject.isBound()) {
            log.trace("Bind selected listview");
            currentObject.bind(aListView.getSelectionModel().selectedItemProperty());
            currentObject.addListener(changeListener);
        }

        if (comparator != null) {
            var sortedList = this.getSortedList(comparator);
            aListView.setItems(sortedList);
        } else {
            aListView.setItems(objectList);
        }
        setItemList(aList);
    }

    public void updateModel(List<T> aList, TableView<T> tableView) {
        log.debug("Update model for TableView");
        if (!currentObject.isBound() && changeListener != null) {
            log.trace("Bind selected table row");
            currentObject.bind(tableView.getSelectionModel().selectedItemProperty());
            currentObject.addListener(changeListener);
        }

        objectList.setAll(aList);
        if (comparator != null) {
            log.debug("Use sorted list to display data...");
            var sortedList = this.getSortedList(comparator);
            tableView.setItems(sortedList);
            sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        } else {
            tableView.setItems(objectList);
        }
    }

    public void setStringConverter(StringConverter<T> stringConverter) {
        this.stringConverter = stringConverter;
    }

    public void setChangeListener(ChangeListener<T> changeListener) {
        this.changeListener = changeListener;
    }

    public void setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public void setItemList(List<T> aList) {
        objectList.clear();
        objectList.addAll(aList);
    }

    private SortedList<T> getSortedList(Comparator<T> aComparator) {
        SortedList<T> sortedList = new SortedList<>(objectList);
        sortedList.setComparator(aComparator);
        return sortedList.sorted();
    }

}
