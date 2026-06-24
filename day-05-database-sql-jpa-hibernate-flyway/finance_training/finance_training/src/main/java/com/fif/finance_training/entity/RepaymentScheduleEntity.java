package com.fif.finance_training.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import com.fif.finance_training.entity.enums.RepaymentStatus;
@Entity
@Table(name = "repayment_schedules") // Nama tabel di database
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepaymentScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relasi ke tabel Loan/Pinjaman (Many-to-One)
    // Satu pinjaman (loan_application) bisa memiliki banyak jadwal cicilan
    // Catatan: Saya menggunakan 'LoanEntity' dari class sebelumnya. 
    // Jika kamu punya class khusus bernama 'LoanApplicationEntity', silakan ubah tipe datanya.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_application_id", nullable = false)
    private LoanApplicationEntity loanApplication;

    // Alternatif jika hanya ingin menyimpan ID-nya saja tanpa relasi Hibernate:
    // @Column(name = "loan_application_id", nullable = false)
    // private Long loanApplicationId;

    @Column(name = "installment_number", nullable = false)
    private Integer installmentNumber; // Contoh: Cicilan ke-1, ke-2, dst.

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate; // Hanya tanggal (YYYY-MM-DD)

    @Column(name = "principal_amount", nullable = false)
    private BigDecimal principalAmount; // Jumlah pokok pinjaman yang dibayar

    @Column(name = "interest_amount", nullable = false)
    private BigDecimal interestAmount; // Jumlah bunga yang dibayar

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount; // Total yang harus dibayar (Pokok + Bunga)

    @Enumerated(EnumType.STRING)
@Column(name = "status", nullable = false)
private RepaymentStatus status = RepaymentStatus.UNPAID; // Sama seperti sebelumnya, sangat disarankan menggunakan Enum
    // Contoh status: UNPAID, PARTIAL, PAID, LATE

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
