package ir.mahdi.mzip.rar.util;

public class VolumeHelper {
    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private VolumeHelper() {
    }

    public static String nextVolumeName(String str, boolean z) {
        if (!z) {
            int length = str.length();
            int i = length - 1;
            while (i >= 0 && !isDigit(str.charAt(i))) {
                i--;
            }
            int i2 = i + 1;
            int i3 = i - 1;
            while (i3 >= 0 && isDigit(str.charAt(i3))) {
                i3--;
            }
            if (i3 < 0) {
                return null;
            }
            int i4 = i3 + 1;
            StringBuilder sb = new StringBuilder(length);
            sb.append(str, 0, i4);
            int i5 = (i - i4) + 1;
            char[] cArr = new char[i5];
            str.getChars(i4, i2, cArr, 0);
            int i6 = i5 - 1;
            while (i6 >= 0) {
                char c = (char) (cArr[i6] + 1);
                cArr[i6] = c;
                if (c != ':') {
                    break;
                }
                cArr[i6] = '0';
                i6--;
            }
            if (i6 < 0) {
                sb.append('1');
            }
            sb.append(cArr);
            sb.append(str, i2, length);
            return sb.toString();
        }
        int length2 = str.length();
        if (length2 <= 4 || str.charAt(length2 - 4) != '.') {
            return null;
        }
        StringBuilder sb2 = new StringBuilder();
        int i7 = length2 - 3;
        sb2.append(str, 0, i7);
        if (!isDigit(str.charAt(i7 + 1)) || !isDigit(str.charAt(i7 + 2))) {
            sb2.append("r00");
        } else {
            char[] cArr2 = new char[3];
            str.getChars(i7, length2, cArr2, 0);
            int i8 = 2;
            while (true) {
                char c2 = (char) (cArr2[i8] + 1);
                cArr2[i8] = c2;
                if (c2 != ':') {
                    break;
                }
                cArr2[i8] = '0';
                i8--;
            }
            sb2.append(cArr2);
        }
        return sb2.toString();
    }
}
