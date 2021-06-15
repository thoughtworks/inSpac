package com.thoughtworks.provider.inspac.utils;

import java.util.Arrays;

public class StringUtils {
  public static String rightPad(String str, int size, String padStr) {
    if (str == null) {
      return null;
    } else {
      if (isEmpty(padStr)) {
        padStr = " ";
      }

      int padLen = padStr.length();
      int strLen = str.length();
      int pads = size - strLen;
      if (pads <= 0) {
        return str;
      } else if (padLen == 1 && pads <= 8192) {
        return rightPad(str, size, padStr.charAt(0));
      } else if (pads == padLen) {
        return str.concat(padStr);
      } else if (pads < padLen) {
        return str.concat(padStr.substring(0, pads));
      } else {
        char[] padding = new char[pads];
        char[] padChars = padStr.toCharArray();

        for(int i = 0; i < pads; ++i) {
          padding[i] = padChars[i % padLen];
        }

        return str.concat(new String(padding));
      }
    }
  }

  private static String rightPad(String str, int size, char padChar) {
    if (str == null) {
      return null;
    } else {
      int pads = size - str.length();
      if (pads <= 0) {
        return str;
      } else {
        return pads > 8192 ? rightPad(str, size, String.valueOf(padChar)) : str.concat(padding(pads, padChar));
      }
    }
  }

  private static boolean isEmpty(String str) {
    return str == null || str.length() == 0;
  }

  private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
    if (repeat < 0) {
      throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
    } else {
      char[] buf = new char[repeat];

      Arrays.fill(buf, padChar);

      return new String(buf);
    }
  }
}
