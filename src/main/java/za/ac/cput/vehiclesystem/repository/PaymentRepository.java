package za.ac.cput.vehiclesystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.vehiclesystem.domain.Payment;
import za.ac.cput.vehiclesystem.domain.PaymentStatus;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findByVehicleIdAndIdempotencyKey(String vehicleId, String idempotencyKey);

    boolean existsByVehicleIdAndStatus(String vehicleId, PaymentStatus status);
}
