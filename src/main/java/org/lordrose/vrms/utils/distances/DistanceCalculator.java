package org.lordrose.vrms.utils.distances;

public class DistanceCalculator {

    private static final double earthRadius = 6371e3; // metres

    public static double calculate(GeoPoint from, GeoPoint to) {
        double latitude_1 = toRadian(from.getLatitude()); // lon and lat are in radians
        double latitude_2 = toRadian(to.getLatitude());
        double deltaLat = calculateDelta(to.getLatitude(), from.getLatitude());
        double deltaLon = calculateDelta(to.getLongitude(), from.getLongitude());
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(latitude_1) * Math.cos(latitude_2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    private static double toRadian(double value) {
        return value * Math.PI/180;
    }

    private static double calculateDelta(double a, double b) {
        return (a - b) * Math.PI / 180;
    }
}
