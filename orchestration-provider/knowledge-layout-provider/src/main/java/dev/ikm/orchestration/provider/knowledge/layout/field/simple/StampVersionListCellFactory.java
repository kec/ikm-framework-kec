package dev.ikm.orchestration.provider.knowledge.layout.field.simple;

import dev.ikm.komet.framework.observable.ObservableVersion;
import dev.ikm.komet.layout.KlWidget;
import dev.ikm.tinkar.entity.EntityVersion;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * Factory class for creating custom ListCell components for a ListView
 * that displays {@link ObservableVersion} instances of type {@link EntityVersion}.
 *
 * This factory customizes the appearance and content of each cell
 * in the ListView by setting a preferred text for the stamp associated
 * with the given {@link ObservableVersion}.
 */
public class StampVersionListCellFactory implements Callback<ListView<ObservableVersion<EntityVersion>>, ListCell<ObservableVersion<EntityVersion>>> {
    final KlWidget enclosingKlWidget;

    public StampVersionListCellFactory(KlWidget enclosingKlWidget) {
        this.enclosingKlWidget = enclosingKlWidget;
    }


    /**
     * Creates a custom `ListCell` for a `ListView` of `ObservableVersion<EntityVersion>`.
     * This method customizes how the individual items in the `ListView` are displayed,
     * showing preferred text for the stamp associated with each `ObservableVersion`.
     *
     * @param param the `ListView` to which the custom `ListCell` will be applied
     * @return a new `ListCell` populated with preferred stamp text from the `ObservableVersion`,
     *         or an appropriate placeholder if the cell is empty or contains a null value
     */
    @Override
    public ListCell<ObservableVersion<EntityVersion>> call(ListView<ObservableVersion<EntityVersion>> param) {
        return new ListCell<>() {
            @Override
            public void updateItem(ObservableVersion<EntityVersion> entityVersion, boolean empty) {
                super.updateItem(entityVersion, empty);
                setGraphic(null);
                if (empty) {
                    setText(null);
                } else if (entityVersion != null) {
                    String stampText = enclosingKlWidget.context().viewCoordinate().getPreferredTextForStamp(entityVersion.stampNid());
                    setText(stampText);
                } else {
                    setText("Null value in list cell");
                }
            }
        };
    }
}
