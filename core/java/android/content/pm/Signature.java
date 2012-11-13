/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.content.pm;

<<<<<<< HEAD
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayInputStream;
import java.lang.ref.SoftReference;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
=======
import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;

>>>>>>> 54b6cfa... Initial Contribution
import java.util.Arrays;

/**
 * Opaque, immutable representation of a signature associated with an
 * application package.
 */
public class Signature implements Parcelable {
    private final byte[] mSignature;
    private int mHashCode;
    private boolean mHaveHashCode;
<<<<<<< HEAD
    private SoftReference<String> mStringRef;
=======
    private String mString;
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * Create Signature from an existing raw byte array.
     */
    public Signature(byte[] signature) {
        mSignature = signature.clone();
    }

<<<<<<< HEAD
    private static final int parseHexDigit(int nibble) {
        if ('0' <= nibble && nibble <= '9') {
            return nibble - '0';
        } else if ('a' <= nibble && nibble <= 'f') {
            return nibble - 'a' + 10;
        } else if ('A' <= nibble && nibble <= 'F') {
            return nibble - 'A' + 10;
        } else {
            throw new IllegalArgumentException("Invalid character " + nibble + " in hex string");
        }
    }

    /**
     * Create Signature from a text representation previously returned by
     * {@link #toChars} or {@link #toCharsString()}. Signatures are expected to
     * be a hex-encoded ASCII string.
     *
     * @param text hex-encoded string representing the signature
     * @throws IllegalArgumentException when signature is odd-length
     */
    public Signature(String text) {
        final byte[] input = text.getBytes();
        final int N = input.length;

        if (N % 2 != 0) {
            throw new IllegalArgumentException("text size " + N + " is not even");
        }

        final byte[] sig = new byte[N / 2];
        int sigIndex = 0;

        for (int i = 0; i < N;) {
            final int hi = parseHexDigit(input[i++]);
            final int lo = parseHexDigit(input[i++]);
            sig[sigIndex++] = (byte) ((hi << 4) | lo);
        }

=======
    /**
     * Create Signature from a text representation previously returned by
     * {@link #toChars} or {@link #toCharsString()}.
     */
    public Signature(String text) {
        final int N = text.length()/2;
        byte[] sig = new byte[N];
        for (int i=0; i<N; i++) {
            char c = text.charAt(i*2);
            byte b = (byte)(
                    (c >= 'a' ? (c - 'a' + 10) : (c - '0'))<<4);
            c = text.charAt(i*2 + 1);
            b |= (byte)(c >= 'a' ? (c - 'a' + 10) : (c - '0'));
            sig[i] = b;
        }
>>>>>>> 54b6cfa... Initial Contribution
        mSignature = sig;
    }

    /**
     * Encode the Signature as ASCII text.
     */
    public char[] toChars() {
        return toChars(null, null);
    }

    /**
     * Encode the Signature as ASCII text in to an existing array.
     *
     * @param existingArray Existing char array or null.
     * @param outLen Output parameter for the number of characters written in
     * to the array.
     * @return Returns either <var>existingArray</var> if it was large enough
     * to hold the ASCII representation, or a newly created char[] array if
     * needed.
     */
    public char[] toChars(char[] existingArray, int[] outLen) {
        byte[] sig = mSignature;
        final int N = sig.length;
        final int N2 = N*2;
        char[] text = existingArray == null || N2 > existingArray.length
                ? new char[N2] : existingArray;
        for (int j=0; j<N; j++) {
            byte v = sig[j];
            int d = (v>>4)&0xf;
            text[j*2] = (char)(d >= 10 ? ('a' + d - 10) : ('0' + d));
            d = v&0xf;
            text[j*2+1] = (char)(d >= 10 ? ('a' + d - 10) : ('0' + d));
        }
        if (outLen != null) outLen[0] = N;
        return text;
    }

    /**
<<<<<<< HEAD
     * Return the result of {@link #toChars()} as a String.
     */
    public String toCharsString() {
        String str = mStringRef == null ? null : mStringRef.get();
        if (str != null) {
            return str;
        }
        str = new String(toChars());
        mStringRef = new SoftReference<String>(str);
        return str;
=======
     * Return the result of {@link #toChars()} as a String.  This result is
     * cached so future calls will return the same String.
     */
    public String toCharsString() {
        if (mString != null) return mString;
        String str = new String(toChars());
        mString = str;
        return mString;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * @return the contents of this signature as a byte array.
     */
    public byte[] toByteArray() {
        byte[] bytes = new byte[mSignature.length];
        System.arraycopy(mSignature, 0, bytes, 0, mSignature.length);
        return bytes;
    }

<<<<<<< HEAD
    /**
     * Returns the public key for this signature.
     *
     * @throws CertificateException when Signature isn't a valid X.509
     *             certificate; shouldn't happen.
     * @hide
     */
    public PublicKey getPublicKey() throws CertificateException {
        final CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        final ByteArrayInputStream bais = new ByteArrayInputStream(mSignature);
        final Certificate cert = certFactory.generateCertificate(bais);
        return cert.getPublicKey();
    }

=======
>>>>>>> 54b6cfa... Initial Contribution
    @Override
    public boolean equals(Object obj) {
        try {
            if (obj != null) {
                Signature other = (Signature)obj;
<<<<<<< HEAD
                return this == other || Arrays.equals(mSignature, other.mSignature);
=======
                return Arrays.equals(mSignature, other.mSignature);
>>>>>>> 54b6cfa... Initial Contribution
            }
        } catch (ClassCastException e) {
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (mHaveHashCode) {
            return mHashCode;
        }
        mHashCode = Arrays.hashCode(mSignature);
        mHaveHashCode = true;
        return mHashCode;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeByteArray(mSignature);
    }

    public static final Parcelable.Creator<Signature> CREATOR
            = new Parcelable.Creator<Signature>() {
        public Signature createFromParcel(Parcel source) {
            return new Signature(source);
        }

        public Signature[] newArray(int size) {
            return new Signature[size];
        }
    };

    private Signature(Parcel source) {
        mSignature = source.createByteArray();
    }
}
