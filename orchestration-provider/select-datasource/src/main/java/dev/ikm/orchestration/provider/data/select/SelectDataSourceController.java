/*
 * Copyright © 2015 Integrated Knowledge Management (support@ikm.dev)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Sample Skeleton for 'SelectDataSource.fxml' Controller Class
 */

package dev.ikm.orchestration.provider.data.select;


import dev.ikm.komet.framework.propsheet.KometPropertyEditorFactory;
import dev.ikm.komet.framework.propsheet.SheetItem;
import dev.ikm.orchestration.interfaces.Orchestrator;
import dev.ikm.tinkar.common.service.DataServiceController;
import dev.ikm.tinkar.common.service.DataServiceProperty;
import dev.ikm.tinkar.common.service.DataUriOption;
import dev.ikm.tinkar.common.service.PrimitiveData;
import dev.ikm.tinkar.common.util.text.NaturalOrder;
import dev.ikm.tinkar.common.validation.ValidationRecord;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static dev.ikm.orchestration.interfaces.Lifecycle.SELECTED_DATA_SOURCE;

/**
 * The SelectDataSourceController class represents a controller for selecting a data source.
 * It is responsible for initializing the UI components, handling user interactions, and saving the selected data source.
 */
public class SelectDataSourceController {

    private File rootFolder = new File(System.getProperty("user.home"), "Solor");

    private File workingFolder = new File(System.getProperty("user.dir"), "target");

    private final Orchestrator orchestrator;

    private Map<DataServiceProperty, SimpleStringProperty> dataServicePropertyStringMap = new HashMap<>();

    private ValidationSupport validationSupport = new ValidationSupport();

    @FXML
    private GridPane inputGrid;

    @FXML
    private PropertySheet propertySheet;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="dataSourceChoiceBox"
    private ChoiceBox<DataServiceController<?>> dataSourceChoiceBox; // Value injected by FXMLLoader

    @FXML // fx:id="cancelButton"
    private Button cancelButton; // Value injected by FXMLLoader

    @FXML // fx:id="rootBorderPane"
    private BorderPane rootBorderPane; // Value injected by FXMLLoader

    @FXML // fx:id="fileListView"
    private ListView<DataUriOption> fileListView; // Value injected by FXMLLoader

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        Platform.exit();
    }

    /**
     * The SelectDataSourceController class represents a controller for selecting a data source provider.
     */
    public SelectDataSourceController(Orchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    /**
     * Initializes the SelectDataSourceController after the FXML file is loaded.
     * This method is called by the FXMLLoader when the initialization is complete.
     */
    @FXML
    void initialize() {
        assert dataSourceChoiceBox != null : "fx:id=\"dataSourceChoiceBox\" was not injected: check your FXML file 'SelectDataSource.fxml'.";
        assert cancelButton != null : "fx:id=\"cancelButton\" was not injected: check your FXML file 'SelectDataSource.fxml'.";
        ObservableList<DataServiceController> controllerOptions = FXCollections.observableList(PrimitiveData.getControllerOptions());
        controllerOptions.forEach(dataServiceController -> dataSourceChoiceBox.getItems().add(dataServiceController));

        dataSourceChoiceBox.getSelectionModel().selectedItemProperty().addListener(this::dataSourceChanged);

        fileListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                okButtonPressed(null);
            }
        });

        propertySheet.setPropertyEditorFactory(new KometPropertyEditorFactory(null));

        Platform.runLater(() ->
                dataSourceChoiceBox.getSelectionModel().select(controllerOptions.get(1)));
    }

    /**
     * This method is called when the data source is changed in the application.
     * It updates the UI components and properties based on the new data source selection.
     *
     * @param observable the observable value representing the data source selection
     * @param oldValue   the previous data source controller
     * @param newValue   the new data source controller
     */
    void dataSourceChanged(ObservableValue<? extends DataServiceController<?>> observable,
                           DataServiceController<?> oldValue,
                           DataServiceController<?> newValue) {
        saveDataServiceProperties(oldValue);

        dataServicePropertyStringMap.clear();
        fileListView.getItems().clear();
        fileListView.getItems().addAll(dataSourceChoiceBox.getValue().providerOptions());
        fileListView.getItems().sort(NaturalOrder.getObjectComparator());
        fileListView.getSelectionModel().selectFirst();
        fileListView.getSelectionModel().selectFirst();
        fileListView.requestFocus();

        propertySheet.getItems().clear();
        validationSupport = new ValidationSupport();

        DataServiceController<?> dataSourceController = dataSourceChoiceBox.getValue();
        dataSourceController.providerProperties().forEachKeyValue(
                (DataServiceProperty key, String value) -> {
                    SimpleStringProperty dataServiceProperty = new SimpleStringProperty(key, key.propertyName(), value);
                    dataServicePropertyStringMap.put(key, dataServiceProperty);
                    if (key.validate()) {
                        Validator validator = new Validator<String>() {

                            @Override
                            public ValidationResult apply(Control control, String s) {
                                ValidationResult validationResult = new ValidationResult();
                                for (ValidationRecord validationRecord : dataSourceController.validate(key, s, control)) {
                                    switch (validationRecord.severity()) {
                                        case ERROR -> validationResult.add(ValidationMessage.error(
                                                (Control) validationRecord.target(), validationRecord.message()));
                                        case INFO -> validationResult.add(ValidationMessage.info(
                                                (Control) validationRecord.target(), validationRecord.message()));
                                        case OK -> validationResult.add(ValidationMessage.ok(
                                                (Control) validationRecord.target(), validationRecord.message()));
                                        case WARNING -> validationResult.add(ValidationMessage.warning(
                                                (Control) validationRecord.target(), validationRecord.message()));
                                    }
                                }
                                return validationResult;
                            }
                        };
                        if (key.hiddenText()) {
                            propertySheet.getItems().add(SheetItem.makeForPassword(dataServiceProperty, validationSupport, validator));
                        } else {
                            propertySheet.getItems().add(SheetItem.make(dataServiceProperty, validationSupport, validator, null));
                        }
                    } else {
                        if (key.hiddenText()) {
                            propertySheet.getItems().add(SheetItem.makeForPassword(dataServiceProperty));
                        } else {
                            propertySheet.getItems().add(SheetItem.make(dataServiceProperty));
                        }
                    }
                });
    }

    /**
     * This method is called when the OK button is pressed in the UI.
     * It performs the necessary actions to save the data service properties,
     * update the data source selection, and set the lifecycle property of the orchestrator.
     *
     * @param event the action event triggered by the OK button
     */
    @FXML
    void okButtonPressed(ActionEvent event) {
        saveDataServiceProperties(dataSourceChoiceBox.getValue());
        dataSourceChoiceBox.getValue().setDataUriOption(fileListView.getSelectionModel().getSelectedItem());
        PrimitiveData.setController(dataSourceChoiceBox.getValue());
       Platform.runLater(() -> orchestrator.lifecycleProperty().set(SELECTED_DATA_SOURCE));
    }

    /**
     * Saves the data service properties for a given data service controller.
     *
     * @param dataServiceController the data service controller to save the properties for
     */
    private void saveDataServiceProperties(DataServiceController<?> dataServiceController) {
        dataServicePropertyStringMap.forEach((dataServiceProperty, simpleStringProperty) -> {
            dataServiceController.setDataServiceProperty(dataServiceProperty, simpleStringProperty.getValue());
        });
    }
}