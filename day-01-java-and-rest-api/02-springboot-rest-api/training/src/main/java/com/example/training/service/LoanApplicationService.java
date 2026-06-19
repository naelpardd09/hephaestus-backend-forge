package com.example.training.service;

import com.example.training.dto.LoanApplicationResponse;
import com.example.training.model.LoanApplication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class LoanApplicationService {
    //@Service berfungsi untuk ngasitau Spring kalo class ini tuh komponen Service
    //yang bakal diinject ke controller.
    //loanStorage ini kayak customerStorage di app samping, nah HashMap ini
    //sebagai database localnya.
    //disini key nya itu id, valuenya object daripada LoanApplication.
    //atomic long itu untuk generated id random (auto increment)
    private final Map<Long, LoanApplication> loanStorage = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    //method ini nerima map berisi string dan object krn JSON tanpa DTO otomatis di-parse Spring jd map
    public LoanApplicationResponse createLoanApplication(Map<String, Object> body) {
        Long id = idCounter.getAndIncrement(); //ngambil nilai curren tid lalu lgsg dinaikin, spy id gak duplikat

        // body.get("customer_id") return Object, jadi kita cast ke Number dulu
        // lalu .longValue() untuk dapat Long-nya
        Long customerId = ((Number) body.get("customer_id")).longValue();

        BigDecimal loanAmount = new BigDecimal(body.get("loan_amount").toString());

        int tenorMonth = ((Number) body.get("tenor_month")).intValue();

        String purpose = (String) body.get("purpose");

        LoanApplication loan = new LoanApplication(id, customerId, loanAmount, tenorMonth, purpose, "SUBMITTED");
        loanStorage.put(id, loan);

        return toResponse(loan);
        //semua value Map bertipe Object, mknya perlu di-cast, angka -> Number dlu
        //baru .longValue() atau .intValue(), sedangkan loan_amount via .toString() ke BigDecimal
        //Status di hc kan submitted krn loan yg bru dibuat mulai dari status ini.
    }

    public List<LoanApplicationResponse> getAllLoanApplications() {
        List<LoanApplicationResponse> result = new ArrayList<>();
        for (LoanApplication loan : loanStorage.values()) {
            result.add(toResponse(loan));
        }
        return result;
        //simplenya ni method ngambil all value HashMap, diconvert 1by1
        //dari model LoanApplication ke LoanApplicationResponse sblm ke controller.
        //kita gak lgsg return si modelnya krn model itu kan gbs lgsg digituin, hrs via dto

    }

    public LoanApplicationResponse getLoanApplicationById(Long id) {
        LoanApplication loan = loanStorage.get(id);
        if (loan == null) return null;
        return toResponse(loan);
        //ini tuh sama kek atas, kalo atas GETALL, kalo itu GET by ID. Kalo diatas
        //ada looping for, disini pake ifelse, jd misal loannya null/gaada, dia
        //ngereturn null, TAPI semisal ga null, dia ngereturn ke method toResponse
        //toResponse dijelasin dibwh
    }

    public LoanApplicationResponse approveLoanApplication(Long id) {
        LoanApplication loan = loanStorage.get(id);
        if (loan == null) return null;
        loan.setStatus("APPROVED");
        return toResponse(loan);
    }
    //dua method atas bawah ini kurleb sama, yg atas nge-approve, yg bawah 
    //nge-reject. mirip sama method getLoanApplicationById (cari data by ID,
    //lalu ngubah statusnya). karena si loanStorage itu nyimpen referensi object
    //kita gaperlu pake .put lagi, lgsg aja abis setStatus() si objek dlm mapnya itu
    //keupdate.
    public LoanApplicationResponse rejectLoanApplication(Long id) {
        LoanApplication loan = loanStorage.get(id);
        if (loan == null) return null;
        loan.setStatus("REJECTED");
        return toResponse(loan);
    }

    // method ini simpel tp berfungsi utk ngonvert model ke dto. jadi
    //semua method diatas ckp sekali panggil aja toResponse() nanti auto convert
    //private krn untuk internal class aja.
    private LoanApplicationResponse toResponse(LoanApplication loan) {
        return new LoanApplicationResponse(
                loan.getId(),
                loan.getKastomerId(),
                loan.getLoanAmount(),
                loan.getTenorMonth(),
                loan.getPurpose(),
                loan.getStatus()
        );
    }
}