package com.mobileinvoice.delivery.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.mobileinvoice.delivery.data.entities.Delivery;
import com.mobileinvoice.delivery.models.DeliveryStatus;
import com.mobileinvoice.delivery.models.Priority;
import java.util.Date;
import java.util.List;

@Dao
public interface DeliveryDao {
    @Query("UPDATE deliveries SET status = :status, completedAt = :completedAt, actualArrival = :actualArrival, signaturePath = :signaturePath, recipientName = :recipientName WHERE id = :id")
    int completeDelivery(long id, DeliveryStatus status, Date completedAt, Date actualArrival, String signaturePath, String recipientName);

    @Delete
    int delete(Delivery delivery);

    @Query("DELETE FROM deliveries")
    int deleteAll();

    @Query("DELETE FROM deliveries WHERE id = :id")
    int deleteById(long id);

    @Query("DELETE FROM deliveries WHERE status = :status")
    int deleteByStatus(DeliveryStatus status);

    @Query("SELECT * FROM deliveries WHERE status NOT IN ('DELIVERED', 'FAILED', 'CANCELLED') ORDER BY routeOrder ASC")
    LiveData<List<Delivery>> getActiveDeliveries();

    @Query("SELECT * FROM deliveries WHERE status NOT IN ('DELIVERED', 'FAILED', 'CANCELLED') ORDER BY routeOrder ASC")
    List<Delivery> getActiveDeliveriesSync();

    @Query("SELECT * FROM deliveries ORDER BY createdAt DESC")
    LiveData<List<Delivery>> getAllDeliveries();

    @Query("SELECT * FROM deliveries ORDER BY createdAt DESC")
    List<Delivery> getAllDeliveriesSync();

    @Query("SELECT COUNT(*) FROM deliveries WHERE status = :status")
    LiveData<Integer> getCountByStatus(DeliveryStatus status);

    @Query("SELECT * FROM deliveries WHERE scheduledDate = :date ORDER BY routeOrder ASC")
    LiveData<List<Delivery>> getDeliveriesByDate(long date);

    @Query("SELECT * FROM deliveries WHERE priority = :priority ORDER BY createdAt DESC")
    LiveData<List<Delivery>> getDeliveriesByPriority(Priority priority);

    @Query("SELECT * FROM deliveries WHERE status = :status ORDER BY createdAt DESC")
    LiveData<List<Delivery>> getDeliveriesByStatus(DeliveryStatus status);

    @Query("SELECT * FROM deliveries WHERE status IN (:statuses) ORDER BY createdAt DESC")
    LiveData<List<Delivery>> getDeliveriesByStatuses(List<DeliveryStatus> statuses);

    @Query("SELECT * FROM deliveries WHERE scheduledDate BETWEEN :startDate AND :endDate ORDER BY scheduledDate ASC")
    LiveData<List<Delivery>> getDeliveriesInDateRange(Date startDate, Date endDate);

    @Query("SELECT * FROM deliveries WHERE id = :id")
    LiveData<Delivery> getDeliveryById(long id);

    @Query("SELECT * FROM deliveries WHERE id = :id")
    Delivery getDeliveryByIdSync(long id);

    @Query("SELECT * FROM deliveries WHERE trackingNumber = :trackingNumber")
    LiveData<Delivery> getDeliveryByTrackingNumber(String trackingNumber);

    @Query("SELECT COUNT(*) FROM deliveries WHERE scheduledDate < :currentTime AND status NOT IN ('DELIVERED', 'FAILED', 'CANCELLED')")
    LiveData<Integer> getOverdueCount(long currentTime);

    @Query("SELECT * FROM deliveries WHERE scheduledDate < :currentTime AND status NOT IN ('DELIVERED', 'FAILED', 'CANCELLED') ORDER BY scheduledDate ASC")
    LiveData<List<Delivery>> getOverdueDeliveries(long currentTime);

    @Query("SELECT COUNT(*) FROM deliveries WHERE date(scheduledDate / 1000, 'unixepoch', 'localtime') = date('now', 'localtime')")
    LiveData<Integer> getTodaysCount();

    @Query("SELECT * FROM deliveries WHERE date(scheduledDate / 1000, 'unixepoch', 'localtime') = date('now', 'localtime') ORDER BY routeOrder ASC")
    LiveData<List<Delivery>> getTodaysDeliveries();

    @Query("SELECT COUNT(*) FROM deliveries")
    LiveData<Integer> getTotalCount();

    @Insert
    long insert(Delivery delivery);

    @Insert
    List<Long> insertAll(List<Delivery> deliveries);

    @Query("UPDATE deliveries SET status = :status, failureReason = :reason WHERE id = :id")
    int markDeliveryFailed(long id, DeliveryStatus status, String reason);

    @Query("SELECT * FROM deliveries WHERE customerName LIKE '%' || :query || '%' OR trackingNumber LIKE '%' || :query || '%' OR streetAddress LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    LiveData<List<Delivery>> searchDeliveries(String query);

    @Update
    int update(Delivery delivery);

    @Query("UPDATE deliveries SET routeOrder = :order WHERE id = :id")
    int updateRouteOrder(long id, int order);

    @Query("UPDATE deliveries SET status = :status WHERE id = :id")
    int updateStatus(long id, DeliveryStatus status);
}
