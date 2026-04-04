package com.mobileinvoice.ocr;

import com.mobileinvoice.ocr.RouteOptimizer;

/* loaded from: classes7.dex */
public class RouteListItem {
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_STOP = 0;
    private int completedCount;
    private String headerTitle;
    private boolean isExpanded;
    private RouteOptimizer.RoutePoint routePoint;
    private int type;

    private RouteListItem() {
    }

    public static RouteListItem createStopItem(RouteOptimizer.RoutePoint point) {
        RouteListItem item = new RouteListItem();
        item.type = 0;
        item.routePoint = point;
        return item;
    }

    public static RouteListItem createHeaderItem(int count, boolean expanded) {
        RouteListItem item = new RouteListItem();
        item.type = 1;
        item.headerTitle = "Completed";
        item.completedCount = count;
        item.isExpanded = expanded;
        return item;
    }

    public int getType() {
        return this.type;
    }

    public RouteOptimizer.RoutePoint getRoutePoint() {
        return this.routePoint;
    }

    public String getHeaderTitle() {
        return this.headerTitle;
    }

    public int getCompletedCount() {
        return this.completedCount;
    }

    public boolean isExpanded() {
        return this.isExpanded;
    }

    public void setExpanded(boolean expanded) {
        this.isExpanded = expanded;
    }
}
