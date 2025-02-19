package dev.ikm.orchestration.provider.knowledge.layout.gadget.layout;

import dev.ikm.tinkar.common.binary.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.layout.Priority;

/**
 * Represents a layout record that defines grid-based layout constraints and properties.
 * This includes position-related settings such as row and column indices, span values,
 * alignment preferences, sizing constraints, and fill behavior.
 *
 * This class implements {@code Encodable}, enabling its serialization and deserialization
 * through custom encoding and decoding methods.
 */
public record LayoutRecord(
        int columnIndex,
        int rowIndex,
        int columnSpan,
        int rowSpan,
        Priority hGrow,
        Priority vGrow,
        HPos hAlignment,
        VPos vAlignment,
        Insets margin,
        Double maxHeight,
        Double maxWidth,
        Double preferredHeight,
        Double preferredWidth,
        boolean fillHeight,
        boolean fillWidth) implements Encodable {


    private static final int marshalVersion = 1;

    /**
     * Encodes the properties of the layout record into the given {@code EncoderOutput}.
     *
     * @param out the {@code EncoderOutput} to which the layout record properties are written
     */
    @Override
    @Encoder
    public void encode(EncoderOutput out) {
        out.writeInt(marshalVersion);
        out.writeInt(columnIndex);
        out.writeInt(rowIndex);
        out.writeInt(columnSpan);
        out.writeInt(rowSpan);
        out.writeString(hGrow.name());
        out.writeString(vGrow.name());
        out.writeString(hAlignment.name());
        out.writeString(vAlignment.name());
        out.writeDouble(margin.getTop());
        out.writeDouble(margin.getRight());
        out.writeDouble(margin.getBottom());
        out.writeDouble(margin.getLeft());
        out.writeDouble(maxHeight);
        out.writeDouble(maxWidth);
        out.writeDouble(preferredHeight);
        out.writeDouble(preferredWidth);
        out.writeBoolean(fillHeight);
        out.writeBoolean(fillWidth);
    }

    /**
     * Decodes a {@code DecoderInput} to reconstruct a {@code LayoutRecord} instance.
     * The method reads the object version and deserializes the contained properties
     * if the version matches the supported marshal version.
     *
     * @param in the {@code DecoderInput} from which the layout record properties
     *           are read and reconstructed
     * @return a {@code LayoutRecord} instance containing the deserialized properties
     * @throws UnsupportedOperationException if the object version is unsupported
     */
    @Decoder
    public static LayoutRecord decode(DecoderInput in) {
        int objectMarshalVersion = in.readInt();
        if (objectMarshalVersion == marshalVersion) {
            return new LayoutRecord(
                    in.readInt(), // int columnIndex,
                    in.readInt(), // int rowIndex,
                    in.readInt(), // int columnSpan,
                    in.readInt(), // int rowSpan,
                    Priority.valueOf(in.readString()), // Priority hGrow,
                    Priority.valueOf(in.readString()), // Priority vGrow,
                    HPos.valueOf(in.readString()), // HPos hAlignment,
                    VPos.valueOf(in.readString()), // VPos vAlignment,
                    new Insets(in.readDouble(), // Insets margin top,
                            in.readDouble(), // Insets margin right,
                            in.readDouble(), // Insets margin bottom,
                            in.readDouble() // Insets margin left,
                    ),
                    in.readDouble(), // Double maxHeight,
                    in.readDouble(), // Double maxWidth,
                    in.readDouble(), // Double preferredHeight,
                    in.readDouble(), // Double preferredWidth
                    in.readBoolean(), // boolean fillHeight
                    in.readBoolean() // boolean fillWidth
             );
        } else {
            throw new UnsupportedOperationException("Unsupported version: " + objectMarshalVersion);
        }

    }
}
