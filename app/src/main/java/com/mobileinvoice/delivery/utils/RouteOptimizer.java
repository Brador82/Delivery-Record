package com.mobileinvoice.delivery.utils;

import com.mobileinvoice.delivery.data.entities.Delivery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes8.dex */
public class RouteOptimizer {
    public static List<Delivery> optimizeByNearestNeighbor(List<Delivery> deliveries, double startLat, double startLng) {
        if (deliveries == null || deliveries.isEmpty()) {
            return new ArrayList();
        }
        List<Delivery> optimized = new ArrayList<>();
        List<Delivery> remaining = new ArrayList<>(deliveries);
        double currentLat = startLat;
        double currentLng = startLng;
        while (!remaining.isEmpty()) {
            Delivery nearest = findNearest(remaining, currentLat, currentLng);
            optimized.add(nearest);
            remaining.remove(nearest);
            if (nearest.hasCoordinates()) {
                currentLat = nearest.getLatitude().doubleValue();
                currentLng = nearest.getLongitude().doubleValue();
            }
        }
        for (int i = 0; i < optimized.size(); i++) {
            optimized.get(i).setRouteOrder(i);
        }
        return optimized;
    }

    public static List<Delivery> optimizeByTimeWindow(List<Delivery> deliveries) {
        if (deliveries == null || deliveries.isEmpty()) {
            return new ArrayList();
        }
        List<Delivery> optimized = new ArrayList<>(deliveries);
        optimized.sort(new Comparator() { // from class: com.mobileinvoice.delivery.utils.RouteOptimizer$$ExternalSyntheticLambda1
            @Override // java.util.Comparator
            public int compare(Object obj, Object obj2) {
                return RouteOptimizer.lambda$optimizeByTimeWindow$0((Delivery) obj, (Delivery) obj2);
            }
        });
        for (int i = 0; i < optimized.size(); i++) {
            optimized.get(i).setRouteOrder(i);
        }
        return optimized;
    }

    static /* synthetic */ int lambda$optimizeByTimeWindow$0(Delivery d1, Delivery d2) {
        boolean d1HasWindow = d1.getTimeWindowStart() != null;
        boolean d2HasWindow = d2.getTimeWindowStart() != null;
        if (d1HasWindow && !d2HasWindow) {
            return -1;
        }
        if (!d1HasWindow && d2HasWindow) {
            return 1;
        }
        if (d1HasWindow && d2HasWindow) {
            return d1.getTimeWindowStart().compareTo(d2.getTimeWindowStart());
        }
        return Integer.compare(d2.getPriority().getValue(), d1.getPriority().getValue());
    }

    public static List<Delivery> optimizeByPriority(List<Delivery> deliveries) {
        if (deliveries == null || deliveries.isEmpty()) {
            return new ArrayList();
        }
        List<Delivery> optimized = new ArrayList<>(deliveries);
        optimized.sort(new Comparator() { // from class: com.mobileinvoice.delivery.utils.RouteOptimizer$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public int compare(Object obj, Object obj2) {
                return RouteOptimizer.lambda$optimizeByPriority$1((Delivery) obj, (Delivery) obj2);
            }
        });
        for (int i = 0; i < optimized.size(); i++) {
            optimized.get(i).setRouteOrder(i);
        }
        return optimized;
    }

    static /* synthetic */ int lambda$optimizeByPriority$1(Delivery d1, Delivery d2) {
        int priorityCompare = Integer.compare(d2.getPriority().getValue(), d1.getPriority().getValue());
        if (priorityCompare != 0) {
            return priorityCompare;
        }
        if (d1.getScheduledDate() != null && d2.getScheduledDate() != null) {
            return d1.getScheduledDate().compareTo(d2.getScheduledDate());
        }
        return 0;
    }

    public static List<Delivery> optimizeSmart(List<Delivery> deliveries, double startLat, double startLng) {
        if (deliveries == null || deliveries.isEmpty()) {
            return new ArrayList();
        }
        List<Delivery> urgent = new ArrayList<>();
        List<Delivery> normal = new ArrayList<>();
        for (Delivery delivery : deliveries) {
            if (delivery.getPriority().getValue() >= 3) {
                urgent.add(delivery);
            } else {
                normal.add(delivery);
            }
        }
        Collection<? extends Delivery> optimizedUrgent = optimizeByTimeWindow(urgent);
        Collection<? extends Delivery> optimizedNormal = optimizeByNearestNeighbor(normal, startLat, startLng);
        List<Delivery> result = new ArrayList<>();
        result.addAll(optimizedUrgent);
        result.addAll(optimizedNormal);
        for (int i = 0; i < result.size(); i++) {
            result.get(i).setRouteOrder(i);
        }
        return result;
    }

    public static double calculateTotalDistance(List<Delivery> deliveries, double startLat, double startLng) {
        if (deliveries == null || deliveries.isEmpty()) {
            return 0.0d;
        }
        double totalDistance = 0.0d;
        double currentLat = startLat;
        double currentLng = startLng;
        for (Delivery delivery : deliveries) {
            if (delivery.hasCoordinates()) {
                double distance = calculateDistance(currentLat, currentLng, delivery.getLatitude().doubleValue(), delivery.getLongitude().doubleValue());
                totalDistance += distance;
                currentLat = delivery.getLatitude().doubleValue();
                currentLng = delivery.getLongitude().doubleValue();
            }
        }
        return totalDistance;
    }

    private static Delivery findNearest(List<Delivery> deliveries, double lat, double lng) {
        Delivery nearest = deliveries.get(0);
        double minDistance = Double.MAX_VALUE;
        for (Delivery delivery : deliveries) {
            if (delivery.hasCoordinates()) {
                double distance = calculateDistance(lat, lng, delivery.getLatitude().doubleValue(), delivery.getLongitude().doubleValue());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = delivery;
                }
            }
        }
        return nearest;
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = (Math.sin(latDistance / 2.0d) * Math.sin(latDistance / 2.0d)) + (Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2.0d) * Math.sin(lonDistance / 2.0d));
        double c = Math.atan2(Math.sqrt(a), Math.sqrt(1.0d - a)) * 2.0d;
        return 6371.0d * c;
    }

    public static int estimateDeliveryTimeMinutes(double distanceKm) {
        double travelTimeMinutes = (distanceKm / 40.0d) * 60.0d;
        return ((int) Math.ceil(travelTimeMinutes)) + 5;
    }
}
