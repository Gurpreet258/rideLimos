package com.ridelimos.ridelimos.util;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gurpreet on 28/04/17.
 */

public class Utility {

    private static final int trucateValue=63;
    private static final double ie5=1E5;
    private static final int length1f=0x1f;
    private static final int length20=0x20;


    /**
     * Decodes an encoded path string into a sequence of LatLngs.
     */
    public static List<LatLng> decodeDirectionsPolyline(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int bInt, shift = 0, result = 0;
            do {
                bInt = encoded.charAt(index++) - trucateValue;
                result |= (bInt & length1f) << shift;
                shift += 5;
            } while (bInt >= length20);
            int dlat = ((result & 1) == 0 ? (result >> 1) : ~(result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                bInt = encoded.charAt(index++) - trucateValue;
                result |= (bInt & length1f) << shift;
                shift += 5;
            } while (bInt >= length20);
            int dlng = ((result & 1) == 0 ? (result >> 1) : ~(result >> 1));
            lng += dlng;

            LatLng pLatLng = new LatLng( (((double) lat / ie5)),
                    (((double) lng / ie5) ));
            poly.add(pLatLng);
        }
        return poly;
    }
}
