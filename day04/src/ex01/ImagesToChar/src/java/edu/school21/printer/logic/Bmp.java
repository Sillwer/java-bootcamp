package edu.school21.printer.logic;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

public class Bmp implements ConsolePrintable {
    private static final int INFO_HEADER_SIZE = 40;
    private static final int HEADER_SIZE = 14 + INFO_HEADER_SIZE;
    private static final int COLOR_TABLE_SIZE = 8;
    private static final char BMP_SIGNATURE = 0x424d;
    private ByteBuffer header = null;
    private ByteBuffer data = null;
    private char blackColorChar = 'O';
    private char whiteColorChar = '.';

    @Override
    public void load(InputStream inputStream) throws IOException, SecurityException {
        byte[] headerBytes = new byte[HEADER_SIZE];
        if (inputStream.read(headerBytes) != HEADER_SIZE) {
            throw new IOException("Problem with BMP header reading");
        }
        header = ByteBuffer.wrap(headerBytes).order(ByteOrder.LITTLE_ENDIAN);
        if (Objects.requireNonNull(getSignature()).compareTo(BMP_SIGNATURE) != 0) {
            throw new IllegalArgumentException(String.format("Unexpected file signature for BMP file: %x", (int) getSignature()));
        } else if (getInfoHeaderSize() != INFO_HEADER_SIZE) {
            throw new IllegalArgumentException(String.format("BMP file have unexpected size of info header: %d", getInfoHeaderSize()));
        } else if (getBitCount() != 1) {
            throw new IllegalArgumentException(String.format("Unsupported color depth: %d", getBitCount()));
        }

        boolean dataReadOk = (inputStream.skip(COLOR_TABLE_SIZE) == COLOR_TABLE_SIZE);
        if (dataReadOk) {
            int dataSize = getFileSize() - HEADER_SIZE - COLOR_TABLE_SIZE;
            byte[] dataByte = new byte[dataSize];
            dataReadOk = (inputStream.read(dataByte) == dataSize);
            data = ByteBuffer.wrap(dataByte).order(ByteOrder.LITTLE_ENDIAN);
        }

        if (!dataReadOk) {
            throw new IOException("Problem with BMP data reading");
        }
    }

    @Override
    public void printInfo() {
        if (header == null) {
            System.out.println("File not loaded yet");
            return;
        }

        try {
            System.out.printf("\t- Signature: %x\n", (int) getSignature());
            System.out.println("\t- size of file: " + getFileSize());
            System.out.println("\t- size of info header: " + getInfoHeaderSize());
            System.out.println("\t- data offset: " + getDataOffset());
            System.out.println("\t- bit count (color depth): " + getBitCount());
            System.out.printf("\t- image resolution: %dx%d\n", getWidth(), getHeight());
        } catch (Exception ignore) {
        }
    }

    @Override
    public void setPixelChar(String str) {
        if (str.length() != 2) {
            System.out.println("Unexpected colors count: " + str.length());
            return;
        } else if (str.charAt(0) == str.charAt(1)) {
            System.out.println("Colors should be different");
            return;
        }

        blackColorChar = str.charAt(0);
        whiteColorChar = str.charAt(1);
    }

    @Override
    public void print() {
        int bytesCount = Math.round((float) getWidth() / 8);
        int alignBytesCount = (bytesCount % 4 == 0) ? 0 : 4 - (bytesCount % 4);
        int rowWidth = bytesCount + alignBytesCount;
        int i = data.array().length - rowWidth;

        for (int j = 1; i >= 0 && i < data.array().length; i++) {
            byte b = data.get(i);
            boolean[] bitSet = {
                    (b & (1 << 7)) == 0,
                    (b & (1 << 6)) == 0,
                    (b & (1 << 5)) == 0,
                    (b & (1 << 4)) == 0,

                    (b & (1 << 3)) == 0,
                    (b & (1 << 2)) == 0,
                    (b & (1 << 1)) == 0,
                    (b & (1)) == 0,
            };

            for (boolean whitePixel : bitSet) {
                if (whitePixel) {
                    System.out.print(blackColorChar);
                } else {
                    System.out.print(whiteColorChar);
                }

                if (j == getWidth()) {
                    j = 1;
                    System.out.println();
                    i -= (rowWidth + bytesCount);
                    break;
                } else {
                    j++;
                }
            }
        }
    }

    private Character getSignature() {
        if (header == null) {
            return null;
        }
        byte c1 = header.get(0x00);
        byte c2 = header.get(0x01);
        return (char) ((c1 << 8) | (c2));
    }

    private int getFileSize() {
        return header == null ?
                -1 : header.getInt(0x02);
    }

    private int getInfoHeaderSize() {
        return header == null ?
                -1 : header.getInt(0x0e);
    }

    private int getDataOffset() {
        return header == null ?
                -1 : header.getInt(0x0a);
    }

    private int getBitCount() {
        return header == null ?
                -1 : (int) header.get(0x1c);
    }

    private int getHeight() {
        return header == null ?
                -1 : header.getInt(0x16);
    }

    private int getWidth() {
        return header == null ?
                -1 : header.getInt(0x12);
    }
}