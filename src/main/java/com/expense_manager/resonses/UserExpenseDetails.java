package com.expense_manager.resonses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExpenseDetails {
    
    private String userName;
    
    private String willGEt;

    private String willPay;

    private String subTotal;

}
