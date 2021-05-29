package name.gst.merge_motion_image;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import org.apache.commons.imaging.formats.jpeg.xmp.JpegXmpRewriter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MergeMotionImage {

    public static void main(String[] args) throws IOException, ImageWriteException, ImageReadException {
        final File imageFile = new File(args[0]);
        final File videoFile = new File(args[1]);
        final File outputFile = new File(args[2]);
        final File tmpOutputFile = new File(args[2] + ".tmp");

        FileUtils.writeByteArrayToFile(outputFile, FileUtils.readFileToByteArray(imageFile));
        FileUtils.writeByteArrayToFile(outputFile, FileUtils.readFileToByteArray(videoFile), true);

        try(final FileOutputStream xmpOutStream = new FileOutputStream(tmpOutputFile)) {
            final JpegXmpRewriter xmpRewriter = new JpegXmpRewriter();
            xmpRewriter.updateXmpXml(new ByteSourceFile(outputFile), xmpOutStream,
                    "<x:xmpmeta xmlns:x=\"adobe:ns:meta/\" x:xmptk=\"Adobe XMP Core 5.1.0-jc003\">\n" +
                            "  <rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">\n" +
                            "    <rdf:Description rdf:about=\"\"\n" +
                            "        xmlns:GCamera=\"http://ns.google.com/photos/1.0/camera/\"\n" +
                            "      GCamera:MicroVideo=\"1\"\n" +
                            "      GCamera:MicroVideoVersion=\"1\"\n" +
                            "      GCamera:MicroVideoOffset=\"" + videoFile.length() + "\"\n" +
                            "      GCamera:MicroVideoPresentationTimestampUs=\"-1\"/>\n" +
                            "  </rdf:RDF>\n" +
                            "</x:xmpmeta>"
            );
        }

        tmpOutputFile.renameTo(outputFile);
    }
}
