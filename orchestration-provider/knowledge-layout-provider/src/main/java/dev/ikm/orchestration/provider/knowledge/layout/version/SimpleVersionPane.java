package dev.ikm.orchestration.provider.knowledge.layout.version;

import dev.ikm.komet.framework.observable.*;
import dev.ikm.komet.layout.KlFactory;
import dev.ikm.komet.layout.component.version.KlGenericVersionPane;
import dev.ikm.komet.layout.preferences.KlPreferenceFactoryProvider.PreferenceFactoryWithParentPreferences;
import dev.ikm.komet.layout.preferences.KlPreferencesFactory;
import dev.ikm.komet.preferences.KometPreferences;
import dev.ikm.orchestration.provider.knowledge.layout.field.simple.SimpleGenericFieldPane;
import dev.ikm.orchestration.provider.knowledge.layout.field.simple.SimpleGenericFieldPaneFactory;
import dev.ikm.orchestration.provider.knowledge.layout.gadget.blueprint.WidgetBlueprint;
import dev.ikm.tinkar.coordinate.stamp.calculator.Latest;
import dev.ikm.tinkar.entity.PatternEntityVersion;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.eclipse.collections.api.list.ImmutableList;

import java.util.concurrent.atomic.AtomicInteger;

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
        PreferenceFactoryWithParentPreferences preferenceFactoryWithParentPreferences =
                new PreferenceFactoryWithParentPreferences(preferences(), SimpleGenericFieldPane.class);
        SimpleGenericFieldPaneFactory simpleGenericFieldPaneFactory = new SimpleGenericFieldPaneFactory();
        AtomicInteger row = new AtomicInteger(0);
        AtomicInteger col = new AtomicInteger(0);
        switch (version) {
            case ObservableSemanticVersion semanticVersion -> {
                Latest<PatternEntityVersion> latestPatternEntityVersion = context().viewCoordinate().calculator().latestPatternEntityVersion(semanticVersion.patternNid());
                if (latestPatternEntityVersion.isPresent()) {
                    ImmutableList<ObservableField> fields = semanticVersion.fields(latestPatternEntityVersion.get());
                    for (ObservableField semanticField : fields) {
                        SimpleGenericFieldPane genericFieldPane= simpleGenericFieldPaneFactory.create(preferenceFactoryWithParentPreferences);
                        gridPane.add(genericFieldPane.fxGadget(), row.getAndIncrement(), col.get());
                        genericFieldPane.setField(semanticField);
                    }
                    addStampFields(semanticVersion.stampNid(), simpleGenericFieldPaneFactory, preferenceFactoryWithParentPreferences, row, col);
                }
            }
            case ObservableVersion observableVersion -> {
                addStampFields(observableVersion.stampNid(), simpleGenericFieldPaneFactory, preferenceFactoryWithParentPreferences, row, col);
            }
            case null -> {}
        }
    }

    private void addStampFields(int stampNid, SimpleGenericFieldPaneFactory simpleGenericFieldPaneFactory, PreferenceFactoryWithParentPreferences preferenceFactoryWithParentPreferences, AtomicInteger row, AtomicInteger col) {
        ObservableStamp observableStamp = ObservableStamp.get(stampNid);
        ObservableStampVersion latestStamp = observableStamp.lastVersion();
        for (ObservableField stampField : latestStamp.fields()) {
            SimpleGenericFieldPane genericFieldPane= simpleGenericFieldPaneFactory.create(preferenceFactoryWithParentPreferences);
            gridPane.add(genericFieldPane.fxGadget(), col.get(), row.getAndIncrement());
            genericFieldPane.setField(stampField);
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
