import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Bzip2Compression {

    public byte[] compress(String text) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (BZip2CompressorOutputStream bzip2Out = new BZip2CompressorOutputStream(byteStream)) {
            bzip2Out.write(text.getBytes("UTF-8"));
        }
        return byteStream.toByteArray();
    }

    public String decompress(byte[] compressedData) throws IOException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(compressedData);
        try (BZip2CompressorInputStream bzip2In = new BZip2CompressorInputStream(byteStream)) {
            byte[] buffer = new byte[1024];
            int n;
            StringBuilder result = new StringBuilder();
            while ((n = bzip2In.read(buffer)) != -1) {
                result.append(new String(buffer, 0, n, "UTF-8"));
            }
            return result.toString();
        }
    }

}
