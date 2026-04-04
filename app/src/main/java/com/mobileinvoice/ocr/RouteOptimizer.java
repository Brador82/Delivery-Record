package com.mobileinvoice.ocr;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import com.mobileinvoice.ocr.RouteOptimizer;
import com.mobileinvoice.ocr.database.Invoice;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.time.DateUtils;

/* loaded from: classes7.dex */
public class RouteOptimizer {
    public static final int PRIORITY_FIRST = 1;
    public static final int PRIORITY_LAST = 2;
    public static final int PRIORITY_NORMAL = 0;
    private static final String TAG = "RouteOptimizer";
    private final Context context;
    private final Geocoder geocoder;

    public static class RoutePoint {
        public String formattedAddress;
        public Invoice invoice;
        public double latitude;
        public double longitude;
        public int orderIndex;
        public int stopTimeMinutes;
        public double distanceFromPrevious = 0.0d;
        public long etaMillis = 0;
        public int priority = 0;
        public int travelTimeMinutes = 0;

        public RoutePoint(Invoice invoice, double lat, double lng, String address) {
            this.stopTimeMinutes = 30;
            this.invoice = invoice;
            this.latitude = lat;
            this.longitude = lng;
            this.formattedAddress = address;
            this.stopTimeMinutes = invoice.getStopTimeMinutes();
        }

        public String getFormattedETA() {
            if (this.etaMillis == 0) {
                return "N/A";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
            return sdf.format(new Date(this.etaMillis));
        }

        public String getStopTimeDisplay() {
            return this.stopTimeMinutes + " min stop";
        }

        public String getPriorityText() {
            switch (this.priority) {
                case 1:
                    return "FIRST";
                case 2:
                    return "LAST";
                default:
                    return null;
            }
        }
    }

    public static class GeocodingFailure {
        public Invoice invoice;
        public String reason;

        public GeocodingFailure(Invoice invoice, String reason) {
            this.invoice = invoice;
            this.reason = reason;
        }
    }

    public static class OptimizedRoute {
        public long endTimeMillis;
        public String summary;
        public List<RoutePoint> orderedPoints = new ArrayList();
        public List<GeocodingFailure> failedInvoices = new ArrayList();
        public double totalDistance = 0.0d;
        public int totalStops = 0;
        public long startTimeMillis = System.currentTimeMillis();

        public int getTotalTimeMinutes() {
            if (this.endTimeMillis == 0 || this.startTimeMillis == 0) {
                return 0;
            }
            return (int) ((this.endTimeMillis - this.startTimeMillis) / DateUtils.MILLIS_PER_MINUTE);
        }

        public String getFormattedEndTime() {
            if (this.endTimeMillis == 0) {
                return "N/A";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
            return sdf.format(new Date(this.endTimeMillis));
        }
    }

    public RouteOptimizer(Context context) {
        this.context = context;
        this.geocoder = new Geocoder(context);
    }

    public OptimizedRoute optimizeRoute(List<Invoice> invoices, double startLatitude, double startLongitude) {
        boolean hasPersistedOrder;
        List<RoutePoint> optimizedPoints;
        Log.d(TAG, "Starting route optimization for " + invoices.size() + " invoices");
        OptimizedRoute route = new OptimizedRoute();
        List<RoutePoint> points = geocodeAddresses(invoices, route.failedInvoices);
        if (points.isEmpty()) {
            Log.w(TAG, "No valid addresses found for route optimization");
            return route;
        }
        Log.d(TAG, "Successfully geocoded " + points.size() + " addresses, " + route.failedInvoices.size() + " failed");
        Iterator<RoutePoint> it = points.iterator();
        while (true) {
            if (!it.hasNext()) {
                hasPersistedOrder = false;
                break;
            }
            RoutePoint p = it.next();
            if (p.invoice.getDeliverySequence() > 0) {
                hasPersistedOrder = true;
                break;
            }
        }
        if (hasPersistedOrder) {
            Log.d(TAG, "Using persisted route order from database");
            points.sort(new Comparator() { // from class: com.mobileinvoice.ocr.RouteOptimizer$$ExternalSyntheticLambda0
                @Override // java.util.Comparator
                public int compare(Object obj, Object obj2) {
                    return RouteOptimizer.lambda$optimizeRoute$0((RouteOptimizer.RoutePoint) obj, (RouteOptimizer.RoutePoint) obj2);
                }
            });
            for (int i = 0; i < points.size(); i++) {
                points.get(i).orderIndex = i + 1;
            }
            optimizedPoints = points;
        } else {
            optimizedPoints = nearestNeighborTSP(points, startLatitude, startLongitude);
        }
        double totalDist = calculateTotalDistance(optimizedPoints, startLatitude, startLongitude);
        route.orderedPoints = optimizedPoints;
        route.totalDistance = totalDist;
        route.totalStops = optimizedPoints.size();
        route.summary = String.format("Total: %.1f mi | %d stops", Double.valueOf(totalDist), Integer.valueOf(optimizedPoints.size()));
        Log.d(TAG, "Route optimization complete: " + route.summary);
        return route;
    }

    static /* synthetic */ int lambda$optimizeRoute$0(RoutePoint p1, RoutePoint p2) {
        int s1 = p1.invoice.getDeliverySequence();
        int s2 = p2.invoice.getDeliverySequence();
        if (s1 > 0 && s2 > 0) {
            return Integer.compare(s1, s2);
        }
        if (s1 > 0) {
            return -1;
        }
        if (s2 > 0) {
            return 1;
        }
        return 0;
    }

    private List<RoutePoint> geocodeAddresses(List<Invoice> invoices, List<GeocodingFailure> failures) {
        List<RoutePoint> points = new ArrayList<>();
        for (Invoice invoice : invoices) {
            String address = invoice.getAddress();
            if (address == null || address.trim().isEmpty()) {
                Log.w(TAG, "Skipping invoice " + invoice.getInvoiceNumber() + " - no address");
                failures.add(new GeocodingFailure(invoice, "No address provided"));
            } else if (address.equalsIgnoreCase("No address found")) {
                Log.w(TAG, "Skipping invoice " + invoice.getInvoiceNumber() + " - address not found during OCR");
                failures.add(new GeocodingFailure(invoice, "Address not detected during scan"));
            } else {
                try {
                    List<Address> addresses = this.geocoder.getFromLocationName(address, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address location = addresses.get(0);
                        double lat = location.getLatitude();
                        double lng = location.getLongitude();
                        RoutePoint point = new RoutePoint(invoice, lat, lng, address);
                        points.add(point);
                        Log.d(TAG, "Geocoded: " + invoice.getCustomerName() + " -> (" + lat + ", " + lng + ")");
                    } else {
                        Log.w(TAG, "No geocoding results for: " + address);
                        failures.add(new GeocodingFailure(invoice, "Address not recognized: " + address));
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Geocoding failed for: " + address, e);
                    failures.add(new GeocodingFailure(invoice, "Network error geocoding address"));
                }
            }
        }
        return points;
    }

    private List<RoutePoint> nearestNeighborTSP(List<RoutePoint> points, double startLat, double startLng) {
        List<RoutePoint> unvisited = new ArrayList<>(points);
        List<RoutePoint> route = new ArrayList<>();
        double currentLat = startLat;
        double currentLng = startLng;
        int order = 1;
        while (!unvisited.isEmpty()) {
            RoutePoint nearest = null;
            double minDistance = Double.MAX_VALUE;
            for (RoutePoint point : unvisited) {
                RoutePoint nearest2 = nearest;
                double dist = calculateDistance(currentLat, currentLng, point.latitude, point.longitude);
                if (dist >= minDistance) {
                    nearest = nearest2;
                } else {
                    nearest = point;
                    minDistance = dist;
                }
            }
            RoutePoint nearest3 = nearest;
            if (nearest3 != null) {
                nearest3.orderIndex = order;
                route.add(nearest3);
                unvisited.remove(nearest3);
                currentLat = nearest3.latitude;
                currentLng = nearest3.longitude;
                order++;
            }
        }
        return route;
    }

    private double calculateTotalDistance(List<RoutePoint> route, double startLat, double startLng) {
        if (route.isEmpty()) {
            return 0.0d;
        }
        double total = 0.0d + calculateDistance(startLat, startLng, route.get(0).latitude, route.get(0).longitude);
        for (int i = 0; i < route.size() - 1; i++) {
            RoutePoint from = route.get(i);
            RoutePoint to = route.get(i + 1);
            total += calculateDistance(from.latitude, from.longitude, to.latitude, to.longitude);
        }
        return total;
    }

    public static double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = (Math.sin(dLat / 2.0d) * Math.sin(dLat / 2.0d)) + (Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2.0d) * Math.sin(dLng / 2.0d));
        double c = Math.atan2(Math.sqrt(a), Math.sqrt(1.0d - a)) * 2.0d;
        return 3959.0d * c;
    }

    public static String formatDistance(double miles) {
        if (miles < 0.1d) {
            return String.format("%.0f ft", Double.valueOf(5280.0d * miles));
        }
        return String.format("%.1f mi", Double.valueOf(miles));
    }

    public static String estimateTravelTime(double miles) {
        double hours = miles / 25.0d;
        int minutes = (int) (60.0d * hours);
        if (minutes < 60) {
            return minutes + " min";
        }
        int hrs = minutes / 60;
        int mins = minutes % 60;
        return hrs + "h " + mins + "m";
    }

    public static int estimateTravelTimeMinutes(double miles) {
        double hours = miles / 25.0d;
        return Math.max(1, (int) (60.0d * hours));
    }

    public static void calculateETAs(OptimizedRoute route, double startLat, double startLng, long startTimeMillis) {
        if (route.orderedPoints.isEmpty()) {
            return;
        }
        route.startTimeMillis = startTimeMillis;
        long currentTime = startTimeMillis;
        double prevLat = startLat;
        double prevLng = startLng;
        for (RoutePoint point : route.orderedPoints) {
            point.distanceFromPrevious = calculateDistance(prevLat, prevLng, point.latitude, point.longitude);
            point.travelTimeMinutes = estimateTravelTimeMinutes(point.distanceFromPrevious);
            long currentTime2 = currentTime + ((long) point.travelTimeMinutes * 60 * 1000);
            point.etaMillis = currentTime2;
            currentTime = currentTime2 + ((long) point.stopTimeMinutes * 60 * 1000);
            double prevLat2 = point.latitude;
            prevLng = point.longitude;
            prevLat = prevLat2;
        }
        route.endTimeMillis = currentTime;
        Log.d(TAG, "ETAs calculated. Route starts at " + new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date(startTimeMillis)) + ", ends at " + route.getFormattedEndTime());
    }

    public static void recalculateETAs(OptimizedRoute route, double startLat, double startLng) {
        calculateETAs(route, startLat, startLng, route.startTimeMillis);
    }

    public static void makeFirst(List<RoutePoint> stops, RoutePoint point) {
        if (stops.remove(point)) {
            stops.add(0, point);
            point.priority = 1;
            for (int i = 0; i < stops.size(); i++) {
                stops.get(i).orderIndex = i + 1;
            }
        }
    }

    public static void makeLast(List<RoutePoint> stops, RoutePoint point) {
        if (stops.remove(point)) {
            stops.add(point);
            point.priority = 2;
            for (int i = 0; i < stops.size(); i++) {
                stops.get(i).orderIndex = i + 1;
            }
        }
    }

    public static void clearPriority(RoutePoint point) {
        point.priority = 0;
    }

    public static String formatMinutes(int minutes) {
        if (minutes < 60) {
            return minutes + " min";
        }
        int hrs = minutes / 60;
        int mins = minutes % 60;
        if (mins == 0) {
            return hrs + " hr";
        }
        return hrs + "h " + mins + "m";
    }
}
