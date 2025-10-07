package dev.ikm.komet.chron;

import dev.ikm.tinkar.common.id.PublicId;
import dev.ikm.tinkar.common.service.DataActivity;
import dev.ikm.tinkar.common.service.NidGenerator;
import dev.ikm.tinkar.common.service.PrimitiveDataSearchResult;
import dev.ikm.tinkar.common.service.PrimitiveDataService;
import net.openhft.chronicle.map.ChronicleMap;
import org.eclipse.collections.api.block.procedure.primitive.IntProcedure;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.primitive.ImmutableIntList;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.ObjIntConsumer;

public class KometChron implements PrimitiveDataService, NidGenerator {
    @Override
    public int newNid() {
        return 0;
    }

    @Override
    public long writeSequence() {
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public int nidForUuids(UUID... uuids) {
        return 0;
    }

    @Override
    public int nidForUuids(ImmutableList<UUID> uuidList) {
        return 0;
    }

    @Override
    public boolean hasUuid(UUID uuid) {
        return false;
    }

    @Override
    public boolean hasPublicId(PublicId publicId) {
        return false;
    }

    @Override
    public void forEach(ObjIntConsumer<byte[]> action) {

    }

    @Override
    public void forEachParallel(ObjIntConsumer<byte[]> action) {

    }

    @Override
    public void forEachParallel(ImmutableIntList nids, ObjIntConsumer<byte[]> action) {

    }

    @Override
    public byte[] getBytes(int nid) {
        return new byte[0];
    }

    @Override
    public byte[] merge(int nid, int patternNid, int referencedComponentNid, byte[] value, Object sourceObject, DataActivity activity) {
        return new byte[0];
    }

    @Override
    public PrimitiveDataSearchResult[] search(String query, int maxResultSize) throws Exception {
        return new PrimitiveDataSearchResult[0];
    }

    @Override
    public CompletableFuture<Void> recreateLuceneIndex() throws Exception {
        return null;
    }

    @Override
    public void forEachSemanticNidOfPattern(int patternNid, IntProcedure procedure) {

    }

    @Override
    public void forEachPatternNid(IntProcedure procedure) {

    }

    @Override
    public void forEachConceptNid(IntProcedure procedure) {

    }

    @Override
    public void forEachStampNid(IntProcedure procedure) {

    }

    @Override
    public void forEachSemanticNid(IntProcedure procedure) {

    }

    @Override
    public void forEachSemanticNidForComponent(int componentNid, IntProcedure procedure) {

    }

    @Override
    public void forEachSemanticNidForComponentOfPattern(int componentNid, int patternNid, IntProcedure procedure) {

    }

    @Override
    public String name() {
        return "";
    }

    public static void main(String[] args) throws IOException {
        // Create or open a ChronicleMap with Integer (int) keys and byte[] values
        try (ChronicleMap<Integer, byte[]> map = ChronicleMap
                .of(Integer.class, byte[].class)
                .name("int-to-bytes")
                .entries(10_000) // Estimate the number of entries
                .averageValueSize(256) // Estimate average byte[] size
                .createOrRecoverPersistedTo(new File("int-bytes-map.dat"))) {

            // Example: PUT
            int exampleKey = 42;
            byte[] exampleValue = {1, 2, 3, 4, 5};
            map.put(exampleKey, exampleValue);

            // Example: GET
            byte[] value = map.get(exampleKey);
            System.out.println("Got value: " + Arrays.toString(value));

            // Example: MERGE (simulate merge; ChronicleMap doesn't have direct merge yet)
            // We'll merge by concatenating old and new values if present
            map.merge(exampleKey, new byte[] {6, 7},
                    (oldVal, newVal) -> {
                        byte[] merged = Arrays.copyOf(oldVal, oldVal.length + newVal.length);
                        System.arraycopy(newVal, 0, merged, oldVal.length, newVal.length);
                        return merged;
                    });

            // Check merged value
            byte[] mergedValue = map.get(exampleKey);
            System.out.println("Merged value: " + Arrays.toString(mergedValue));
        }
    }

}
