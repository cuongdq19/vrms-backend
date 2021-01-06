package org.lordrose.vrms.utils.distances;

import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class DistanceCalculator {

    private static final double earthRadius = 6371e3; // metres

    public static double calculate(GeoPoint from, GeoPoint to) {
        double lat1 = toRadian(from.getLatitude()); // lon and lat are in radians
        double lat2 = toRadian(to.getLatitude());
        double lon1 = toRadian(from.getLongitude());
        double lon2 = toRadian(to.getLongitude());

        double havC = sin((lat2 - lat1)/2) * sin((lat2 - lat1)/2) +
                cos(lat1) * cos(lat2) * sin((lon2 - lon1)/2) * sin((lon2 - lon1)/2);

        return 2 * earthRadius * asin(sqrt(havC));
    }

    private static double toRadian(double value) {
        return value * PI / 180;
    }
}
