package tern.internal.org.apache.commons.io.input;

import java.io.InputStream;

/**
 * Closed input stream. This stream returns -1 to all attempts to read
 * something from the stream.
 * <p>
 * Typically uses of this class include testing for corner cases in methods
 * that accept input streams and acting as a sentinel value instead of a
 * <code>null</code> input stream.
 *
 * @version $Id: ClosedInputStream.java 1304052 2012-03-22 20:55:29Z ggregory $
 * @since 1.4
 */
public class ClosedInputStream extends InputStream {
    
    /**
     * A singleton.
     */
    public static final ClosedInputStream CLOSED_INPUT_STREAM = new ClosedInputStream();

    /**
     * Returns -1 to indicate that the stream is closed.
     *
     * @return always -1
     */
    @Override
    public int read() {
        return -1;
    }

}
