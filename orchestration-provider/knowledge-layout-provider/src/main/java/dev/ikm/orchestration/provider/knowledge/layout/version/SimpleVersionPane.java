package dev.ikm.orchestration.provider.knowledge.layout.version;

import dev.ikm.komet.framework.observable.ObservableField;
import dev.ikm.komet.framework.observable.ObservableSemanticVersion;
import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.component.version.KlGenericVersionPane;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.WidgetBlueprint;
import dev.ikm.tinkar.coordinate.stamp.calculator.Latest;
import dev.ikm.tinkar.entity.PatternEntityVersion;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.eclipse.collections.api.list.ImmutableList;

public class SimpleVersionPane extends WidgetBlueprint<BorderPane> implements KlGenericVersionPane<BorderPane> {

    private final SimpleObjectProperty<ObservableVersion> versionProperty = new SimpleObjectProperty<>();
    {
        versionProperty.subscribe(this::versionChanged);
    }

    private final GridPane gridPane = new GridPane();

    public SimpleVersionPane(KometPreferences preferences) {
        super(preferences, new BorderPane());
        setup();
    }

    public SimpleVersionPane(KlPreferencesFactory preferencesFactory, KlFactory gadgetFactory) {
        super(preferencesFactory, gadgetFactory, new BorderPane());
        setup();
    }

    private void setup() {
        fxGadget.setCenter(gridPane);
    }


    private void versionChanged() {
        gridPane.getChildren().clear();
        ObservableVersion version = versionProperty.get();
        switch (version) {
            case ObservableSemanticVersion semanticVersion -> {
                Latest<PatternEntityVersion> latestPatternEntityVersion = context().viewCoordinate().calculator().latestPatternEntityVersion(semanticVersion.patternNid());
                if (latestPatternEntityVersion.isPresent()) {
                    ImmutableList<ObservableField> fields = semanticVersion.fields(latestPatternEntityVersion.get());
                    //TODO: Need to get the stamp fields in somehow...
                    for (ObservableField field : fields) {
                        switch (field.fieldDataType()) {
                            case LONG -> {

                            }
                            case FLOAT -> {}
                            case STAMP -> {}
                            case DITREE -> {}
                            case STRING -> {}
                            case VERTEX -> {}
                            case BOOLEAN -> {}
                            case CONCEPT -> {}
                            case DECIMAL -> {}
                            case DIGRAPH -> {}
                            case INTEGER -> {}
                            case INSTANT -> {}
                            case PATTERN -> {}
                            case SEMANTIC -> {}
                            case BYTE_ARRAY -> {}
                            case OBJECT_ARRAY -> {}
                            case PLANAR_POINT -> {}
                            case SPATIAL_POINT -> {}
                            case STAMP_VERSION -> {}
                            case CONCEPT_VERSION -> {}
                            case PATTERN_VERSION -> {}
                            case COMPONENT_ID_SET -> {}
                            case FIELD_DEFINITION -> {}
                            case IDENTIFIED_THING -> {}
                            case SEMANTIC_VERSION -> {}
                            case COMPONENT_ID_LIST -> {}
                            case CONCEPT_CHRONOLOGY -> {}
                            case PATTERN_CHRONOLOGY -> {}
                            case SEMANTIC_CHRONOLOGY -> {}
                        }
                    }

                }
            }
            case ObservableVersion observableVersion -> {}
            case null -> {}
        }
    }

    @Override
    public ObjectProperty<ObservableVersion> versionProperty() {
        return versionProperty;
    }

    @Override
    protected void subWidgetRevert() {

    }

    @Override
    protected void subWidgetSave() {

    }

    @Override
    public void unsubscribeFromContext() {

    }

    @Override
    public void subscribeToContext() {

    }
}
