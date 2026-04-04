package com.mobileinvoice.delivery.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.mobileinvoice.delivery.data.dao.DeliveryDao;
import com.mobileinvoice.delivery.data.database.DeliveryDatabase;
import com.mobileinvoice.delivery.data.entities.Delivery;
import com.mobileinvoice.delivery.models.DeliveryStatus;
import com.mobileinvoice.delivery.models.Priority;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.util.ProcessIdUtil;

/* loaded from: classes10.dex */
public class DeliveryRepository {
    private final LiveData<List<Delivery>> activeDeliveries;
    private final LiveData<List<Delivery>> allDeliveries;
    private final DeliveryDao deliveryDao;
    private final ExecutorService executorService;
    private final LiveData<List<Delivery>> todaysDeliveries;
    private final LiveData<Integer> totalCount;

    public interface OnDeliveriesBulkInsertedListener {
        void onInserted(List<Long> ids);
    }

    public interface OnDeliveriesLoadedListener {
        void onLoaded(List<Delivery> deliveries);
    }

    public interface OnDeliveryInsertedListener {
        void onInserted(long id);
    }

    public DeliveryRepository(Application application) {
        DeliveryDatabase database = DeliveryDatabase.getInstance(application);
        this.deliveryDao = database.deliveryDao();
        this.executorService = Executors.newFixedThreadPool(4);
        this.allDeliveries = this.deliveryDao.getAllDeliveries();
        this.activeDeliveries = this.deliveryDao.getActiveDeliveries();
        this.todaysDeliveries = this.deliveryDao.getTodaysDeliveries();
        this.totalCount = this.deliveryDao.getTotalCount();
    }

    public void insert(final Delivery delivery, final OnDeliveryInsertedListener listener) {
        this.executorService.execute(new Runnable() { // from class: com.mobileinvoice.delivery.data.repository.DeliveryRepository$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                DeliveryRepository.this.lambda$insert$0(delivery, listener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$insert$0(Delivery delivery, OnDeliveryInsertedListener listener) {
        if (delivery.getTrackingNumber() == null) {
            delivery.setTrackingNumber(generateTrackingNumber());
        }
        long id = this.deliveryDao.insert(delivery);
        if (listener != null) {
            listener.onInserted(id);
        }
    }

    public void insertAll(final List<Delivery> deliveries, final OnDeliveriesBulkInsertedListener listener) {
        this.executorService.execute(new Runnable() { // from class: com.mobileinvoice.delivery.data.repository.DeliveryRepository$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                DeliveryRepository.this.lambda$insertAll$1(deliveries, listener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$insertAll$1(List deliveries, OnDeliveriesBulkInsertedListener listener) {
        Iterator it = deliveries.iterator();
        while (it.hasNext()) {
            Delivery delivery = (Delivery) it.next();
            if (delivery.getTrackingNumber() == null) {
                delivery.setTrackingNumber(generateTrackingNumber());
            }
        }
        List<Long> ids = this.deliveryDao.insertAll(deliveries);
        if (listener != null) {
            listener.onInserted(ids);
        }
    }

    public LiveData<List<Delivery>> getAllDeliveries() {
        return this.allDeliveries;
    }

    public LiveData<Delivery> getDeliveryById(long id) {
        return this.deliveryDao.getDeliveryById(id);
    }

    public LiveData<List<Delivery>> getActiveDeliveries() {
        return this.activeDeliveries;
    }

    public LiveData<List<Delivery>> getTodaysDeliveries() {
        return this.todaysDeliveries;
    }

    public LiveData<List<Delivery>> getDeliveriesByStatus(DeliveryStatus status) {
        return this.deliveryDao.getDeliveriesByStatus(status);
    }

    public LiveData<List<Delivery>> getDeliveriesByPriority(Priority priority) {
        return this.deliveryDao.getDeliveriesByPriority(priority);
    }

    public LiveData<List<Delivery>> getDeliveriesByDate(Date date) {
        return this.deliveryDao.getDeliveriesByDate(date.getTime());
    }

    public LiveData<List<Delivery>> getOverdueDeliveries() {
        return this.deliveryDao.getOverdueDeliveries(System.currentTimeMillis());
    }

    public LiveData<List<Delivery>> searchDeliveries(String query) {
        return this.deliveryDao.searchDeliveries(query);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$update$2(Delivery delivery) {
        this.deliveryDao.update(delivery);
    }

    public void update(final Delivery delivery) {
        this.executorService.execute(new Runnable() { // from class: com.mobileinvoice.delivery.data.repository.DeliveryRepository$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                DeliveryRepository.this.lambda$update$2(delivery);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateStatus$3(long id, DeliveryStatus status) {
        this.deliveryDao.updateStatus(id, status);
    }

    public void updateStatus(final long id, final DeliveryStatus status) {
        this.executorService.execute(new Runnable() { // from class: com.mobileinvoice.delivery.data.repository.DeliveryRepository$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                DeliveryRepository.this.lambda$updateStatus$3(id, status);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateRouteOrder$4(long id, int order) {
        this.deliveryDao.updateRouteOrder(id, order);
    }

    public void updateRouteOrder(final long id, final int order) {
        this.executorService.execute(new Runnable() { // from class: com.mobileinvoice.delivery.data.repository.DeliveryRepository$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                DeliveryRepository.this.lambda$updateRouteOrder$4(id, order);
            }
        });
    }

    public void completeDelivery(final long id, final String recipientName, final String signaturePath) {
        this.executorService.execute(new Runnable() { // from class: com.mobileinvoice.delivery.data.repository.DeliveryRepository$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                DeliveryRepository.this.lambda$completeDelivery$5(id, signaturePath, recipientName);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$completeDelivery$5(long id, String signaturePath, String recipientName) {
        Date now = new Date();
        this.deliveryDao.completeDelivery(id, DeliveryStatus.DELIVERED, now, now, signaturePath, recipientName);
    }

    public void markDeliveryFailed(final long id, final String reason) {
        this.executorService.execute(new Runnable() { // from class: com.mobileinvoice.delivery.data.repository.DeliveryRepository$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                DeliveryRepository.this.lambda$markDeliveryFailed$6(id, reason);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$markDeliveryFailed$6(long id, String reason) {
        this.deliveryDao.markDeliveryFailed(id, DeliveryStatus.FAILED, reason);
    }

    public void updateRouteOrders(final List<Delivery> deliveries) {
        this.executorService.execute(new Runnable() { // from class: com.mobileinvoice.delivery.data.repository.DeliveryRepository$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                DeliveryRepository.this.lambda$updateRouteOrders$7(deliveries);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateRouteOrders$7(List deliveries) {
        for (int i = 0; i < deliveries.size(); i++) {
            this.deliveryDao.updateRouteOrder(((Delivery) deliveries.get(i)).getId(), i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$delete$8(Delivery delivery) {
        this.deliveryDao.delete(delivery);
    }

    public void delete(final Delivery delivery) {
        this.executorService.execute(new Runnable() { // from class: com.mobileinvoice.delivery.data.repository.DeliveryRepository$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                DeliveryRepository.this.lambda$delete$8(delivery);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteById$9(long id) {
        this.deliveryDao.deleteById(id);
    }

    public void deleteById(final long id) {
        this.executorService.execute(new Runnable() { // from class: com.mobileinvoice.delivery.data.repository.DeliveryRepository$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                DeliveryRepository.this.lambda$deleteById$9(id);
            }
        });
    }

    public void deleteCompleted() {
        this.executorService.execute(new Runnable() { // from class: com.mobileinvoice.delivery.data.repository.DeliveryRepository$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                DeliveryRepository.this.lambda$deleteCompleted$10();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteCompleted$10() {
        this.deliveryDao.deleteByStatus(DeliveryStatus.DELIVERED);
        this.deliveryDao.deleteByStatus(DeliveryStatus.CANCELLED);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteAll$11() {
        this.deliveryDao.deleteAll();
    }

    public void deleteAll() {
        this.executorService.execute(new Runnable() { // from class: com.mobileinvoice.delivery.data.repository.DeliveryRepository$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                DeliveryRepository.this.lambda$deleteAll$11();
            }
        });
    }

    public LiveData<Integer> getTotalCount() {
        return this.totalCount;
    }

    public LiveData<Integer> getCountByStatus(DeliveryStatus status) {
        return this.deliveryDao.getCountByStatus(status);
    }

    public LiveData<Integer> getTodaysCount() {
        return this.deliveryDao.getTodaysCount();
    }

    public LiveData<Integer> getOverdueCount() {
        return this.deliveryDao.getOverdueCount(System.currentTimeMillis());
    }

    private String generateTrackingNumber() {
        long timestamp = System.currentTimeMillis();
        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date(timestamp));
        String random = String.format("%05d", Integer.valueOf((int) (Math.random() * 100000.0d)));
        return "DEL-" + dateStr + ProcessIdUtil.DEFAULT_PROCESSID + random;
    }

    public void getActiveDeliveriesSync(final OnDeliveriesLoadedListener listener) {
        this.executorService.execute(new Runnable() { // from class: com.mobileinvoice.delivery.data.repository.DeliveryRepository$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                DeliveryRepository.this.lambda$getActiveDeliveriesSync$12(listener);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getActiveDeliveriesSync$12(OnDeliveriesLoadedListener listener) {
        List<Delivery> deliveries = this.deliveryDao.getActiveDeliveriesSync();
        if (listener != null) {
            listener.onLoaded(deliveries);
        }
    }
}
