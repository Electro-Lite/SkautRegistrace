package CZ.Scout.PrvniSmeckaOlomouc.Registrace.GroupSetup;

import java.awt.Color;

/**
 * Utility class for converting between HEX and HSL color formats.
 */
public class ColorUtils {

	/**
     * Converts a HEX color string to an HSL array.
     *
     * @param hex the HEX color string (e.g., "#ff0000")
     * @return float array representing HSL values (hue, saturation, lightness)
     */
    public static float[] hexToHsl(String hex) {
        Color color = Color.decode(hex);
        float[] rgb = new float[] {
            color.getRed() / 255f,
            color.getGreen() / 255f,
            color.getBlue() / 255f
        };
        return rgbToHsl(rgb);
    }

    /**
     * Converts an HSL array to a HEX color string.
     *
     * @param hsl float array with hue, saturation, and lightness
     * @return HEX color string (e.g., "#ff0000")
     */
    public static String hslToHex(float[] hsl) {
        float[] rgb = hslToRgb(hsl);
        int r = Math.round(rgb[0] * 255);
        int g = Math.round(rgb[1] * 255);
        int b = Math.round(rgb[2] * 255);
        return String.format("#%02x%02x%02x", r, g, b);
    }

    /**
     * Converts RGB values to HSL.
     *
     * @param rgb float array with red, green, and blue values
     * @return float array with HSL values
     */
    private static float[] rgbToHsl(float[] rgb) {
        float r = rgb[0], g = rgb[1], b = rgb[2];
        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float h, s, l;

        l = (max + min) / 2f;

        if (max == min) {
            h = s = 0f; // achromatic
        } else {
            float d = max - min;
            s = l > 0.5f ? d / (2f - max - min) : d / (max + min);
            if (max == r) {
                h = (g - b) / d + (g < b ? 6f : 0f);
            } else if (max == g) {
                h = (b - r) / d + 2f;
            } else {
                h = (r - g) / d + 4f;
            }
            h /= 6f;
        }
        return new float[] { h, s, l };
    }

    /**
     * Converts HSL values to RGB.
     *
     * @param hsl float array with HSL values
     * @return float array with RGB values
     */
    private static float[] hslToRgb(float[] hsl) {
        float h = hsl[0], s = hsl[1], l = hsl[2];
        float r, g, b;

        if (s == 0f) {
            r = g = b = l; // achromatic
        } else {
            float q = l < 0.5f ? l * (1f + s) : l + s - l * s;
            float p = 2f * l - q;
            r = hueToRgb(p, q, h + 1f / 3f);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 1f / 3f);
        }

        return new float[] { r, g, b };
    }

    /**
     * Helper method for HSL to RGB conversion.
     *
     * @param p intermediate value
     * @param q intermediate value
     * @param t hue component
     * @return RGB component value
     */
    private static float hueToRgb(float p, float q, float t) {
        if (t < 0f) t += 1f;
        if (t > 1f) t -= 1f;
        if (t < 1f / 6f) return p + (q - p) * 6f * t;
        if (t < 1f / 2f) return q;
        if (t < 2f / 3f) return p + (q - p) * (2f / 3f - t) * 6f;
        return p;
    }
}
