package dev.ikm.kometite;

import dev.ikm.tinkar.common.id.PublicId;
import dev.ikm.tinkar.common.service.DataActivity;
import dev.ikm.tinkar.common.service.PrimitiveDataSearchResult;
import dev.ikm.tinkar.common.service.PrimitiveDataService;
import dev.ikm.tinkar.common.service.NidGenerator;
import org.eclipse.collections.api.block.procedure.primitive.IntProcedure;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.primitive.ImmutableIntList;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.ObjIntConsumer;

public class Kometite implements PrimitiveDataService, NidGenerator {

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
    public int nidForUuids(ImmutableList<UUID> immutableList) {
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
    public void forEach(ObjIntConsumer<byte[]> objIntConsumer) {

    }

    @Override
    public void forEachParallel(ObjIntConsumer<byte[]> objIntConsumer) {

    }

    @Override
    public void forEachParallel(ImmutableIntList immutableIntList, ObjIntConsumer<byte[]> objIntConsumer) {

    }

    @Override
    public byte[] getBytes(int i) {
        return new byte[0];
    }

    @Override
    public byte[] merge(int i, int i1, int i2, byte[] bytes, Object o, DataActivity dataActivity) {
        return new byte[0];
    }

    @Override
    public PrimitiveDataSearchResult[] search(String s, int i) throws Exception {
        return new PrimitiveDataSearchResult[0];
    }

    @Override
    public CompletableFuture<Void> recreateLuceneIndex() throws Exception {
        return null;
    }

    @Override
    public void forEachSemanticNidOfPattern(int i, IntProcedure intProcedure) {

    }

    @Override
    public void forEachPatternNid(IntProcedure intProcedure) {

    }

    @Override
    public void forEachConceptNid(IntProcedure intProcedure) {

    }

    @Override
    public void forEachStampNid(IntProcedure intProcedure) {

    }

    @Override
    public void forEachSemanticNid(IntProcedure intProcedure) {

    }

    @Override
    public void forEachSemanticNidForComponent(int i, IntProcedure intProcedure) {

    }

    @Override
    public void forEachSemanticNidForComponentOfPattern(int i, int i1, IntProcedure intProcedure) {

    }

    @Override
    public String name() {
        return "";
    }

    static {
        RocksDB.loadLibrary();
    }

    // Helper to convert int to little-endian byte[4]
    private static byte[] intToBytes(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    // Helper to convert byte[4] to int
    private static int bytesToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static void main(String[] args) {
        String dbPath = "rocksdb-int-bytes";
        try (final Options options = new Options().setCreateIfMissing(true);
             final RocksDB db = RocksDB.open(options, dbPath)) {

            // Example: PUT
            int exampleKey = 42;
            byte[] exampleValue = {1, 2, 3, 4, 5};
            db.put(intToBytes(exampleKey), exampleValue);

            // Example: GET
            byte[] value = db.get(intToBytes(exampleKey));
            System.out.println("Got value: " + Arrays.toString(value));

            // Example: MERGE (manual, since RocksDB merge operators require config; we'll simulate it)
            byte[] newValue = {6, 7};
            byte[] current = db.get(intToBytes(exampleKey));
            if (current != null) {
                // Simple: concatenate old and new
                byte[] merged = Arrays.copyOf(current, current.length + newValue.length);
                System.arraycopy(newValue, 0, merged, current.length, newValue.length);
                db.put(intToBytes(exampleKey), merged);
                System.out.println("Merged value: " + Arrays.toString(db.get(intToBytes(exampleKey))));
            } else {
                db.put(intToBytes(exampleKey), newValue);
            }

        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

}
