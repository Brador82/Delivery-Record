package com.mobileinvoice.delivery.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.mobileinvoice.delivery.data.entities.Delivery;
import com.mobileinvoice.delivery.data.repository.DeliveryRepository;
import com.mobileinvoice.delivery.models.DeliveryStatus;
import com.mobileinvoice.delivery.models.Priority;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* loaded from: classes3.dex */
public class DeliveryViewModel extends AndroidViewModel {
    private final LiveData<List<Delivery>> activeDeliveries;
    private final LiveData<List<Delivery>> allDeliveries;
    private final MutableLiveData<String> errorMessage;
    private final MediatorLiveData<List<Delivery>> filteredDeliveries;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<Priority> priorityFilter;
    private final DeliveryRepository repository;
    private final MutableLiveData<String> searchQuery;
    private final MutableLiveData<DeliveryStatus> statusFilter;
    private final LiveData<List<Delivery>> todaysDeliveries;
    private final LiveData<Integer> totalCount;

    public DeliveryViewModel(Application application) {
        super(application);
        this.statusFilter = new MutableLiveData<>();
        this.priorityFilter = new MutableLiveData<>();
        this.searchQuery = new MutableLiveData<>();
        this.filteredDeliveries = new MediatorLiveData<>();
        this.isLoading = new MutableLiveData<>(false);
        this.errorMessage = new MutableLiveData<>();
        this.repository = new DeliveryRepository(application);
        this.allDeliveries = this.repository.getAllDeliveries();
        this.activeDeliveries = this.repository.getActiveDeliveries();
        this.todaysDeliveries = this.repository.getTodaysDeliveries();
        this.totalCount = this.repository.getTotalCount();
        setupFilteredDeliveries();
    }

    private void setupFilteredDeliveries() {
        this.filteredDeliveries.addSource(this.allDeliveries, new Observer() { // from class: com.mobileinvoice.delivery.viewmodel.DeliveryViewModel$$ExternalSyntheticLambda0
            @Override // androidx.lifecycle.Observer
            public void onChanged(Object obj) {
                DeliveryViewModel.this.lambda$setupFilteredDeliveries$0((List) obj);
            }
        });
        this.filteredDeliveries.addSource(this.statusFilter, new Observer() { // from class: com.mobileinvoice.delivery.viewmodel.DeliveryViewModel$$ExternalSyntheticLambda1
            @Override // androidx.lifecycle.Observer
            public void onChanged(Object obj) {
                DeliveryViewModel.this.lambda$setupFilteredDeliveries$1((DeliveryStatus) obj);
            }
        });
        this.filteredDeliveries.addSource(this.priorityFilter, new Observer() { // from class: com.mobileinvoice.delivery.viewmodel.DeliveryViewModel$$ExternalSyntheticLambda2
            @Override // androidx.lifecycle.Observer
            public void onChanged(Object obj) {
                DeliveryViewModel.this.lambda$setupFilteredDeliveries$2((Priority) obj);
            }
        });
        this.filteredDeliveries.addSource(this.searchQuery, new Observer() { // from class: com.mobileinvoice.delivery.viewmodel.DeliveryViewModel$$ExternalSyntheticLambda3
            @Override // androidx.lifecycle.Observer
            public void onChanged(Object obj) {
                DeliveryViewModel.this.lambda$setupFilteredDeliveries$3((String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupFilteredDeliveries$0(List deliveries) {
        applyFilters();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupFilteredDeliveries$1(DeliveryStatus status) {
        applyFilters();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupFilteredDeliveries$2(Priority priority) {
        applyFilters();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupFilteredDeliveries$3(String query) {
        applyFilters();
    }

    private void applyFilters() {
        List<Delivery> deliveries = this.allDeliveries.getValue();
        if (deliveries == null) {
            this.filteredDeliveries.setValue(null);
            return;
        }
        final DeliveryStatus status = this.statusFilter.getValue();
        final Priority priority = this.priorityFilter.getValue();
        final String query = this.searchQuery.getValue();
        if (status == null && priority == null && (query == null || query.isEmpty())) {
            this.filteredDeliveries.setValue(deliveries);
        } else {
            List<Delivery> filtered = (List) deliveries.stream().filter(new Predicate() { // from class: com.mobileinvoice.delivery.viewmodel.DeliveryViewModel$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public boolean test(Object obj) {
                    return DeliveryViewModel.lambda$applyFilters$4(status, (Delivery) obj);
                }
            }).filter(new Predicate() { // from class: com.mobileinvoice.delivery.viewmodel.DeliveryViewModel$$ExternalSyntheticLambda5
                @Override // java.util.function.Predicate
                public boolean test(Object obj) {
                    return DeliveryViewModel.lambda$applyFilters$5(priority, (Delivery) obj);
                }
            }).filter(new Predicate() { // from class: com.mobileinvoice.delivery.viewmodel.DeliveryViewModel$$ExternalSyntheticLambda6
                @Override // java.util.function.Predicate
                public boolean test(Object obj) {
                    boolean lambda$applyFilters$6;
                    lambda$applyFilters$6 = DeliveryViewModel.this.lambda$applyFilters$6(query, (Delivery) obj);
                    return lambda$applyFilters$6;
                }
            }).collect(Collectors.toList());
            this.filteredDeliveries.setValue(filtered);
        }
    }

    static /* synthetic */ boolean lambda$applyFilters$4(DeliveryStatus status, Delivery d) {
        return status == null || d.getStatus() == status;
    }

    static /* synthetic */ boolean lambda$applyFilters$5(Priority priority, Delivery d) {
        return priority == null || d.getPriority() == priority;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$applyFilters$6(String query, Delivery d) {
        return query == null || query.isEmpty() || matchesSearchQuery(d, query);
    }

    private boolean matchesSearchQuery(Delivery delivery, String query) {
        String lowerQuery = query.toLowerCase();
        return (delivery.getCustomerName() != null && delivery.getCustomerName().toLowerCase().contains(lowerQuery)) || (delivery.getTrackingNumber() != null && delivery.getTrackingNumber().toLowerCase().contains(lowerQuery)) || ((delivery.getStreetAddress() != null && delivery.getStreetAddress().toLowerCase().contains(lowerQuery)) || (delivery.getCity() != null && delivery.getCity().toLowerCase().contains(lowerQuery)));
    }

    public LiveData<List<Delivery>> getAllDeliveries() {
        return this.allDeliveries;
    }

    public LiveData<List<Delivery>> getActiveDeliveries() {
        return this.activeDeliveries;
    }

    public LiveData<List<Delivery>> getTodaysDeliveries() {
        return this.todaysDeliveries;
    }

    public LiveData<List<Delivery>> getFilteredDeliveries() {
        return this.filteredDeliveries;
    }

    public LiveData<Delivery> getDeliveryById(long id) {
        return this.repository.getDeliveryById(id);
    }

    public LiveData<List<Delivery>> getDeliveriesByStatus(DeliveryStatus status) {
        return this.repository.getDeliveriesByStatus(status);
    }

    public LiveData<List<Delivery>> getOverdueDeliveries() {
        return this.repository.getOverdueDeliveries();
    }

    public LiveData<Integer> getTotalCount() {
        return this.totalCount;
    }

    public LiveData<Integer> getCountByStatus(DeliveryStatus status) {
        return this.repository.getCountByStatus(status);
    }

    public LiveData<Integer> getTodaysCount() {
        return this.repository.getTodaysCount();
    }

    public LiveData<Integer> getOverdueCount() {
        return this.repository.getOverdueCount();
    }

    public void setStatusFilter(DeliveryStatus status) {
        this.statusFilter.setValue(status);
    }

    public void setPriorityFilter(Priority priority) {
        this.priorityFilter.setValue(priority);
    }

    public void setSearchQuery(String query) {
        this.searchQuery.setValue(query);
    }

    public void clearFilters() {
        this.statusFilter.setValue(null);
        this.priorityFilter.setValue(null);
        this.searchQuery.setValue(null);
    }

    public void insert(Delivery delivery) {
        this.isLoading.setValue(true);
        this.repository.insert(delivery, new DeliveryRepository.OnDeliveryInsertedListener() { // from class: com.mobileinvoice.delivery.viewmodel.DeliveryViewModel$$ExternalSyntheticLambda7
            @Override // com.mobileinvoice.delivery.data.repository.DeliveryRepository.OnDeliveryInsertedListener
            public void onInserted(long j) {
                DeliveryViewModel.this.lambda$insert$7(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$insert$7(long id) {
        this.isLoading.postValue(false);
        if (id <= 0) {
            this.errorMessage.postValue("Failed to create delivery");
        }
    }

    public void update(Delivery delivery) {
        this.isLoading.setValue(true);
        this.repository.update(delivery);
        this.isLoading.setValue(false);
    }

    public void delete(Delivery delivery) {
        this.repository.delete(delivery);
    }

    public void updateStatus(long id, DeliveryStatus status) {
        this.repository.updateStatus(id, status);
    }

    public void completeDelivery(long id, String recipientName, String signaturePath) {
        this.repository.completeDelivery(id, recipientName, signaturePath);
    }

    public void markDeliveryFailed(long id, String reason) {
        this.repository.markDeliveryFailed(id, reason);
    }

    public void updateRouteOrders(List<Delivery> deliveries) {
        this.repository.updateRouteOrders(deliveries);
    }

    public void deleteCompleted() {
        this.repository.deleteCompleted();
    }

    public LiveData<Boolean> isLoading() {
        return this.isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return this.errorMessage;
    }

    public void clearError() {
        this.errorMessage.setValue(null);
    }
}
