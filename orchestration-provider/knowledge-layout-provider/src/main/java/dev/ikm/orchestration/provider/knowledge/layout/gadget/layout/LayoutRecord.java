package dev.ikm.orchestration.provider.knowledge.layout.gadget.layout;

import dev.ikm.tinkar.common.binary.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.layout.Priority;

/**
 * Represents a layout record encapsulating various layout properties for UI elements.
 * This record is designed to store layout parameters that define the position, size,
 * and alignment of a component in a layout.
 * <p>
 * Implements {@code Encodable} to support serialization and deserialization of layout configurations.
 * <p>
 * Fields:
 * <p>- {@code columnIndex}: The column index of the component in the layout.
 * <p>- {@code rowIndex}: The row index of the component in the layout.
 * <p>- {@code columnSpan}: The number of columns the component should span.
 * <p>- {@code rowSpan}: The number of rows the component should span.
 * <p>- {@code hGrow}: The horizontal grow priority for the component.
 * <p>- {@code vGrow}: The vertical grow priority for the component.
 * <p>- {@code hAlignment}: The horizontal alignment of the component.
 * <p>- {@code vAlignment}: The vertical alignment of the component.
 * <p>- {@code margin}: The margin around the component represented as an {@code Insets} instance.
 * <p>- {@code maxHeight}: The maximum height of the component.
 * <p>- {@code maxWidth}: The maximum width of the component.
 * <p>- {@code preferredHeight}: The preferred height of the component.
 * <p>- {@code preferredWidth}: The preferred width of the component.
 * <p>
 * The class includes methods for encoding the object for serialization and decoding it from
 * its serialized form.
 */
public record LayoutRecord(
        int columnIndex,
        int rowIndex,
        int columnSpan,
        int rowSpan,
        Priority hGrow,
        Priority vGrow,
        HPos hAlighment,
        VPos vAlignment,
        Insets margin,
        Double maxHeight,
        Double maxWidth,
        Double preferredHeight,
        Double preferredWidth) implements Encodable {


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
        out.writeString(hAlighment.name());
        out.writeString(vAlignment.name());
        out.writeDouble(margin.getTop());
        out.writeDouble(margin.getRight());
        out.writeDouble(margin.getBottom());
        out.writeDouble(margin.getLeft());
        out.writeDouble(maxHeight);
        out.writeDouble(maxWidth);
        out.writeDouble(preferredHeight);
        out.writeDouble(preferredWidth);
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
                    HPos.valueOf(in.readString()), // HPos hAlighment,
                    VPos.valueOf(in.readString()), // VPos vAlignment,
                    new Insets(in.readDouble(), // Insets margin top,
                            in.readDouble(), // Insets margin right,
                            in.readDouble(), // Insets margin bottom,
                            in.readDouble() // Insets margin left,
                    ),
                    in.readDouble(), // Double maxHeight,
                    in.readDouble(), // Double maxWidth,
                    in.readDouble(), // Double preferredHeight,
                    in.readDouble() // Double preferredWidth
             );
        } else {
            throw new UnsupportedOperationException("Unsupported version: " + objectMarshalVersion);
        }

    }
}
