package com.mobileinvoice.delivery.data.dao;

import androidx.lifecycle.LiveData;
import com.mobileinvoice.delivery.data.entities.Delivery;
import com.mobileinvoice.delivery.models.DeliveryStatus;
import com.mobileinvoice.delivery.models.Priority;
import java.util.Date;
import java.util.List;

/* loaded from: classes4.dex */
public interface DeliveryDao {
    int completeDelivery(long id, DeliveryStatus status, Date completedAt, Date actualArrival, String signaturePath, String recipientName);

    int delete(Delivery delivery);

    int deleteAll();

    int deleteById(long id);

    int deleteByStatus(DeliveryStatus status);

    LiveData<List<Delivery>> getActiveDeliveries();

    List<Delivery> getActiveDeliveriesSync();

    LiveData<List<Delivery>> getAllDeliveries();

    List<Delivery> getAllDeliveriesSync();

    LiveData<Integer> getCountByStatus(DeliveryStatus status);

    LiveData<List<Delivery>> getDeliveriesByDate(long date);

    LiveData<List<Delivery>> getDeliveriesByPriority(Priority priority);

    LiveData<List<Delivery>> getDeliveriesByStatus(DeliveryStatus status);

    LiveData<List<Delivery>> getDeliveriesByStatuses(List<DeliveryStatus> statuses);

    LiveData<List<Delivery>> getDeliveriesInDateRange(Date startDate, Date endDate);

    LiveData<Delivery> getDeliveryById(long id);

    Delivery getDeliveryByIdSync(long id);

    LiveData<Delivery> getDeliveryByTrackingNumber(String trackingNumber);

    LiveData<Integer> getOverdueCount(long currentTime);

    LiveData<List<Delivery>> getOverdueDeliveries(long currentTime);

    LiveData<Integer> getTodaysCount();

    LiveData<List<Delivery>> getTodaysDeliveries();

    LiveData<Integer> getTotalCount();

    long insert(Delivery delivery);

    List<Long> insertAll(List<Delivery> deliveries);

    int markDeliveryFailed(long id, DeliveryStatus status, String reason);

    LiveData<List<Delivery>> searchDeliveries(String query);

    int update(Delivery delivery);

    int updateRouteOrder(long id, int order);

    int updateStatus(long id, DeliveryStatus status);
}
