package com.mtpv.ghmcepettycase;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PlurkInputStream extends FilterInputStream {

    protected PlurkInputStream(InputStream in) {
        super(in);
    }

    @Override
    public int read(byte[] buffer, int offset, int count)
        throws IOException {
        int ret = super.read(buffer, offset, count);
        /*for ( int i = 2; i < buffer.length; i++ ) {
            if ( buffer[i - 2] == 0x2c && buffer[i - 1] == 0x05
                && buffer[i] == 0 ) {
                buffer[i - 1] = 0;
            }
        }*/
        for ( int i = 6; i < buffer.length - 4; i++ ) {
            if ( buffer[i] == 0x2c ) {
                if ( buffer[i + 2] == 0 && buffer[i + 1] > 0
                    && buffer[i + 1] <= 48 ) {
                    buffer[i + 1] = 0;
                }
                if ( buffer[i + 4] == 0 && buffer[i + 3] > 0
                    && buffer[i + 3] <= 48 ) {
                    buffer[i + 3] = 0;
                }
            }
        }
        return ret;
    }

}
