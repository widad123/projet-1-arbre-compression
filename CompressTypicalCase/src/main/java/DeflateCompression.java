import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class DeflateCompression {

    public byte[] compress(String text) {
        try {
            byte[] input = text.getBytes("UTF-8");
            Deflater deflater = new Deflater();
            deflater.setInput(input);
            deflater.finish();

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream(input.length);
            byte[] buffer = new byte[1024];
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer);
                byteStream.write(buffer, 0, count);
            }
            deflater.end();
            return byteStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decompress(byte[] compressedData) {
        try {
            Inflater inflater = new Inflater();
            inflater.setInput(compressedData);

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream(compressedData.length);
            byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                byteStream.write(buffer, 0, count);
            }
            inflater.end();
            return byteStream.toString("UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
